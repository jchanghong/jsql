/*
 * Java-based distributed database like Mysql
 */

package io.jsql.sql.handler.adminstatement

import io.jsql.sql.OConnection
import io.jsql.sql.handler.SqlStatementHander
import io.jsql.sql.response.MShowDatabases
import org.springframework.stereotype.Component

/**
 * Created by 长宏 on 2017/5/3 0003.
 */
@Component
class MMySqlShowDatabasesStatement : SqlStatementHander() {
    override fun supportSQLstatement(): Class<out SQLStatement> {
        return MySqlShowDatabasesStatement::class.java
    }

    @Throws(Exception::class)
    override fun handle0(sqlStatement: SQLStatement, c: OConnection): Any? {
        return MShowDatabases.getdata()
    }

}
