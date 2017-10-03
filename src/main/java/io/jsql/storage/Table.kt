/*
 * Java-based distributed database like Mysql
 */

package io.jsql.storage

import com.orientechnologies.orient.core.metadata.schema.OClass
import com.orientechnologies.orient.core.record.OElement
import java.util.stream.Stream

/**
 * Created by 长宏 on 2017/3/20 0020.
 * 表有关的功能
 */
interface Table {
    @Throws(StorageException::class)
    fun droptableSyn(dbname: String, table: String)

    @Throws(StorageException::class)
    fun createtableSyn(dbname: String, createTableStatement: MySqlCreateTableStatement)

    @Throws(StorageException::class)
    fun getalltable(dbname: String): List<String>

    @Throws(StorageException::class)
    fun gettableclass(tablename: String, db: String): OClass

    @Throws(StorageException::class)
    fun selectSyn(oClass: OClass, dbname: String): Stream<OElement>
}
