package io.jsql.sql.handler.adminstatement

import com.alibaba.druid.sql.ast.SQLStatement
import com.alibaba.druid.sql.ast.statement.SQLSetStatement
import io.jsql.sql.handler.SqlStatementHander
import io.jsql.sql.response.MShowDatabases
import org.springframework.stereotype.Component

/**
 * Created by 长宏 on 2017/5/3 0003.
 */
@Component
class MSQLSetStatement : SqlStatementHander() {
    override fun supportSQLstatement(): Class<out SQLStatement> {
        return SQLSetStatement::class.java
    }

    @Throws(Exception::class)
    override fun handle(sqlStatement: SQLStatement): Any? {
        return MShowDatabases.getdata()
    }

}
