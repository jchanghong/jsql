package io.jsql.sql.handler.data_define;

import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.SQLDropDatabaseStatement;
import io.jsql.sql.OConnection;
import io.jsql.sql.handler.SqlStatementHander;
import org.springframework.stereotype.Component;

/**
 * Created by 长宏 on 2017/3/18 0018.
 */
@Component
public class DropDatabase extends SqlStatementHander{
    public static void handle(SQLDropDatabaseStatement x, OConnection connection) {
            OConnection.DB_ADMIN.deletedbAyn(x.getDatabase().toString());
            connection.writeok();
    }

    @Override
    public Class<? extends SQLStatement> supportSQLstatement() {
        return SQLDropDatabaseStatement.class;
    }

    @Override
    protected Object handle(SQLStatement sqlStatement) throws Exception {
        SQLDropDatabaseStatement x = (SQLDropDatabaseStatement) sqlStatement;
        OConnection.DB_ADMIN.deletedbAyn(x.getDatabase().toString());
        return null;
    }
}
