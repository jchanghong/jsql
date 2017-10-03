/*
 * Java-based distributed database like Mysql
 */
package io.jsql.sql.handler.tx_and_lock

import io.jsql.sql.OConnection
import io.jsql.sql.handler.SqlStatementHander
import io.netty.buffer.Unpooled

/**
 * @author 完成
 */
object StartHandler : SqlStatementHander() {
    private val AC_OFF = byteArrayOf(7, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0)
    override fun supportSQLstatement(): Class<out SQLStatement> {
        return MySqlStartTransactionStatement::class.java
    }

    override fun handle0(sqlStatement: SQLStatement, c: OConnection): Any? {
        return null
    }

    fun handle(stmt: String, c: OConnection, offset: Int) {

        if (c.autocommit) {
            c.autocommit = false
            c.write(Unpooled.wrappedBuffer(AC_OFF))
        } else {
            //                    c.getSession2().commit();
            c.writeok()
        }

    }

    fun handle(x: MySqlStartTransactionStatement, c: OConnection) {
        c.writeok()
    }
}