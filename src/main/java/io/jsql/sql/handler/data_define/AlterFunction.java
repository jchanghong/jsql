package io.jsql.sql.handler.data_define;

import io.jsql.sql.OConnection;

/**
 * Created by 长宏 on 2017/3/18 0018.
 * ALTER FUNCTION func_name [characteristic ...]
 * <p>
 * characteristic:
 * COMMENT 'string'
 * | LANGUAGE SQL
 * | { CONTAINS SQL | NO SQL | READS SQL DATA | MODIFIES SQL DATA }
 * | SQL SECURITY { DEFINER | INVOKER }
 */
public class AlterFunction {
    public static boolean isme(String sql) {
        String sqll = sql.toUpperCase().trim();
        String list[] = sqll.split("\\s+");
        return list.length > 2 && list[0].equals("ALTER") && list[1].equals("FUNCTION");
    }

    public static void handle(String sql, OConnection c) {
        c.writeok();
    }
}
