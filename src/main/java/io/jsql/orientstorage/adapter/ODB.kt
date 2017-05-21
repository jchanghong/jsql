package io.jsql.orientstorage.adapter

import com.orientechnologies.orient.core.db.ODatabaseType
import com.orientechnologies.orient.core.db.OrientDB
import com.orientechnologies.orient.core.db.OrientDBConfig
import com.orientechnologies.orient.core.db.document.ODatabaseDocument
import com.orientechnologies.orient.core.record.OElement
import com.orientechnologies.orient.core.sql.executor.OResult
import com.orientechnologies.orient.core.sql.executor.OResultSet
import io.jsql.cache.MCache
import io.jsql.orientstorage.MdatabasePool
import io.jsql.sql.OConnection
import io.jsql.storage.DB
import io.jsql.storage.StorageException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

import javax.annotation.PostConstruct
import java.util.Optional
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.stream.Stream

/**
 * Created by 长宏 on 2017/5/3 0003.
 */
@Service
class ODB : DB {
    private val orientDB: OrientDB

    @Value("\${db.dir}")
    var dbDIR = "databases"
    @Autowired
    private val pool: MdatabasePool? = null
    private val dbcaches:MutableSet<String> = mutableSetOf()

    init {
        val builder = StringBuilder("embedded:")
        builder.append("./")
        builder.append(dbDIR)
        builder.append("/")
        val confi = OrientDBConfig.defaultConfig()
        orientDB = OrientDB(builder.toString(), null, null, confi)
        //     pool = new MdatabasePool(orientDB);

    }

    @PostConstruct
    internal fun post() {
        OConnection.DB_ADMIN = this
        pool!!.setOrientDB(orientDB)
    }

    private val executorService = Executors.newFixedThreadPool(2)

    //    private Map<String, ODatabasePool> dbpoolmap = new HashMap<>();

    @Throws(StorageException::class)
    override fun deletedbSyn(dbname: String) {
        if (!orientDB.exists(dbname)) {
            throw StorageException("db not exits")
        }
        dbcaches.remove(dbname)
        MCache.showdb().clear()
        orientDB.drop(dbname)
    }

    override fun deletedbAyn(dbname: String) {

        executorService.execute {
            try {
                deletedbSyn(dbname)
            } catch (e: StorageException) {
                e.printStackTrace()
            }


        }
    }

    @Throws(StorageException::class)
    override fun createdbSyn(dbname: String) {
        if (orientDB.exists(dbname)) {
            throw StorageException("db exits")
        }
        dbcaches.add(dbname)
        MCache.showdb().clear()
        orientDB.create(dbname, ODatabaseType.PLOCAL)
    }

    override fun createdbAsyn(dbname: String) {
        executorService.execute {
            try {
                createdbSyn(dbname)
            } catch (e: StorageException) {
                e.printStackTrace()
            }


        }
    }

    @Throws(StorageException::class)
    override fun getallDBs(): List<String> {
        if (dbcaches.size == 0) {
            dbcaches.addAll(orientDB.list())
        } else {
            return dbcaches.toList()
        }
        return orientDB.list()
    }

    @Throws(StorageException::class)
    override fun exesqlforResult(sql: String, dbname: String): Optional<OResult> {
        val document = getdb(dbname)
        document.activateOnCurrentThread()
        val arg:Array<Any>?=null
        val command = document.command(sql, arg)
        pool!!.close(document)
        return command.stream().findAny()
    }

    override fun exesqlNoResultAsyn(sql: String, dbname: String) {
        executorService.execute {
            try {
                exesqlforResult(sql, dbname)
            } catch (e: StorageException) {
                e.printStackTrace()
            }
        }
    }

    @Throws(StorageException::class)
    override fun query(sqlquery: String, dbname: String): Stream<OElement> {
        val document = getdb(dbname)
        document.activateOnCurrentThread()
        var arg:Array<Any>?=null
        val resultSet = document.query(sqlquery, arg)
        val oElementStream = resultSet.stream().map { a -> a.toElement() }
        pool!!.close(document)
        return oElementStream

    }

    @Throws(StorageException::class)
    override fun getdb(dbname: String): ODatabaseDocument {
        if (!orientDB.exists(dbname)) {
            throw StorageException("db not exists")
        }
        return pool!!.get(dbname)
    }

    override fun close() {
        try {
            pool!!.close()
            orientDB.close()
            executorService.shutdown()
        } catch (e: Exception) {
            //            e.printStackTrace();
        }

    }

    override fun exe(sql: String, db: String) {

    }

    override fun close(databaseDocument: ODatabaseDocument) {
        pool!!.close(databaseDocument)

    }
}
