/*
 * Java-based distributed database like Mysql
 */

package io.jsql.sql.handler.data_define

import io.jsql.sql.OConnection

/**
 * Created by 长宏 on 2017/3/18 0018.
 */
object DropEVENT {
    fun isdropevent(sql: String): Boolean {
        val strings = sql.split("\\s+".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        return strings.size > 2 && strings[0].equals("drop", ignoreCase = true) && strings[1].equals("event", ignoreCase = true)
    }

    fun handle(sql: String, c: OConnection) {

        c.writeok()

    }
}
