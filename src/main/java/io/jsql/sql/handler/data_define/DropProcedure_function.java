package io.jsql.sql.handler.data_define;

import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.SQLDropFunctionStatement;
import com.alibaba.druid.sql.ast.statement.SQLDropProcedureStatement;
import io.jsql.sql.OConnection;
import io.jsql.sql.handler.SqlStatementHander;
import org.springframework.stereotype.Component;

/**
 * Created by 长宏 on 2017/3/18 0018.
 */
@Component
public class DropProcedure_function extends SqlStatementHander{
    public static void handle(SQLDropFunctionStatement x, OConnection connection) {

        connection.writeok();
    }

    public static void handle(OConnection connection, SQLDropProcedureStatement x) {

        connection.writeok();
    }

    @Override
    public Class<? extends SQLStatement> supportSQLstatement() {
        return SQLDropFunctionStatement.class;
    }

    @Override
    protected Object handle(SQLStatement sqlStatement) throws Exception {
        return null;
    }
}
