/*
 * Java-based distributed database like Mysql 
 */

package io.jsql.storage

import com.orientechnologies.orient.core.db.document.ODatabaseDocument
import com.orientechnologies.orient.core.record.OElement
import com.orientechnologies.orient.core.sql.executor.OResult
import java.util.*
import java.util.stream.Stream

/**
 * Created by 长宏 on 2017/3/20 0020.
 * 和数据库有关的接口
 */
interface DB {
    /**
     * Deletedb syn.同步操作

     * @param dbname the dbname
     * *
     * @throws StorageException the m exception
     */
    @Throws(StorageException::class)
    fun deletedbSyn(dbname: String)

    fun deletedbAyn(dbname: String)
    @Throws(StorageException::class)
    fun createdbSyn(dbname: String)

    fun createdbAsyn(dbname: String)
    @Throws(StorageException::class)
    fun getallDBs(): List<String>

    @Throws(StorageException::class)
    fun exesqlforResult(sql: String, dbname: String): Optional<OResult>

    fun exesqlNoResultAsyn(sql: String, dbname: String)
    @Throws(StorageException::class)
    fun query(sqlquery: String, dbname: String): Stream<OElement>

    @Throws(StorageException::class)
    fun getdb(dbname: String): ODatabaseDocument

    fun close()

    @Throws(StorageException::class)
    fun exe(sql: String, db: String)

    fun close(databaseDocument: ODatabaseDocument)
}
