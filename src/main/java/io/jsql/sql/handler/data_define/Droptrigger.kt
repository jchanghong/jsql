package io.jsql.sql.handler.data_define

import com.alibaba.druid.sql.ast.SQLStatement
import com.alibaba.druid.sql.ast.statement.SQLDropTriggerStatement
import io.jsql.sql.OConnection
import io.jsql.sql.handler.SqlStatementHander
import org.springframework.stereotype.Component

/**
 * Created by 长宏 on 2017/3/18 0018.
 */
@Component
class Droptrigger : SqlStatementHander() {

    override fun supportSQLstatement(): Class<out SQLStatement> {
        return SQLDropTriggerStatement::class.java
    }

    @Throws(Exception::class)
    override fun handle(sqlStatement: SQLStatement): Any? {
        return null
    }

    companion object {
        fun handle(x: SQLDropTriggerStatement, connection: OConnection) {

            connection.writeok()
        }
    }
}
