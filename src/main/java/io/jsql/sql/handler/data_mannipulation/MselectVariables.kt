/*
 * Java-based distributed database like Mysql
 */

package io.jsql.sql.handler.data_mannipulation

import com.orientechnologies.orient.core.record.OElement
import io.jsql.config.Fields
import io.jsql.mysql.PacketUtil
import io.jsql.mysql.mysql.EOFPacket
import io.jsql.mysql.mysql.FieldPacket
import io.jsql.mysql.mysql.MySQLPacket
import io.jsql.mysql.mysql.RowDataPacket
import io.jsql.orientstorage.constant.Minformation_schama
import io.jsql.orientstorage.constant.MvariableTable
import io.jsql.sql.OConnection
import io.jsql.storage.StorageException
import io.jsql.util.StringUtil
import java.util.*
import kotlin.streams.toList

/**
 * Created by 长宏 on 2017/3/23 0023.
 */
object MselectVariables {

    private val FIELD_COUNT = 2
    private val header = PacketUtil.getHeader(FIELD_COUNT)
    private val fields = arrayOfNulls<FieldPacket>(FIELD_COUNT)
    private val eof = EOFPacket()

    init {
        var i = 0
        var packetId: Byte = 0
        header.packetId = ++packetId
        fields[i] = PacketUtil.getField("Variable_name", Fields.FIELD_TYPE_VAR_STRING)
        fields[i++]!!.packetId = ++packetId
        fields[i] = PacketUtil.getField("values", Fields.FIELD_TYPE_VAR_STRING)
        fields[i++]!!.packetId = ++packetId
        eof.packetId = ++packetId
    }

    /**
     * Handle.

     * @param c         the c
     * *
     * @param statement the statement 一定是show variables 或者select @@这样的语句
     * *                  然后变成select * from variables
     */
    fun handle(c: OConnection, statement: String) {
        // write rows
        var packetId = eof.packetId
        val datas = ArrayList<OElement>()
        try {
            OConnection.DB_ADMIN!!.query(statement, Minformation_schama.dbname).forEach { a -> datas.add(a) }
        } catch (e: StorageException) {
            e.printStackTrace()
            c.writeErrMessage(e.message!!)
            return
        }

        val rows = arrayOfNulls<MySQLPacket>(datas.size)
        var index = 0
        for (name in datas) {
            val row = RowDataPacket(FIELD_COUNT)
            row.add(StringUtil.encode(name.getProperty<String>("Variable_name"), c.charset)!!)
            row.add(StringUtil.encode(name.getProperty<String>("value"), c.charset)!!)
            row.packetId = ++packetId
            rows[index++] = row
        }
        // write last eof
        val lastEof = EOFPacket()
        lastEof.packetId = ++packetId
        c.writeResultSet(header, fields as Array<MySQLPacket>, eof, rows as Array<MySQLPacket>, lastEof)
    }

    fun getcolumn(selectStatement: SQLSelectStatement): List<String> {
        val block = selectStatement.select.query as MySqlSelectQueryBlock
        val items = block.selectList
        val list = ArrayList<String>()
        items.forEach { a -> list.add(a.alias) }
        return list
    }

    fun getbs(selectStatement: SQLSelectStatement, column: List<String>): List<OElement>? {
        try {
            val collect = OConnection.DB_ADMIN!!.query("select * from " + MvariableTable.tablename, Minformation_schama.dbname).toList()
            if (collect is List<OElement>) {

                return collect
            }
        } catch (e: StorageException) {
            e.printStackTrace()
            return null
        }
        return null
    }

    private fun getv(datas: List<OElement>, c: String): String? {
        for (d in datas) {
            if (d.getProperty<Any>("Variable_name") == c) {
                return d.getProperty<String>("value")
            }
        }
        return null
    }
}
