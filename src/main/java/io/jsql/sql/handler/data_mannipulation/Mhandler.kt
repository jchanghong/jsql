/*
 * Java-based distributed database like Mysql
 */

package io.jsql.sql.handler.data_mannipulation

import io.jsql.sql.OConnection

/**
 * Created by 长宏 on 2017/3/18 0018.
 * HANDLER tbl_name READ `PRIMARY` ...
 */
object Mhandler {
    fun isme(sql: String): Boolean {
        val sqll = sql.toUpperCase().trim { it <= ' ' }
        val list = sqll.split("\\s+".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        return list.size > 0 && list[0] == "HANDLER"
    }

    fun handle(sql: String, c: OConnection) {
        c.writeok()
    }
}
