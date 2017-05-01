package io.jsql.orientserver.handler.data_define;

import com.alibaba.druid.sql.ast.statement.SQLCreateTableStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlCreateTableStatement;
import io.jsql.config.ErrorCode;
import io.jsql.orientserver.OConnection;
import io.jsql.storage.DBAdmin;
import io.jsql.storage.MException;

/**
 * Created by 长宏 on 2017/2/26 0026.
 */
public class CreateTable {
    public static void handle(MySqlCreateTableStatement x, OConnection c) {
        if (DBAdmin.currentDB == null) {
            c.writeErrMessage(ErrorCode.ER_NO_DB_ERROR, "no database selected!!");
            return;
        }
        try {
            OConnection.TABLE_ADMIN.createtableSyn(DBAdmin.currentDB, x);
            c.writeok();
        } catch (MException e) {
            e.printStackTrace();
            c.writeErrMessage(e.getMessage());
        }

    }

    public static void handle(OConnection connection, SQLCreateTableStatement x) {


    }
}
