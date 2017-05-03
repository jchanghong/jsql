package io.jsql.sql.handler.data_define;

import io.jsql.sql.OConnection;

/**
 * Created by 长宏 on 2017/3/18 0018.
 */
public class DropEVENT {
    public static boolean isdropevent(String sql) {
        String[] strings = sql.split("\\s+");
        return strings.length > 2 && strings[0].equalsIgnoreCase("drop") && strings[1].equalsIgnoreCase("event");
    }

    public static void handle(String sql, OConnection c) {

        c.writeok();

    }
}
