/*
 * Java-based distributed database like Mysql
 */
package io.jsql.sql.response

import com.orientechnologies.orient.core.record.OElement
import com.orientechnologies.orient.core.record.impl.ODocument
import io.jsql.config.Fields
import io.jsql.mysql.PacketUtil
import io.jsql.mysql.mysql.EOFPacket
import io.jsql.mysql.mysql.FieldPacket
import io.jsql.mysql.mysql.MySQLPacket
import io.jsql.mysql.mysql.RowDataPacket
import io.jsql.sql.OConnection
import io.jsql.sql.handler.MyResultSet
import io.jsql.util.StringUtil

/**
 * @author jsql
 */
object SessionIsolation {

    private val FIELD_COUNT = 1
    private val header = PacketUtil.getHeader(FIELD_COUNT)
    private val fields = arrayOfNulls<FieldPacket>(FIELD_COUNT)
    private val eof = EOFPacket()

    init {
        var i = 0
        var packetId: Byte = 0
        header.packetId = ++packetId
        fields[i] = PacketUtil.getField("@@session.tx_isolation", Fields.FIELD_TYPE_STRING)
        fields[i++]!!.packetId = ++packetId
        eof.packetId = ++packetId
    }

    fun response(c: OConnection) {

        var packetId = eof.packetId
        val row = RowDataPacket(FIELD_COUNT)
        row.add(StringUtil.encode("REPEATABLE-READ", c.charset)!!)
        row.packetId = ++packetId
        val lastEof = EOFPacket()
        lastEof.packetId = ++packetId
        c.writeResultSet(header, fields as Array<MySQLPacket>, eof, arrayOf(row), lastEof)
    }

    fun getdata(): Any {
        val element = ODocument()
        element.setProperty("@@session.tx_isolation", "5")
        return MyResultSet(listOf<OElement>(element), listOf("@@session.tx_isolation"))
    }
}