package io.jsql.sql.handler.replication_statement

import io.jsql.config.ErrorCode
import io.jsql.sql.OConnection

/**
 * Created by zhang on 2017/3/27.
 * SET sql_log_bin = {0|1}
 */
object set_sql_log_bin {
    fun isMe(sql: String): Boolean {
        val strings = sql.split("\\s+".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        var s: String? = null
        if (strings.size >= 2 && strings[0].equals("set", ignoreCase = true)) {
            for (i in 1..strings.size - 1)
                s = s!! + strings[i]
            if (s!!.equals("sql_log_bin=0", ignoreCase = true) || s.equals("sql_log_bin=1", ignoreCase = true))
                return true
        }
        return false
    }

    fun handle(sql: String, c: OConnection) {
        c.writeErrMessage(ErrorCode.ER_NOT_SUPPORTED_YET, "暂未支持")
    }
}
