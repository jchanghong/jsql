/*
 * Java-based distributed database like Mysql
 */

package io.jsql.sql.handler.replication_statement

import io.jsql.config.ErrorCode
import io.jsql.sql.OConnection

/**
 * Created by zhang on 2017/3/27.
 * PURGE  BINARY  LOGS TO  BEFORE datetime_expr
 * eg:
 * PURGE BINARY LOGS BEFORE '2008-04-02 22:46:26';
 */
object PURGE_BINARY_LOGS_BEFORE {
    fun isMe(sql: String): Boolean {
        val strings = sql.split("\\s+".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        return strings.size > 4 && strings[0].equals("purge", ignoreCase = true) && strings[1].equals("binary", ignoreCase = true) && strings[2].equals("logs", ignoreCase = true) && strings[3].equals("before", ignoreCase = true)
    }

    fun handle(sql: String, c: OConnection) {
        c.writeErrMessage(ErrorCode.ER_NOT_SUPPORTED_YET, "暂未支持")
    }
}
