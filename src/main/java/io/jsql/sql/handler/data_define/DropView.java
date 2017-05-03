package io.jsql.sql.handler.data_define;

import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.SQLDropViewStatement;
import io.jsql.sql.OConnection;
import io.jsql.sql.handler.SqlStatementHander;
import org.springframework.stereotype.Component;

/**
 * Created by 长宏 on 2017/3/18 0018.
 */
@Component
public class DropView extends SqlStatementHander {
    public static void handle(SQLDropViewStatement x, OConnection connection) {

        connection.writeok();

    }

    @Override
    public Class<? extends SQLStatement> supportSQLstatement() {
        return SQLDropViewStatement.class;
    }

    @Override
    protected Object handle(SQLStatement sqlStatement) throws Exception {
        return null;
    }
}
