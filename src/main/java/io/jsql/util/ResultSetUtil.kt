/*
 * Java-based distributed database like Mysql
 */

package io.jsql.util

import io.jsql.mysql.mysql.FieldPacket
import io.jsql.mysql.mysql.RowDataPacket

import java.sql.ResultSet
import java.sql.ResultSetMetaData
import java.sql.SQLException

/**
 * @author struct
 */
object ResultSetUtil {

    @Throws(SQLException::class)
    fun toFlag(metaData: ResultSetMetaData, column: Int): Int {

        var flags = 0
        if (metaData.isNullable(column) == 1) {
            flags = flags or 1
        }

        if (metaData.isSigned(column)) {
            flags = flags or 16
        }

        if (metaData.isAutoIncrement(column)) {
            flags = flags or 128
        }

        return flags
    }

    @Throws(SQLException::class)
    fun resultSetToFieldPacket(charset: String,
                               fieldPks: MutableList<FieldPacket>, rs: ResultSet,
                               isSpark: Boolean) {
        val metaData = rs.metaData
        val colunmCount = metaData.columnCount
        if (colunmCount > 0) {
            //String values="";
            for (i in 0..colunmCount - 1) {
                val j = i + 1
                val fieldPacket = FieldPacket()
                fieldPacket.orgName = StringUtil.encode(metaData.getColumnName(j), charset)
                fieldPacket.name = StringUtil.encode(metaData.getColumnLabel(j), charset)
                if (!isSpark) {
                    fieldPacket.orgTable = StringUtil.encode(metaData.getTableName(j), charset)
                    fieldPacket.table = StringUtil.encode(metaData.getTableName(j), charset)
                    fieldPacket.db = StringUtil.encode(metaData.getSchemaName(j), charset)
                    fieldPacket.flags = toFlag(metaData, j)
                }
                fieldPacket.length = metaData.getColumnDisplaySize(j).toLong()

                fieldPacket.decimals = metaData.getScale(j).toByte()
                val javaType = MysqlDefs.javaTypeDetect(
                        metaData.getColumnType(j), fieldPacket.decimals.toInt())
                fieldPacket.type = (MysqlDefs.javaTypeMysql(javaType) and 0xff).toByte().toInt()
                fieldPks.add(fieldPacket)
                //values+=metaData.getColumnLabel(j)+"|"+metaData.getColumnName(j)+"  ";
            }
            // System.out.println(values);
        }


    }

    fun parseRowData(row: ByteArray,
                     fieldValues: List<ByteArray>): RowDataPacket {
        val rowDataPkg = RowDataPacket(fieldValues.size)
        rowDataPkg.read(row)
        return rowDataPkg
    }

    fun getColumnValAsString(row: ByteArray,
                             fieldValues: List<ByteArray>, columnIndex: Int): String {
        val rowDataPkg = RowDataPacket(fieldValues.size)
        rowDataPkg.read(row)
        val columnData = rowDataPkg.fieldValues[columnIndex]
        return String(columnData)
    }

    fun getColumnVal(row: ByteArray, fieldValues: List<ByteArray>,
                     columnIndex: Int): ByteArray {
        val rowDataPkg = RowDataPacket(fieldValues.size)
        rowDataPkg.read(row)
        return rowDataPkg.fieldValues[columnIndex]
    }

    fun fromHex(hexString: String): ByteArray {
        val hex = hexString.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val b = ByteArray(hex.size)
        for (i in hex.indices) {
            b[i] = (Integer.parseInt(hex[i], 16) and 0xff).toByte()
        }

        return b
    }

    @Throws(Exception::class)
    @JvmStatic fun main(args: Array<String>) {
        // byte[] byt =
        // fromHex("20 00 00 02 03 64 65 66 00 00 00 0A 40 40 73 71 6C 5F 6D 6F 64 65 00 0C 21 00 BA 00 00 00 FD 01 00 1F 00 00");
        // MysqlPacketBuffer buffer = new MysqlPacketBuffer(byt);
        // /*
        // * ResultSetHeaderPacket packet = new ResultSetHeaderPacket();
        // * packet.init(buffer);
        // */
        // FieldPacket[] fields = new FieldPacket[(int) 1];
        // for (int i = 0; i < 1; i++) {
        // fields[i] = new FieldPacket();
        // fields[i].init(buffer);
        // }
        // System.out.println(1 | 0200);

    }
}
