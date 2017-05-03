package io.jsql.sql.handler.data_define;

import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.SQLDropTableStatement;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import io.jsql.config.ErrorCode;
import io.jsql.sql.OConnection;
import io.jsql.sql.handler.SqlStatementHander;
import io.jsql.storage.DB;
import io.jsql.storage.StorageException;
import org.springframework.stereotype.Component;

/**
 * Created by 长宏 on 2017/3/18 0018.
 */
@Component
public class DropTable extends SqlStatementHander{
    public static void handle(SQLDropTableStatement x, OConnection connection) {
        if (connection.schema == null) {
            connection.writeErrMessage(ErrorCode.ER_NO_DB_ERROR, "没有选择数据库");
        }
        x.getTableSources().forEach(table -> {
            try {
                OConnection.TABLE_ADMIN.droptableSyn(connection.schema, table.toString());
            } catch (StorageException e) {
                e.printStackTrace();
                connection.writeErrMessage(e.getMessage());
            }
        });
        connection.writeok();
    }

    @Override
    public Class<? extends SQLStatement> supportSQLstatement() {
        return SQLDropTableStatement.class;
    }

    @Override
    protected Object handle(SQLStatement sqlStatement) throws Exception {
        if (c.schema == null) {
            return "没有选择数据库";
        }
        SQLDropTableStatement x = (SQLDropTableStatement) sqlStatement;
        for (SQLExprTableSource sqlExprTableSource : x.getTableSources()) {
            OConnection.TABLE_ADMIN.droptableSyn(c.schema, sqlExprTableSource.toString());
        }
        return null;
    }
}
