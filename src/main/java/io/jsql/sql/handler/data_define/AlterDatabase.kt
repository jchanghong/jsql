/*
 * Java-based distributed database like Mysql
 */

package io.jsql.sql.handler.data_define

import io.jsql.sql.OConnection
import io.jsql.sql.handler.SqlStatementHander
import org.springframework.stereotype.Component

/**
 * Created by 长宏 on 2017/3/18 0018.
 * ALTER {DATABASE | SCHEMA} [db_name]
 * alter_specification ...
 * ALTER {DATABASE | SCHEMA} db_name
 * UPGRADE DATA DIRECTORY NAME
 *
 *
 * alter_specification:
 * [DEFAULT] CHARACTER SET [=] charset_name
 * | [DEFAULT] COLLATE [=] collation_name
 *
 *
 * --ALTER DATABASE CUSTOM strictSQL=false
 */
@Component
class AlterDatabase : SqlStatementHander() {

    override fun supportSQLstatement(): Class<out SQLStatement> {
        return SQLAlterDatabaseStatement::class.java
    }

    @Throws(Exception::class)
    override fun handle0(sqlStatement: SQLStatement, c: OConnection): Any? {
        return null
    }

    companion object {
        fun handle(x: SQLAlterDatabaseStatement, connection: OConnection) {

            connection.writeok()
        }
    }
}
