package io.jsql.sql.handler.data_mannipulation;

import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.SQLCallStatement;
import io.jsql.sql.OConnection;
import io.jsql.sql.handler.SqlStatementHander;
import org.springframework.stereotype.Component;

/**
 * Created by 长宏 on 2017/3/18 0018.
 * mysql> SET @increment = 10;
 * mysql> CALL p(@version, @increment);
 * mysql> SELECT @version, @increment;
 */
@Component
public class Mcall extends SqlStatementHander{
    public static void handle(SQLCallStatement x, OConnection connection) {
        connection.writeok();

    }
    @Override
    public Class<? extends SQLStatement> supportSQLstatement() {
        return SQLCallStatement.class;
    }

    @Override
    protected Object handle(SQLStatement sqlStatement) throws Exception {
        return null;
    }
}
