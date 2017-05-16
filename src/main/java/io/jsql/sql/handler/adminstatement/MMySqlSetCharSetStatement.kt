package io.jsql.sql.handler.adminstatement

import com.alibaba.druid.sql.ast.SQLStatement
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlSetCharSetStatement
import io.jsql.sql.OConnection
import io.jsql.sql.handler.SqlStatementHander
import org.springframework.stereotype.Component

/**
 * Created by 长宏 on 2017/5/3 0003.
 */
@Component
class MMySqlSetCharSetStatement : SqlStatementHander() {
    override fun supportSQLstatement(): Class<out SQLStatement> {
        return MySqlSetCharSetStatement::class.java
    }

    @Throws(Exception::class)
    override fun handle0(sqlStatement: SQLStatement, c: OConnection): Any? {
        return null
    }
}
