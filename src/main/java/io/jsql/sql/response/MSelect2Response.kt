/*
 * Java-based distributed database like Mysql
 */
package io.jsql.sql.response

import io.jsql.config.Fields
import io.jsql.mysql.PacketUtil
import io.jsql.mysql.mysql.*
import io.jsql.sql.OConnection
import io.jsql.util.StringUtil

/**
 * @author schanghong
 * *         select 返回2个列
 */
object MSelect2Response {

    private val FIELD_COUNT = 2
    private val header = PacketUtil.getHeader(FIELD_COUNT)
    private val fields = arrayOfNulls<FieldPacket>(FIELD_COUNT)
    private val eof = EOFPacket()

    init {
        var i = 0
        var packetId: Byte = 0
        header.packetId = ++packetId
        fields[i] = PacketUtil.getField("DATABASE", Fields.FIELD_TYPE_VAR_STRING)
        fields[i++]!!.packetId = ++packetId
        fields[i] = PacketUtil.getField("DATABASE", Fields.FIELD_TYPE_VAR_STRING)
        fields[i++]!!.packetId = ++packetId
        eof.packetId = ++packetId
    }

    fun response(c: OConnection, column1: String, colu2: String, clist1: List<String>, clist2: List<String>) {
        fields[0] = PacketUtil.getField(column1, Fields.FIELD_TYPE_VAR_STRING)
        fields[1] = PacketUtil.getField(colu2, Fields.FIELD_TYPE_VAR_STRING)

        val rows = arrayOfNulls<MySQLPacket>(clist1.size)
        var index = 0
        // write rows
        var packetId = eof.packetId
        for (i in clist1.indices) {
            val row = RowDataPacket(FIELD_COUNT)
            row.add(StringUtil.encode(clist1[i], c.charset)!!)
            row.add(StringUtil.encode(clist2[i], c.charset)!!)
            row.packetId = ++packetId
            rows[index++] = row
        }
        // write last eof
        val lastEof = EOFPacket()
        lastEof.packetId = ++packetId
        c.writeResultSet(header, fields as Array<MySQLPacket>, eof, rows as Array<MySQLPacket>, lastEof)
    }

}