package io.jsql.sql.handler.adminstatement;

import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlSetPasswordStatement;
import io.jsql.sql.handler.SqlStatementHander;
import org.springframework.stereotype.Component;

/**
 * Created by 长宏 on 2017/5/3 0003.
 */
@Component
public class MMySqlSetPasswordStatement extends SqlStatementHander{
    @Override
    public Class<? extends SQLStatement> supportSQLstatement() {
        return MySqlSetPasswordStatement.class;
    }

    @Override
    protected Object handle(SQLStatement sqlStatement) throws Exception {
        return null;
    }
}
