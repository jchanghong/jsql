/*
 * Java-based distributed database like Mysql
 */

package io.jsql.orientstorage.adapter

import com.orientechnologies.orient.core.metadata.schema.OClass
import com.orientechnologies.orient.core.metadata.schema.OType
import com.orientechnologies.orient.core.record.OElement
import io.jsql.orientstorage.sqlhander.sqlutil.MSQLutil
import io.jsql.sql.OConnection
import io.jsql.storage.DB
import io.jsql.storage.StorageException
import io.jsql.storage.Table
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*
import java.util.stream.Stream
import javax.annotation.PostConstruct

/**
 * Created by 长宏 on 2017/5/3 0003.
 */
@Service
class OTable : Table {
    @Autowired
    lateinit private var dbadmin: DB

    @PostConstruct
    internal fun post() {
        OConnection.TABLE_ADMIN = this
    }

    @Throws(StorageException::class)
    override fun droptableSyn(dbname: String, table: String) {
        val document = dbadmin.getdb(dbname)
        document.activateOnCurrentThread()
        val schema = document.metadata.schema
        if (!schema.existsClass(table)) {
            throw StorageException("table exist")
        }
        schema.dropClass(table)
        dbadmin.close(document)
    }

    @Throws(StorageException::class)
    override fun createtableSyn(dbname: String, createTableStatement: MySqlCreateTableStatement) {

        if (!dbadmin.getallDBs().contains(dbname)) {
            throw StorageException("db不存在")
        }
        val db = dbadmin.getdb(dbname)
        db.activateOnCurrentThread()
        val oSchema = db.metadata.schema
        val table = createTableStatement.tableSource.toString()
        if (oSchema.existsClass(table)) {
            dbadmin.close(db)
            throw StorageException("table已经存在")
        }
        val oClass = db
                .createClass(table)
        oClass.isStrictMode = true
        val dstring = ArrayList<String>()
        createTableStatement.tableElementList.forEach { a ->
            if (a is SQLColumnDefinition) {
                if (a.constraints != null) {
                    dstring.add(a.name.toString())
                }
            }
        }
        val maps = MSQLutil.gettablenamefileds(createTableStatement)
        maps.entries.forEach { e ->
            if (e.value.toLowerCase().contains("int")) {
                oClass.createProperty(e.key, OType.INTEGER)
                if (dstring.contains(e.key)) {
                    oClass.getProperty(e.key).createIndex(OClass.INDEX_TYPE.NOTUNIQUE)
                }
            } else if (e.value.toLowerCase().contains("varchar")) {
                oClass.createProperty(e.key, OType.STRING)
                if (dstring.contains(e.key)) {
                    oClass.getProperty(e.key).createIndex(OClass.INDEX_TYPE.NOTUNIQUE)
                }
            } else if (e.value.toLowerCase().contains("datetime")) {
                oClass.createProperty(e.key, OType.DATETIME)
                if (dstring.contains(e.key)) {
                    oClass.getProperty(e.key).createIndex(OClass.INDEX_TYPE.NOTUNIQUE)
                }
            } else if (e.value.toLowerCase().contains("times")) {
                oClass.createProperty(e.key, OType.DATETIME)
                if (dstring.contains(e.key)) {
                    oClass.getProperty(e.key).createIndex(OClass.INDEX_TYPE.NOTUNIQUE)
                }
            } else {
                oClass.createProperty(e.key, OType.STRING)
                if (dstring.contains(e.key)) {
                    oClass.getProperty(e.key).createIndex(OClass.INDEX_TYPE.NOTUNIQUE)
                }
            }
        }
        dbadmin.close(db)
    }

    @Throws(StorageException::class)
    override fun getalltable(dbname: String): List<String> {
        val document = dbadmin.getdb(dbname)
        document.activateOnCurrentThread()
        val strings = ArrayList<String>()
        document.metadata.schema.classes.forEach { a -> strings.add(a.name) }
        dbadmin.close(document)
        return strings
    }

    @Throws(StorageException::class)
    override fun gettableclass(tablename: String, db: String): OClass {
        val document = dbadmin.getdb(db)
        val b = document.metadata.schema.existsClass(tablename)
        if (!b) {
            dbadmin.close(document)
            throw StorageException("not exits")
        }
        val aClass = document.getClass(tablename)
        dbadmin.close(document)
        return aClass
    }

    @Throws(StorageException::class)
    override fun selectSyn(oClass: OClass, dbname: String): Stream<OElement> {
        return dbadmin.query("select * from " + oClass.name, dbname)
    }
}
