package io.jsql.sql.handler.data_define

import com.alibaba.druid.sql.ast.SQLStatement
import com.alibaba.druid.sql.ast.statement.SQLCreateDatabaseStatement
import io.jsql.orientstorage.sqlhander.sqlutil.MSQLutil
import io.jsql.sql.OConnection
import io.jsql.sql.handler.SqlStatementHander
import org.springframework.stereotype.Component

/**
 * Created by 长宏 on 2017/2/25 0025.
 * CREATE {DATABASE | SCHEMA} [IF NOT EXISTS] db_name
 * [create_specification] ...
 *
 *
 * create_specification:
 * [DEFAULT] CHARACTER SET [=] charset_name
 * | [DEFAULT] COLLATE [=] collation_name
 * CREATE DATABASE creates a database with the given name.
 * To use this statement, you need the CREATE privilege for the database.
 * CREATE SCHEMA is a synonym for CREATE DATABASE.
 */
@Component
class CreateDababaseHander : SqlStatementHander() {

    override fun supportSQLstatement(): Class<out SQLStatement> {
        return SQLCreateDatabaseStatement::class.java
    }

    @Throws(Exception::class)
    override fun handle0(sqlStatement: SQLStatement, c: OConnection): Any? {
        val createDatabaseStatement = sqlStatement as SQLCreateDatabaseStatement
        OConnection.DB_ADMIN!!.createdbAsyn(MSQLutil.getdbname(createDatabaseStatement))
        return null
    }

    companion object {
        fun handle(createDatabaseStatement: SQLCreateDatabaseStatement, c: OConnection) {
            OConnection.DB_ADMIN!!.createdbAsyn(MSQLutil.getdbname(createDatabaseStatement))
            c.writeok()
        }
    }
}
