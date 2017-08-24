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
 * *         select n个列，只有一行value
 */
object MselectNResponse {


    /**
     * Responseselect.

     * @param c      the c
     * *
     * @param colums the colums
     * *
     * @param values the values只有一行
     */
    fun response(c: OConnection, colums: List<String>, values: List<String>) {
        val FIELD_COUNT: Int
        val header: ResultSetHeaderPacket
        val fields: Array<FieldPacket?>
        val eof: EOFPacket
        FIELD_COUNT = colums.size
        header = PacketUtil.getHeader(FIELD_COUNT)
        fields = arrayOfNulls<FieldPacket>(FIELD_COUNT)
        var i = 0
        var packetId: Byte = 0
        header.packetId = ++packetId
        for (string in colums) {
            fields[i] = PacketUtil.getField(string, Fields.FIELD_TYPE_VAR_STRING)
            fields[i++]!!.packetId = ++packetId
        }
        eof = EOFPacket()
        eof.packetId = ++packetId


        val row = RowDataPacket(FIELD_COUNT)
        for (name in values) {
            row.add(StringUtil.encode(name, c.charset)!!)
        }
        row.packetId = ++packetId
        // write last eof
        val lastEof = EOFPacket()
        lastEof.packetId = ++packetId
        c.writeResultSet(header, fields as Array<MySQLPacket>, eof, arrayOf(row), lastEof)
    }


}