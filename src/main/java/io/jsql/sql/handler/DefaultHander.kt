package io.jsql.sql.handler

import com.orientechnologies.orient.core.record.OElement
import io.jsql.config.Fields
import io.jsql.mysql.PacketUtil
import io.jsql.mysql.mysql.*
import io.jsql.sql.OConnection
import io.jsql.util.StringUtil

/**
 * Created by 长宏 on 2017/2/26 0026.
 * 默认的处理
 */
object DefaultHander {
    private val FIELD_COUNT = 1
    private val header = PacketUtil.getHeader(FIELD_COUNT)
    private val fields = arrayOfNulls<FieldPacket>(FIELD_COUNT)
    private val eof = EOFPacket()

    init {
        var i = 0
        var packetId: Byte = 0
        header.packetId = ++packetId
        fields[i] = PacketUtil.getField("DATABASE", Fields.FIELD_TYPE_VAR_STRING)
        fields[i++]!!.packetId = ++packetId
        eof.packetId = ++packetId
    }

    /**
     * Response 1 column.只有一列的时候直接用这个函数

     * @param columnname the columnname
     * *
     * @param data       the data
     * *
     * @param c          the c
     */
    fun response_1_column(columnname: String, data: List<String>, c: OConnection) {
        PacketUtil.setFieldName(fields[0] as FieldPacket, columnname)
        // write rows
        var packetId = eof.packetId
        val rows = arrayOfNulls<MySQLPacket>(data.size)
        var index = 0
        for (name in data) {
            val row = RowDataPacket(FIELD_COUNT)
            row.add(StringUtil.encode(name, c.charset)!!)
            row.packetId = ++packetId
            rows[index++] = row
        }
        // write last eof
        val lastEof = EOFPacket()
        lastEof.packetId = ++packetId
        var fs: Array<MySQLPacket> = fields as Array<MySQLPacket>
        c.writeResultSet(header, fs, eof, rows as Array<MySQLPacket>, lastEof)
    }

    /**
     * Onerror.

     * @param e          the e
     * *
     * @param connection the connection
     */
    fun onerror(e: Exception, connection: OConnection) {

        connection.writeErrMessage(e.message!!)

    }

    /**
     * Onseccess.
     * @param data       the data
     * *
     * @param columns
     * *
     * @param connection the connection
     */
    fun onseccess(datas: List<OElement>, columns: List<String>, connection: OConnection) {
        val FIELD_COUNT: Int
        val header: ResultSetHeaderPacket
        val fields: Array<FieldPacket?>
        val eof: EOFPacket
        FIELD_COUNT = columns.size
        header = PacketUtil.getHeader(FIELD_COUNT)
        fields = arrayOfNulls<FieldPacket>(FIELD_COUNT)
        var i = 0
        var packetId: Byte = 0
        header.packetId = ++packetId
        for (string in columns) {
            fields[i] = PacketUtil.getField(string, Fields.FIELD_TYPE_VAR_STRING)
            fields[i++]!!.packetId = ++packetId
        }
        eof = EOFPacket()
        eof.packetId = ++packetId


        var index = 0
        val rows = arrayOfNulls<MySQLPacket>(datas.size)
        for (name in datas) {
            val row = RowDataPacket(FIELD_COUNT)
            columns.forEach { a -> row.add(StringUtil.encode(if (name.getProperty<Any>(a) == null) "null" else name.getProperty<Any>(a).toString(), connection.charset)!!) }
            row.packetId = ++packetId
            rows[index++] = row
        }
        //        // write last eof
        val lastEof = EOFPacket()
        lastEof.packetId = ++packetId
        connection.writeResultSet(header, fields as Array<MySQLPacket>, eof, rows as Array<MySQLPacket>, lastEof)
    }

    /**
     * Onseccess.

     * @param row        the row
     * *
     * @param connection the connection
     */
    fun onseccess(row: Long, connection: OConnection) {
        val okPacket = OkPacket()
        okPacket.read(OkPacket.OK)
        okPacket.affectedRows = row
        okPacket.write(connection.channelHandlerContext!!.channel())
    }
}
