package io.jsql.sql.handler.data_define;

import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.SQLDropIndexStatement;
import io.jsql.sql.OConnection;
import io.jsql.sql.handler.SqlStatementHander;
import org.springframework.stereotype.Component;

/**
 * Created by 长宏 on 2017/3/18 0018.
 */
@Component
public class DropIndex extends SqlStatementHander{
    public static void handle(SQLDropIndexStatement x, OConnection connection) {

        connection.writeok();

    }

    @Override
    public Class<? extends SQLStatement> supportSQLstatement() {
        return SQLDropIndexStatement.class;
    }

    @Override
    protected Object handle(SQLStatement sqlStatement) throws Exception {
        return null;
    }
}
