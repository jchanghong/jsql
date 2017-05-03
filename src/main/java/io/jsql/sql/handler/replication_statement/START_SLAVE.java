package io.jsql.sql.handler.replication_statement;

import io.jsql.config.ErrorCode;
import io.jsql.sql.OConnection;

/**
 * Created by dell on 2017/3/27.
 * START SLAVE [thread_types] [until_option] [connection_options] [channel_option]
 */
public class START_SLAVE {
    public static void handle(String sql, OConnection c) {
        c.writeErrMessage(ErrorCode.ER_NOT_SUPPORTED_YET, "暂未支持");
    }
}
