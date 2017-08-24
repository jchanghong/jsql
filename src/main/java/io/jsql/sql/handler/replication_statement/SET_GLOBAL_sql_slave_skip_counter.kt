/*
 * Java-based distributed database like Mysql
 */

package io.jsql.sql.handler.replication_statement

import io.jsql.config.ErrorCode
import io.jsql.sql.OConnection

/**
 * Created by dell on 2017/3/27.
 * SET GLOBAL sql_slave_skip_counter = N
 */
object SET_GLOBAL_sql_slave_skip_counter {
    internal var N: String? = null

    fun isMe(sql: String): Boolean {
        val strings = sql.split("\\s+".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        var s: String? = null
        if (strings.size > 2 && strings[0].equals("SET", ignoreCase = true) && strings[0].equals("global", ignoreCase = true)) {
            for (i in 3..strings.size - 1)
                s = s!! + strings[i]
            N = s!!.substring(s.length - 1)
            s = s.substring(0, s.length - 2)
            if (s.equals("sql_slave_skip_counter=", ignoreCase = true))
                return true
        }
        return false
    }

    fun handle(sql: String, c: OConnection) {
        c.writeErrMessage(ErrorCode.ER_NOT_SUPPORTED_YET, "暂未支持")
    }
}
