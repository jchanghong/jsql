/*
 * Java-based distributed database like Mysql
 */

package io.jsql.sql.handler.adminstatement

import io.jsql.sql.OConnection
import io.jsql.sql.handler.SqlStatementHander

class MMySqlShowVariantsStatement : SqlStatementHander() {
    override fun supportSQLstatement(): Class<out SQLStatement> {
        return MySqlShowVariantsStatement::class.java
    }

    override fun handle0(sqlStatement: SQLStatement, c: OConnection): Any? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}