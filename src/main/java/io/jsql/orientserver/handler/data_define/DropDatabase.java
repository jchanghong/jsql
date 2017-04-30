package io.jsql.orientserver.handler.data_define;

import com.alibaba.druid.sql.ast.statement.SQLDropDatabaseStatement;
import io.jsql.databaseorient.adapter.MDBadapter;
import io.jsql.databaseorient.adapter.MException;
import io.jsql.orientserver.OConnection;

/**
 * Created by 长宏 on 2017/3/18 0018.
 */
public class DropDatabase {
    public static void handle(SQLDropDatabaseStatement x, OConnection connection) {
        try {
            MDBadapter.deletedb(x.getDatabase().toString());
            connection.writeok();
        } catch (MException e) {
            e.printStackTrace();
            connection.writeErrMessage(e.getMessage());

        }


    }
}
