package io.jsql.orientserver.handler.replication_statement;

import io.jsql.config.ErrorCode;
import io.jsql.orientserver.OConnection;

/**
 * Created by dell on 2017/3/27.
 * RESET SLAVE [ALL] [channel_option]
 * channel_option:
 * FOR CHANNEL channel
 */
public class RESET_SLAVE {
    public static void handle(String sql, OConnection c) {
        c.writeErrMessage(ErrorCode.ER_NOT_SUPPORTED_YET, "暂未支持");
    }
}
