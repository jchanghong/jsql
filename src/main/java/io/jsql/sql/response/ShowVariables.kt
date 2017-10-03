/*
 * Java-based distributed database like Mysql
 */

package io.jsql.sql.response

import io.jsql.config.Fields
import io.jsql.mysql.PacketUtil
import io.jsql.mysql.mysql.EOFPacket
import io.jsql.mysql.mysql.FieldPacket
import io.jsql.mysql.mysql.MySQLPacket
import io.jsql.mysql.mysql.RowDataPacket
import io.jsql.sql.OConnection
import io.jsql.util.StringUtil
import java.util.*

/**
 * Created by jiang on 2017/2/26 0026.
 */
object ShowVariables {

    private val FIELD_COUNT = 2
    private val header = PacketUtil.getHeader(FIELD_COUNT)
    private val fields = arrayOfNulls<FieldPacket>(FIELD_COUNT)
    private val eof = EOFPacket()
    private val variables = HashMap<String, String>()

    init {
        var i = 0
        var packetId: Byte = 0
        header.packetId = ++packetId
        fields[i] = PacketUtil.getField("variable", Fields.FIELD_TYPE_VAR_STRING)
        fields[i++]!!.packetId = ++packetId
        fields[i] = PacketUtil.getField("values", Fields.FIELD_TYPE_VAR_STRING)
        fields[i++]!!.packetId = ++packetId
        eof.packetId = ++packetId
    }

    init {
        variables.put("@@character_set_client", "utf8")
        variables.put("@@character_set_connection", "utf8")
        variables.put("@@character_set_results", "utf8")
        variables.put("@@character_set_server", "utf8")
        variables.put("@@init_connect", "")
        variables.put("@@interactive_timeout", "172800")
        variables.put("@@license", "GPL")
        variables.put("@@lower_case_table_names", "1")
        variables.put("@@max_allowed_packet", "16777216")
        variables.put("@@net_buffer_length", "16384")
        variables.put("@@net_write_timeout", "60")
        variables.put("@@query_cache_size", "0")
        variables.put("@@query_cache_type", "OFF")
        variables.put("@@sql_mode", "STRICT_TRANS_TABLES")
        variables.put("@@system_time_zone", "CST")
        variables.put("@@time_zone", "SYSTEM")
        variables.put("@@tx_isolation", "REPEATABLE-READ")
        variables.put("@@wait_timeout", "172800")
        variables.put("@@session.auto_increment_increment", "1")

        variables.put("character_set_client", "utf8")
        variables.put("character_set_connection", "utf8")
        variables.put("character_set_results", "utf8")
        variables.put("character_set_server", "utf8")
        variables.put("init_connect", "")
        variables.put("interactive_timeout", "172800")
        variables.put("license", "GPL")
        variables.put("lower_case_table_names", "1")
        variables.put("max_allowed_packet", "16777216")
        variables.put("net_buffer_length", "16384")
        variables.put("net_write_timeout", "60")
        variables.put("query_cache_size", "0")
        variables.put("query_cache_type", "OFF")
        variables.put("sql_mode", "STRICT_TRANS_TABLES")
        variables.put("system_time_zone", "CST")
        variables.put("time_zone", "SYSTEM")
        variables.put("tx_isolation", "REPEATABLE-READ")
        variables.put("wait_timeout", "172800")
        variables.put("auto_increment_increment", "1")
    }

    fun response(c: OConnection) {
        // write rows
        var packetId = eof.packetId

        val rows = ArrayList<MySQLPacket>()
        for ((key, value) in variables) {
            val row = RowDataPacket(FIELD_COUNT)
            row.add(StringUtil.encode(key, c.charset)!!)
            row.add(StringUtil.encode(value, c.charset)!!)
            row.packetId = ++packetId
            rows.add(row)
        }
        // write last eof
        val lastEof = EOFPacket()
        lastEof.packetId = ++packetId
        c.writeResultSet(header, fields as Array<MySQLPacket>, eof, rows.toTypedArray(), lastEof)
    }


    fun getdata(): Any? {
        return null
    }
}
