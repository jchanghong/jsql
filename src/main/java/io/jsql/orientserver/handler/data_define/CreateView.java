package io.jsql.orientserver.handler.data_define;

import com.alibaba.druid.sql.ast.statement.SQLCreateViewStatement;
import io.jsql.orientserver.OConnection;

/**
 * Created by 长宏 on 2017/3/18 0018.
 */
public class CreateView {
    public static void handle(SQLCreateViewStatement x, OConnection connection) {
        connection.writeok();
    }
}
