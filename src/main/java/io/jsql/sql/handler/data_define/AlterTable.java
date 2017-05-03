package io.jsql.sql.handler.data_define;

import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableStatement;
import io.jsql.sql.OConnection;
import io.jsql.sql.handler.SqlStatementHander;
import org.springframework.stereotype.Component;

/**
 * Created by 长宏 on 2017/3/18 0018.
 * ALTER TABLE t2 DROP COLUMN c, DROP COLUMN d;
 */
@Component
public class AlterTable extends SqlStatementHander{
    public static void handle(SQLAlterTableStatement x, OConnection connection) {

        connection.writeok();

    }

    @Override
    public Class<? extends SQLStatement> supportSQLstatement() {
        return SQLAlterTableStatement.class;
    }

    @Override
    protected Object handle(SQLStatement sqlStatement) throws Exception {
        return null;
    }
}
