package io.jsql.sql.handler.data_define;

import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.SQLAlterDatabaseStatement;
import io.jsql.sql.OConnection;
import io.jsql.sql.handler.SqlStatementHander;
import org.springframework.stereotype.Component;

/**
 * Created by 长宏 on 2017/3/18 0018.
 * ALTER {DATABASE | SCHEMA} [db_name]
 * alter_specification ...
 * ALTER {DATABASE | SCHEMA} db_name
 * UPGRADE DATA DIRECTORY NAME
 * <p>
 * alter_specification:
 * [DEFAULT] CHARACTER SET [=] charset_name
 * | [DEFAULT] COLLATE [=] collation_name
 * <p>
 * --ALTER DATABASE CUSTOM strictSQL=false
 */
@Component
public class AlterDatabase extends SqlStatementHander{
    public static void handle(SQLAlterDatabaseStatement x, OConnection connection) {

        connection.writeok();
    }

    @Override
    public Class<? extends SQLStatement> supportSQLstatement() {
        return SQLAlterDatabaseStatement.class;
    }

    @Override
    protected Object handle(SQLStatement sqlStatement) throws Exception {
        return null;
    }
}
