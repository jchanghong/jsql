/*
 * Java-based distributed database like Mysql
 */

package io.jsql.sql.handler.data_define

import io.jsql.sql.OConnection
import io.jsql.sql.handler.SqlStatementHander
import org.springframework.stereotype.Component

/**
 * Created by 长宏 on 2017/3/18 0018.
 */
@Component
class CreateView : SqlStatementHander() {

    override fun supportSQLstatement(): Class<out SQLStatement> {
        return SQLCreateViewStatement::class.java
    }

    @Throws(Exception::class)
    override fun handle0(sqlStatement: SQLStatement, c: OConnection): Any? {
        return null
    }

    companion object {
        fun handle(x: SQLCreateViewStatement, connection: OConnection) {
            connection.writeok()
        }
    }
}
