package io.jsql.sql.handler.data_mannipulation

import com.alibaba.druid.sql.ast.SQLStatement
import com.alibaba.druid.sql.ast.statement.SQLCallStatement
import io.jsql.sql.OConnection
import io.jsql.sql.handler.SqlStatementHander
import org.springframework.stereotype.Component

/**
 * Created by 长宏 on 2017/3/18 0018.
 * mysql> SET @increment = 10;
 * mysql> CALL p(@version, @increment);
 * mysql> SELECT @version, @increment;
 */
@Component
class Mcall : SqlStatementHander() {
    override fun supportSQLstatement(): Class<out SQLStatement> {
        return SQLCallStatement::class.java
    }

    @Throws(Exception::class)
    override fun handle(sqlStatement: SQLStatement): Any? {
        return null
    }

    companion object {
        fun handle(x: SQLCallStatement, connection: OConnection) {
            connection.writeok()

        }
    }
}
