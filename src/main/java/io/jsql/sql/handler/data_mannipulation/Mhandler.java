package io.jsql.sql.handler.data_mannipulation;

import io.jsql.sql.OConnection;

/**
 * Created by 长宏 on 2017/3/18 0018.
 * HANDLER tbl_name READ `PRIMARY` ...
 */
public class Mhandler {
    public static boolean isme(String sql) {
        String sqll = sql.toUpperCase().trim();
        String list[] = sqll.split("\\s+");
        return list.length > 0 && list[0].equals("HANDLER");
    }

    public static void handle(String sql, OConnection c) {
        c.writeok();
    }
}
