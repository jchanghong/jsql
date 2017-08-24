/*
 * Java-based distributed database like Mysql
 */

package io.jsql.sql.handler.replication_statement

import io.jsql.config.ErrorCode
import io.jsql.sql.OConnection

/**
 * Created by zhang on 2017/3/27.
 * PURGE  BINARY  LOGS TO 'log_name';
 * eg:
 * PURGE BINARY LOGS TO 'mysql-bin.010';
 */
object PURGE_BINARY_LOGS_TO {
    fun isMe(sql: String): Boolean {
        val strings = sql.split("\\s+".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        return strings.size > 4 && strings[0].equals("purge", ignoreCase = true) && strings[1].equals("binary", ignoreCase = true) && strings[2].equals("logs", ignoreCase = true) && strings[3].equals("to", ignoreCase = true)
    }

    fun handle(sql: String, c: OConnection) {
        c.writeErrMessage(ErrorCode.ER_NOT_SUPPORTED_YET, "暂未支持")
    }
}
