package io.jsql.sql.handler.replication_statement;

import io.jsql.config.ErrorCode;
import io.jsql.sql.OConnection;

/**
 * Created by 长宏 on 2017/3/27.
 */
public class RESET_MASTER {
    public static boolean isMe(String sql) {
        String[] strings = sql.split("\\s+");
        return strings.length > 2 && strings[0].equalsIgnoreCase("reset") && strings[1].equalsIgnoreCase("master");
    }

    public static void handle(String sql, OConnection c) {
        c.writeErrMessage(ErrorCode.ER_NOT_SUPPORTED_YET, "暂未支持");
    }
}
