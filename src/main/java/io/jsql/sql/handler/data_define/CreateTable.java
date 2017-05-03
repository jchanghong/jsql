package io.jsql.sql.handler.data_define;

import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.SQLCreateTableStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlCreateTableStatement;
import io.jsql.config.ErrorCode;
import io.jsql.sql.OConnection;
import io.jsql.sql.handler.SqlStatementHander;
import io.jsql.storage.DB;
import io.jsql.storage.StorageException;
import org.springframework.stereotype.Component;

/**
 * Created by 长宏 on 2017/2/26 0026.
 */
@Component
public class CreateTable extends SqlStatementHander{
    public static void handle(MySqlCreateTableStatement x, OConnection c) {
        if (c.schema == null) {
            c.writeErrMessage(ErrorCode.ER_NO_DB_ERROR, "no database selected!!");
            return;
        }
        try {
            OConnection.TABLE_ADMIN.createtableSyn(c.schema, x);
            c.writeok();
        } catch (StorageException e) {
            e.printStackTrace();
            c.writeErrMessage(e.getMessage());
        }

    }

    public static void handle(OConnection connection, SQLCreateTableStatement x) {


    }

    @Override
    public Class<? extends SQLStatement> supportSQLstatement() {
        return MySqlCreateTableStatement.class;
    }

    @Override
    protected Object handle(SQLStatement sqlStatement) throws Exception {
        if (c.schema == null) {
            return "no database selected!!";
        }
        MySqlCreateTableStatement x = (MySqlCreateTableStatement) sqlStatement;
        OConnection.TABLE_ADMIN.createtableSyn(c.schema, x);
        return null;
    }
}
