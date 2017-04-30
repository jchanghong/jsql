package io.jsql.orientserver.handler.data_define;

import com.alibaba.druid.sql.ast.statement.SQLTruncateStatement;
import io.jsql.orientserver.OConnection;

/**
 * Created by 长宏 on 2017/3/18 0018.
 */
public class TruncteTable {
    public static void handle(SQLTruncateStatement x, OConnection connection) {

        connection.writeok();
    }
}
