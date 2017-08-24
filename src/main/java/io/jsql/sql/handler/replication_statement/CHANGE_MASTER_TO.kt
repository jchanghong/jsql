/*
 * Java-based distributed database like Mysql
 */

package io.jsql.sql.handler.replication_statement

import io.jsql.config.ErrorCode
import io.jsql.sql.OConnection

/**
 * Created by dell on 2017/3/27.
 * CHANGE MASTER TO option [, option] ... [ channel_option ]
 * eg:
 * CHANGE MASTER TO MASTER_NAME=host1, MASTER_PORT=3002 FOR CHANNEL channel2
 * CHANGE MASTER TO MASTER_PASSWORD='new3cret';
 */
object CHANGE_MASTER_TO {
    internal var options: String? = null

    fun isMe(sql: String): Boolean {
        val strings = sql.split("\\s+".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        if (strings.size > 3 && strings[0].equals("change", ignoreCase = true) && strings[1].equals("master", ignoreCase = true) && strings[2].equals("to", ignoreCase = true)) {
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
