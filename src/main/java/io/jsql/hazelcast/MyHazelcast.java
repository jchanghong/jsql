package io.jsql.hazelcast;

import com.google.common.collect.Lists;
import com.hazelcast.config.Config;
import com.hazelcast.core.*;
import com.hazelcast.nio.Address;
import io.jsql.mysql.handler.MysqlSQLhander;
import io.jsql.sql.OConnectionCMD;
import io.jsql.storage.DB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by 长宏 on 2017/5/5 0005.
 * sql队列
 * 复制队列
 * 命令队列。
 * 2个锁。
 * 发布
 */
@Component
public class MyHazelcast implements ItemListener<SqlUpdateLog>{
  volatile   private boolean isreplicating ;
    private final ItemListener<SqlUpdateLog> remotequenelister = new replicationlister();
    private HazelcastInstance hazelcastInstance;
    private LinkedList<SqlUpdateLog> localquene = new LinkedList<>();
    private LinkedList<SqlUpdateLog> localqueneReplication = new LinkedList<>();
    private IQueue<SqlUpdateLog> remotequene;
    private IQueue<SqlUpdateLog> replicationQuene;
    private IQueue<ReplicationCMD> cmdiQueue;
    private ItemListener<ReplicationCMD> cmdItemListener;
    private IdGenerator idGenerator;
    private long id_froCMD=-1;
    ILock iLockwrite;
    ILock iLockread;
    ILock ilockcmdquene;
    @Autowired
    LogFile logFile;
    Logger logger = LoggerFactory.getLogger(MyHazelcast.class.getName());
    private long locals_maxlsn;
    private Member localmember;
    private IAtomicLong iAtomic_remote_lsn;
    @Autowired
    OConnectionCMD oConnectionCMD;
    @Autowired
    ApplicationContext applicationContext;
    //    LinkedList<SqlUpdateLog> logfiletest = new LinkedList<>();
    MysqlSQLhander sqLhander = new MysqlSQLhander();
    public MyHazelcast() {
    }

  public   void inits() {
        Config config = new Config();
        hazelcastInstance = Hazelcast.newHazelcastInstance(config);
        localmember = hazelcastInstance.getCluster().getLocalMember();
        remotequene = hazelcastInstance.getQueue("sqls");
        remotequene.addItemListener(this, true);
        replicationQuene = hazelcastInstance.getQueue("sendingsqls");
        replicationQuene.addItemListener(remotequenelister, true);
        iLockwrite = hazelcastInstance.getLock("ilocakwrite");
        iLockread = hazelcastInstance.getLock("ilockread");
        iAtomic_remote_lsn = hazelcastInstance.getAtomicLong("lsn");
        ilockcmdquene = hazelcastInstance.getLock("ilockcmdquene");
        cmdiQueue = hazelcastInstance.getQueue("cmds");
        cmdItemListener = new cmdlister();
        idGenerator = hazelcastInstance.getIdGenerator("idgenerator");
        cmdiQueue.addItemListener(cmdItemListener, true);
//        locals_maxlsn = logFile.maxLSN();
        sqLhander.setReadOnly(false);
        locals_maxlsn = logFile.maxLSN();
        sqLhander = applicationContext.getBean(MysqlSQLhander.class);
        sqLhander.setConnection(oConnectionCMD);
        init();
    }

    class cmdlister implements ItemListener<ReplicationCMD> {

        @Override
        public void itemAdded(ItemEvent<ReplicationCMD> item) {
            ReplicationCMD replicationCMD = item.getItem();
            if (replicationCMD.id == id_froCMD) {
                return;
            }
            if (locals_maxlsn < replicationCMD.tolsn) {
                return;
            }
            if (ilockcmdquene.tryLock()) {
                 replicationCMD = cmdiQueue.poll();
                ilockcmdquene.unlock();
                sendlogdata(replicationCMD.fromlsn, replicationCMD.tolsn);
            }

        }

        @Override
        public void itemRemoved(ItemEvent<ReplicationCMD> item) {

        }
    }

    class replicationlister implements ItemListener<SqlUpdateLog> {

        @Override
        public void itemAdded(ItemEvent<SqlUpdateLog> item) {
            if (!isreplicating) {
                return;
            }
            long max = iAtomic_remote_lsn.get();
            if (item.getItem().LSN == max) {
                localqueneReplication.addLast(item.getItem());
                exeSqlforReplication();
            }
            else {
                if (item.getItem().LSN > locals_maxlsn) {
                    localqueneReplication.addLast(item.getItem());
                }
            }

        }
        @Override
        public void itemRemoved(ItemEvent<SqlUpdateLog> item) {

        }
    }

    //rep quene
    private void exeSqlforReplication() {
        Collections.sort(localqueneReplication);
        SqlUpdateLog log = localqueneReplication.poll();
        long lastlsn = 0;
        while (log != null) {
//            ------------------------------------------------------------
            locals_maxlsn = log.LSN;
            logger.info("exeSqlforReplication exe sql " + log);
            oConnectionCMD.schema = log.db;
            sqLhander.handle(log);

//            logfiletest.addLast(log);
            logFile.write(log);
            lastlsn = log.LSN;
            log = localqueneReplication.poll();
        }
        isreplicating = false;
        log = localquene.poll();
        long remotel = iAtomic_remote_lsn.get();
        while (log != null) {
//            ------------------------------------------------------------
            locals_maxlsn = log.LSN;
            logger.info("exe  Sql : " + log);
            oConnectionCMD.schema = log.db;
            sqLhander.handle(log);

//            logfiletest.addLast(log);
            logFile.write(log);
            lastlsn = log.LSN;
            if (lastlsn > remotel) {
                remotequene.offer(log);
            }
            log = localqueneReplication.poll();
        }
        if (lastlsn > remotel) {
            iAtomic_remote_lsn.set(lastlsn);
        }
    }

    private void sendlogdata(long fromlsn, long tolsn) {
//        logfiletest.forEach(a->{
//            if (a.LSN >= fromlsn && a.LSN <= tolsn) {
//                replicationQuene.offer(a);
//                logger.info("send log:" + a);
//            }
//        });
        logFile.getall().forEach(a->{
            if (a.LSN >= fromlsn && a.LSN <= tolsn) {
                replicationQuene.offer(a);
                logger.info("send log:" + a);
            }
        });
        logger.info("send log data 从"+fromlsn+" to "+tolsn);
    }
    private void init() {
        long l = iAtomic_remote_lsn.get();
        logger.info("remote lsn is" + l);
        if (locals_maxlsn < l) {
            isreplicating = true;
            id_froCMD = idGenerator.newId();
            cmdiQueue.add(new ReplicationCMD(locals_maxlsn + 1, l, id_froCMD));
        }
        else {
            iAtomic_remote_lsn.set(locals_maxlsn);
        }
    }
//本机发出的sql语句
    public void exeSql(String sql,String db) {
//        if (isupdatesql(sql)) {
            if (isreplicating) {
                long l = iAtomic_remote_lsn.get();
                SqlUpdateLog log = new SqlUpdateLog(l+1, sql,db);
                localquene.addLast(log);
            }
            else {
                locals_maxlsn++;
                long l = iAtomic_remote_lsn.addAndGet(1);
                SqlUpdateLog log = new SqlUpdateLog(l, sql,db);
                logger.info("exe sql " + log);
//                logfiletest.addLast(log);
                logFile.write(log);
                remotequene.offer(log);
            }
//        }
    }
    private boolean isupdatesql(String sql) {
        String sqll = sql.toLowerCase();
        List<String> list = Lists.newArrayList("delete", "drop", "create", "insert", "update");
        for (String s : list) {
            if (sqll.contains(s)) {
                return true;
            }
        }
        return false;
    }

    //remote quene
    @Override
    public void itemAdded(ItemEvent<SqlUpdateLog> item) {
        if (item.getItem().LSN > locals_maxlsn) {
            localquene.offer(item.getItem());
            if (!isreplicating) {
                exesqlforremoteData();
            }
        }
    }

    //remote quene
    private void exesqlforremoteData() {
        Collections.sort(localquene);
        SqlUpdateLog log = localquene.poll();
        long last = 0;
        while (log != null) {
//            ------------------------------------------------------------
            locals_maxlsn = log.LSN;
            logger.info("exesqlforremoteData()  " + log);
            oConnectionCMD.schema = log.db;
            sqLhander.handle(log);

//            logfiletest.offer(log);
            logFile.write(log);

            last = log.LSN;
            log = localquene.poll();
        }
        long l = iAtomic_remote_lsn.get();
        if (last > l) {
            iAtomic_remote_lsn.set(last);
        }

    }

    @Override
    public void itemRemoved(ItemEvent<SqlUpdateLog> item) {
        logger.info(item.toString());
    }


}
