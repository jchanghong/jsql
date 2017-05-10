package io.jsql.sql.handler.data_define

import com.alibaba.druid.sql.ast.SQLStatement
import com.alibaba.druid.sql.ast.statement.SQLDropFunctionStatement
import com.alibaba.druid.sql.ast.statement.SQLDropProcedureStatement
import io.jsql.sql.OConnection
import io.jsql.sql.handler.SqlStatementHander
import org.springframework.stereotype.Component

/**
 * Created by 长宏 on 2017/3/18 0018.
 */
@Component
class DropProcedure_function : SqlStatementHander() {

    override fun supportSQLstatement(): Class<out SQLStatement> {
        return SQLDropFunctionStatement::class.java
    }

    @Throws(Exception::class)
    override fun handle(sqlStatement: SQLStatement): Any? {
        return null
    }

    companion object {
        fun handle(x: SQLDropFunctionStatement, connection: OConnection) {

            connection.writeok()
        }

        fun handle(connection: OConnection, x: SQLDropProcedureStatement) {

            connection.writeok()
        }
    }
}
