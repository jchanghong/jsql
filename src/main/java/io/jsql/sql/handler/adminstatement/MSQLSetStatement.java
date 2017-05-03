package io.jsql.sql.handler.adminstatement;

import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.SQLSetStatement;
import io.jsql.sql.handler.SqlStatementHander;
import io.jsql.sql.response.MShowDatabases;
import org.springframework.stereotype.Component;

/**
 * Created by 长宏 on 2017/5/3 0003.
 */
@Component
public class MSQLSetStatement extends SqlStatementHander{
    @Override
    public Class<? extends SQLStatement> supportSQLstatement() {
        return SQLSetStatement.class;
    }
    @Override
    protected Object handle(SQLStatement sqlStatement) throws Exception {
        return MShowDatabases.getdata();
    }

}
