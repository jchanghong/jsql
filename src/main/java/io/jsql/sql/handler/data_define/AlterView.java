package io.jsql.sql.handler.data_define;

import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.expr.SQLHexExpr;
import com.alibaba.druid.sql.ast.statement.SQLAlterViewRenameStatement;
import io.jsql.sql.OConnection;
import io.jsql.sql.handler.SqlStatementHander;
import org.springframework.stereotype.Component;

/**
 * Created by 长宏 on 2017/3/18 0018.
 * ALTER
 * [ALGORITHM = {UNDEFINED | MERGE | TEMPTABLE}]
 * [DEFINER = { user | CURRENT_USER }]
 * [SQL SECURITY { DEFINER | INVOKER }]
 * VIEW view_name [(column_list)]
 * AS select_statement
 * [WITH [CASCADED | LOCAL] CHECK OPTION]
 */
@Component
public class AlterView extends SqlStatementHander{
    public static void handle(SQLAlterViewRenameStatement x, OConnection connection) {

        connection.writeok();

    }

    @Override
    public Class<? extends SQLStatement> supportSQLstatement() {
        return SQLAlterViewRenameStatement.class;
    }

    @Override
    protected Object handle(SQLStatement sqlStatement) throws Exception {
        return null;
    }
}
