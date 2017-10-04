/*
 * Java-based distributed database like Mysql
 */
package io.jsql.sql.handler.tx_and_lock

import com.alibaba.druid.sql.ast.SQLStatement
import com.alibaba.druid.sql.ast.statement.SQLReleaseSavePointStatement
import com.alibaba.druid.sql.ast.statement.SQLSavePointStatement
import io.jsql.sql.OConnection
import io.jsql.sql.handler.SqlStatementHander

/**
 * @author jsql
 */
object SavepointHandler : SqlStatementHander() {
    override fun handle0(sqlStatement: SQLStatement, c: OConnection): Any? {
        return null
    }

    override fun supportSQLstatement(): Class<out SQLStatement> {
        return SQLSavePointStatement::class.java
    }

    fun handle(stmt: String, c: OConnection) {
        //        c.write(c.writeToBuffer(OkPacket.OK, c.allocate()));
    }

    fun handle(x: SQLSavePointStatement, c: OConnection) {
        //        c.write(c.writeToBuffer(OkPacket.OK, c.allocate()));
    }

    fun handle(x: SQLReleaseSavePointStatement, c: OConnection) {
        //        c.write(c.writeToBuffer(OkPacket.OK, c.allocate()));
    }
}