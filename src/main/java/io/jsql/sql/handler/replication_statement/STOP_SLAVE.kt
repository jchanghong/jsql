/*
 * Java-based distributed database like Mysql
 */

package io.jsql.sql.handler.replication_statement

import io.jsql.config.ErrorCode
import io.jsql.sql.OConnection

/**
 * Created by dell on 2017/3/27.
 * STOP SLAVE [thread_types]
 *
 *
 * thread_types:
 * [thread_type [, thread_type] ... ]
 *
 *
 * thread_type: IO_THREAD | SQL_THREAD
 *
 *
 * channel_option:
 * FOR CHANNEL channel
 */
object STOP_SLAVE {
    fun handle(sql: String, c: OConnection) {
        c.writeErrMessage(ErrorCode.ER_NOT_SUPPORTED_YET, "暂未支持")
    }
}
