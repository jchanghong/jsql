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
 * @author changhong
 * *         这个类用来简化常用的功能，
 * *         select 只还回一个列
 */
object MSelect1Response {

    private val FIELD_COUNT = 1
    private val header = PacketUtil.getHeader(FIELD_COUNT)
    private val fields = arrayOfNulls<FieldPacket>(FIELD_COUNT)
    private val eof = EOFPacket()

    init {
        val i = 0
        var packetId: Byte = 0
        header.packetId = ++packetId
        //        fields[i] = PacketUtil.getField("DATABASE", Fields.FIELD_TYPE_VAR_STRING);
        //        fields[i++].packetId = ++packetId;
        packetId++
        eof.packetId = ++packetId
    }

    fun response(c: OConnection, columnname: String, columnvaluess: List<String>) {
        fields[0] = PacketUtil.getField(columnname, Fields.FIELD_TYPE_VAR_STRING)
        fields[0]!!.packetId = 2

        // write rows
        var packetId = eof.packetId

        val rows = arrayOfNulls<MySQLPacket>(columnvaluess.size)
        var index = 0
        for (name in columnvaluess) {
            val row = RowDataPacket(FIELD_COUNT)
            row.add(StringUtil.encode(name, c.charset)!!)
            row.packetId = ++packetId
            //                buffer = row.write(buffer, c, true);
            rows[index++] = row
        }
        // write last eof
        val lastEof = EOFPacket()
        lastEof.packetId = ++packetId
        c.writeResultSet(header, fields as Array<MySQLPacket>, eof, rows as Array<MySQLPacket>, lastEof)
    }

}