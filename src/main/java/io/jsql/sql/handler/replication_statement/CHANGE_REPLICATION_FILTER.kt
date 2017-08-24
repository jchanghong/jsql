/*
 * Java-based distributed database like Mysql
 */

package io.jsql.sql.handler.replication_statement

import io.jsql.config.ErrorCode
import io.jsql.sql.OConnection

/**
 * Created by dell on 2017/3/27.
 * eg:
 * CHANGE REPLICATION FILTER
 * REPLICATE_DO_DB = (d1), REPLICATE_IGNORE_DB = (d2);
 */
object CHANGE_REPLICATION_FILTER {
    internal var options: String? = null

    fun isMe(sql: String): Boolean {
        val strings = sql.split("\\s+".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        if (strings.size > 3 && strings[0].equals("change", ignoreCase = true) && strings[1].equals("replication", ignoreCase = true) && strings[2].equals("filter", ignoreCase = true)) {
            for (i in 4..strings.size - 1)
                options = options!! + strings[i]
            return true
        }
        return false
    }

    fun handle(sql: String, c: OConnection) {
        c.writeErrMessage(ErrorCode.ER_NOT_SUPPORTED_YET, "暂未支持")
    }
}
