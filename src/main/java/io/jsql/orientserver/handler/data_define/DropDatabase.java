package io.jsql.orientserver.handler.data_define;

import com.alibaba.druid.sql.ast.statement.SQLDropDatabaseStatement;
import io.jsql.orientserver.OConnection;
import io.jsql.storage.MException;

/**
 * Created by 长宏 on 2017/3/18 0018.
 */
public class DropDatabase {
    public static void handle(SQLDropDatabaseStatement x, OConnection connection) {
            OConnection.DB_ADMIN.deletedbAyn(x.getDatabase().toString());
            connection.writeok();
    }
}
