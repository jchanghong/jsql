/*
 * Java-based distributed database like Mysql
 */

package io.jsql.sql.handler.data_define

import com.alibaba.druid.sql.ast.SQLStatement
import com.alibaba.druid.sql.ast.statement.SQLDropDatabaseStatement
import io.jsql.sql.OConnection
import io.jsql.sql.handler.SqlStatementHander
import org.springframework.stereotype.Component

/**
 * Created by 长宏 on 2017/3/18 0018.
 */
@Component
class DropDatabase : SqlStatementHander() {

    override fun supportSQLstatement(): Class<out SQLStatement> {
        return SQLDropDatabaseStatement::class.java
    }

    @Throws(Exception::class)
    override fun handle0(sqlStatement: SQLStatement, c: OConnection): Any? {
        val x = sqlStatement as SQLDropDatabaseStatement
        OConnection.DB_ADMIN!!.deletedbAyn(x.database.toString())
        return null
    }

    companion object {
        fun handle(x: SQLDropDatabaseStatement, connection: OConnection) {
            OConnection.DB_ADMIN!!.deletedbAyn(x.database.toString())
            connection.writeok()
        }
    }
}
