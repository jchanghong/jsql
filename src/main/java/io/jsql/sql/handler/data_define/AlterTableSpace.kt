/*
 * Java-based distributed database like Mysql
 */

package io.jsql.sql.handler.data_define

import io.jsql.sql.OConnection

/**
 * Created by 长宏 on 2017/3/18 0018.
 * ALTER TABLESPACE tablespace_name
 * {ADD|DROP} DATAFILE 'file_name'
 * [INITIAL_SIZE [=] size]
 * [WAIT]
 * ENGINE [=] engine_name
 * This statement can be used either to add a new data file, or to drop a data file from a tablespace.
 */
object AlterTableSpace {
    fun isme(sql: String): Boolean {
        val sqll = sql.toUpperCase().trim { it <= ' ' }
        val list = sqll.split("\\s+".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        return list.size > 2 && list[0] == "ALTER" && list[1] == "TABLESPACE"
    }

    fun handle(sql: String, c: OConnection) {
        c.writeok()
    }
}
