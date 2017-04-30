package io.jsql.orientserver.handler.data_define;

import com.alibaba.druid.sql.ast.statement.SQLCreateTableStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlCreateTableStatement;
import io.jsql.config.ErrorCode;
import io.jsql.databaseorient.adapter.MDBadapter;
import io.jsql.databaseorient.adapter.MException;
import io.jsql.databaseorient.adapter.MtableAdapter;
import io.jsql.orientserver.OConnection;

/**
 * Created by jiang on 2017/2/26 0026.
 */
public class CreateTable {
    public static void handle(MySqlCreateTableStatement x, OConnection c) {
        if (MDBadapter.currentDB == null) {
            c.writeErrMessage(ErrorCode.ER_NO_DB_ERROR, "no database selected!!");
            return;
        }
        try {
            MtableAdapter.createtable(MDBadapter.currentDB, x);
            c.writeok();
            return;
        } catch (MException e) {
            e.printStackTrace();
            c.writeErrMessage(e.getMessage());
        }

    }

    public static void handle(OConnection connection, SQLCreateTableStatement x) {


    }
}
