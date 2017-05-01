package io.jsql.orientserver.handler.replication_statement;

import io.jsql.config.ErrorCode;
import io.jsql.orientserver.OConnection;

/**
 * Created by dell on 2017/3/27.
 * STOP SLAVE [thread_types]
 * <p>
 * thread_types:
 * [thread_type [, thread_type] ... ]
 * <p>
 * thread_type: IO_THREAD | SQL_THREAD
 * <p>
 * channel_option:
 * FOR CHANNEL channel
 */
public class STOP_SLAVE {
    public static void handle(String sql, OConnection c) {
        c.writeErrMessage(ErrorCode.ER_NOT_SUPPORTED_YET, "暂未支持");
    }
}
