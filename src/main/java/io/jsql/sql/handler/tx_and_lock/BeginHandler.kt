/*
 * Java-based distributed database like Mysql
 */
package io.jsql.sql.handler.tx_and_lock

import io.jsql.sql.OConnection
import io.netty.buffer.Unpooled

/**
 * @author jsql
 */
object BeginHandler {
    private val AC_OFF = byteArrayOf(7, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0)

    fun handle(stmt: String, c: OConnection) {
        if (c.autocommit) {
            c.autocommit = false
            c.write(Unpooled.wrappedBuffer(AC_OFF))
        } else {
            c.writeok()
        }
    }

}