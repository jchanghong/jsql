/*
 * Java-based distributed database like Mysql
 */

package io.jsql.sql.handler.data_define

import io.jsql.sql.OConnection
import io.jsql.sql.handler.SqlStatementHander
import org.springframework.stereotype.Component

/**
 * Created by 长宏 on 2017/3/18 0018.
 * ALTER
 * [ALGORITHM = {UNDEFINED | MERGE | TEMPTABLE}]
 * [DEFINER = { user | CURRENT_USER }]
 * [SQL SECURITY { DEFINER | INVOKER }]
 * VIEW view_name [(column_list)]
 * AS select_statement
 * [WITH [CASCADED | LOCAL] CHECK OPTION]
 */
@Component
class AlterView : SqlStatementHander() {

    override fun supportSQLstatement(): Class<out SQLStatement> {
        return SQLAlterViewRenameStatement::class.java
    }

    @Throws(Exception::class)
    override fun handle0(sqlStatement: SQLStatement, c: OConnection): Any? {
        return null
    }

    companion object {
        fun handle(x: SQLAlterViewRenameStatement, connection: OConnection) {

            connection.writeok()

        }
    }
}
