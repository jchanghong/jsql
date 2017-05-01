package io.jsql.orientserver.handler.data_define;

import com.alibaba.druid.sql.ast.statement.SQLDropTableStatement;
import io.jsql.config.ErrorCode;
import io.jsql.orientserver.OConnection;
import io.jsql.storage.DBAdmin;
import io.jsql.storage.MException;

/**
 * Created by 长宏 on 2017/3/18 0018.
 */
public class DropTable {
    public static void handle(SQLDropTableStatement x, OConnection connection) {
        if (DBAdmin.currentDB == null) {
            connection.writeErrMessage(ErrorCode.ER_NO_DB_ERROR, "没有选择数据库");
        }
        x.getTableSources().forEach(table -> {
            try {
                OConnection.TABLE_ADMIN.droptableSyn(DBAdmin.currentDB, table.toString());
            } catch (MException e) {
                e.printStackTrace();
                connection.writeErrMessage(e.getMessage());
            }
        });
        connection.writeok();
    }
}
