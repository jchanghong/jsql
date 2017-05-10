package io.jsql.hazelcast

import com.google.common.collect.Lists
import com.hazelcast.config.Config
import com.hazelcast.core.*
import com.hazelcast.nio.Address
import io.jsql.mysql.handler.MysqlSQLhander
import io.jsql.sql.OConnectionCMD
import io.jsql.storage.DB
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component

import javax.annotation.PostConstruct
import java.util.Collections
import java.util.LinkedList

/**
 * Created by 长宏 on 2017/5/5 0005.
 * sql队列
 * 复制队列
 * 命令队列。
 * 2个锁。
 * 发布
 */
@Component
class MyHazelcast : ItemListener<SqlUpdateLog> {
    @Volatile private var isreplicating: Boolean = false
    private val remotequenelister = replicationlister()
    var hazelcastInstance: HazelcastInstance? = null
        private set
    private val localquene = LinkedList<SqlUpdateLog>()
    private val localqueneReplication = LinkedList<SqlUpdateLog>()
    private var remotequene: IQueue<SqlUpdateLog>? = null
    private var replicationQuene: IQueue<SqlUpdateLog>? = null
    private var cmdiQueue: IQueue<ReplicationCMD>? = null
    private var cmdItemListener: ItemListener<ReplicationCMD>? = null
    private var idGenerator: IdGenerator? = null
    private var id_froCMD: Long = -1
    internal var iLockwrite: ILock?=null
    internal var iLockread: ILock?=null
    internal var ilockcmdquene: ILock?=null
    @Autowired
    internal var logFile: LogFile? = null
    internal var logger = LoggerFactory.getLogger(MyHazelcast::class.java.name)
    private var locals_maxlsn: Long = 0
    private var localmember: Member? = null
    private var iAtomic_remote_lsn: IAtomicLong? = null
    @Autowired
    internal var oConnectionCMD: OConnectionCMD? = null
    @Autowired
    internal var applicationContext: ApplicationContext? = null
    //    LinkedList<SqlUpdateLog> logfiletest = new LinkedList<>();
    internal var sqLhander = MysqlSQLhander()

    fun inits() {
        val config = Config()
        hazelcastInstance = Hazelcast.newHazelcastInstance(config)
        localmember = hazelcastInstance!!.cluster.localMember
        remotequene = hazelcastInstance!!.getQueue<SqlUpdateLog>("sqls")
        remotequene!!.addItemListener(this, true)
        replicationQuene = hazelcastInstance!!.getQueue<SqlUpdateLog>("sendingsqls")
        replicationQuene!!.addItemListener(remotequenelister, true)
        iLockwrite = hazelcastInstance!!.getLock("ilocakwrite")
        iLockread = hazelcastInstance!!.getLock("ilockread")
        iAtomic_remote_lsn = hazelcastInstance!!.getAtomicLong("lsn")
        ilockcmdquene = hazelcastInstance!!.getLock("ilockcmdquene")
        cmdiQueue = hazelcastInstance!!.getQueue<ReplicationCMD>("cmds")
        cmdItemListener = cmdlister()
        idGenerator = hazelcastInstance!!.getIdGenerator("idgenerator")
        cmdiQueue!!.addItemListener(cmdItemListener, true)
        //        locals_maxlsn = logFile.maxLSN();
//        sqLhander.setReadOnly(false)
        locals_maxlsn = logFile!!.maxLSN()
        sqLhander = applicationContext!!.getBean(MysqlSQLhander::class.java)
        sqLhander.setConnection(oConnectionCMD!!)
        init()
    }

    internal inner class cmdlister : ItemListener<ReplicationCMD> {

        override fun itemAdded(item: ItemEvent<ReplicationCMD>) {
            var replicationCMD = item.item
            if (replicationCMD.id == id_froCMD) {
                return
            }
            if (locals_maxlsn < replicationCMD.tolsn) {
                return
            }
            if (ilockcmdquene!!.tryLock()) {
                replicationCMD = cmdiQueue!!.poll()
                ilockcmdquene!!.unlock()
                sendlogdata(replicationCMD.fromlsn, replicationCMD.tolsn)
            }

        }

        override fun itemRemoved(item: ItemEvent<ReplicationCMD>) {

        }
    }

    internal inner class replicationlister : ItemListener<SqlUpdateLog> {

        override fun itemAdded(item: ItemEvent<SqlUpdateLog>) {
            if (!isreplicating) {
                return
            }
            val max = iAtomic_remote_lsn!!.get()
            if (item.item.LSN == max) {
                localqueneReplication.addLast(item.item)
                exeSqlforReplication()
            } else {
                if (item.item.LSN > locals_maxlsn) {
                    localqueneReplication.addLast(item.item)
                }
            }

        }

        override fun itemRemoved(item: ItemEvent<SqlUpdateLog>) {

        }
    }

    //rep quene
    private fun exeSqlforReplication() {
        Collections.sort(localqueneReplication)
        var log: SqlUpdateLog? = localqueneReplication.poll()
        var lastlsn: Long = 0
        while (log != null) {
            //            ------------------------------------------------------------
            locals_maxlsn = log.LSN
            logger.info("exeSqlforReplication exe sql " + log)
            oConnectionCMD!!.schema = log.db
            sqLhander.handle(log)

            //            logfiletest.addLast(log);
            logFile!!.write(log)
            lastlsn = log.LSN
            log = localqueneReplication.poll()
        }
        isreplicating = false
        log = localquene.poll()
        val remotel = iAtomic_remote_lsn!!.get()
        while (log != null) {
            //            ------------------------------------------------------------
            locals_maxlsn = log.LSN
            logger.info("exe  Sql : " + log)
            oConnectionCMD!!.schema = log.db
            sqLhander.handle(log)

            //            logfiletest.addLast(log);
            logFile!!.write(log)
            lastlsn = log.LSN
            if (lastlsn > remotel) {
                remotequene!!.offer(log)
            }
            log = localqueneReplication.poll()
        }
        if (lastlsn > remotel) {
            iAtomic_remote_lsn!!.set(lastlsn)
        }
    }

    private fun sendlogdata(fromlsn: Long, tolsn: Long) {
        //        logfiletest.forEach(a->{
        //            if (a.LSN >= fromlsn && a.LSN <= tolsn) {
        //                replicationQuene.offer(a);
        //                logger.info("send log:" + a);
        //            }
        //        });
        logFile!!.getall().forEach { a ->
            if (a.LSN >= fromlsn && a.LSN <= tolsn) {
                replicationQuene!!.offer(a)
                logger.info("send log:" + a)
            }
        }
        logger.info("send log data 从$fromlsn to $tolsn")
    }

    private fun init() {
        val l = iAtomic_remote_lsn!!.get()
        logger.info("remote lsn is" + l)
        if (locals_maxlsn < l) {
            isreplicating = true
            id_froCMD = idGenerator!!.newId()
            cmdiQueue!!.add(ReplicationCMD(locals_maxlsn + 1, l, id_froCMD))
        } else {
            iAtomic_remote_lsn!!.set(locals_maxlsn)
        }
    }

    //本机发出的sql语句
    fun exeSql(sql: String, db: String) {
        //        if (isupdatesql(sql)) {
        if (isreplicating) {
            val l = iAtomic_remote_lsn!!.get()
            val log = SqlUpdateLog(l + 1, sql, db)
            localquene.addLast(log)
        } else {
            locals_maxlsn++
            val l = iAtomic_remote_lsn!!.addAndGet(1)
            val log = SqlUpdateLog(l, sql, db)
            logger.info("exe sql " + log)
            //                logfiletest.addLast(log);
            logFile!!.write(log)
            remotequene!!.offer(log)
        }
        //        }
    }

    private fun isupdatesql(sql: String): Boolean {
        val sqll = sql.toLowerCase()
        val list = Lists.newArrayList("delete", "drop", "create", "insert", "update")
        for (s in list) {
            if (sqll.contains(s)) {
                return true
            }
        }
        return false
    }

    //remote quene
    override fun itemAdded(item: ItemEvent<SqlUpdateLog>) {
        if (item.item.LSN > locals_maxlsn) {
            localquene.offer(item.item)
            if (!isreplicating) {
                exesqlforremoteData()
            }
        }
    }

    //remote quene
    private fun exesqlforremoteData() {
        Collections.sort(localquene)
        var log: SqlUpdateLog? = localquene.poll()
        var last: Long = 0
        while (log != null) {
            //            ------------------------------------------------------------
            locals_maxlsn = log.LSN
            logger.info("exesqlforremoteData()  " + log)
            oConnectionCMD!!.schema = log.db
            sqLhander.handle(log)

            //            logfiletest.offer(log);
            logFile!!.write(log)

            last = log.LSN
            log = localquene.poll()
        }
        val l = iAtomic_remote_lsn!!.get()
        if (last > l) {
            iAtomic_remote_lsn!!.set(last)
        }

    }

    override fun itemRemoved(item: ItemEvent<SqlUpdateLog>) {
        logger.info(item.toString())
    }


}
