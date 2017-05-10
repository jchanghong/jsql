package io.jsql.sql.handler.replication_statement

import io.jsql.config.ErrorCode
import io.jsql.sql.OConnection

/**
 * Created by dell on 2017/3/27.
 * RESET SLAVE [ALL] [channel_option]
 * channel_option:
 * FOR CHANNEL channel
 */
object RESET_SLAVE {
    fun handle(sql: String, c: OConnection) {
        c.writeErrMessage(ErrorCode.ER_NOT_SUPPORTED_YET, "暂未支持")
    }
}
