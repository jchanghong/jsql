/*
 * Java-based distributed database like Mysql
 */
package io.jsql.mysql

import io.jsql.config.ErrorCode
import io.jsql.mysql.mysql.BinaryPacket
import io.jsql.mysql.mysql.ErrorPacket
import io.jsql.mysql.mysql.FieldPacket
import io.jsql.mysql.mysql.ResultSetHeaderPacket

import java.io.FilePermission
import java.io.UnsupportedEncodingException

/**
 * @author jsql
 */
object PacketUtil {
    private val CODE_PAGE_1252 = "Cp1252"

    fun getHeader(fieldCount: Int): ResultSetHeaderPacket {
        val packet = ResultSetHeaderPacket()
        packet.packetId = 1
        packet.fieldCount = fieldCount
        return packet
    }

    fun encode(src: String?, charset: String): ByteArray? {
        if (src == null) {
            return null
        }
        try {
            return src.toByteArray(charset(charset))
        } catch (e: UnsupportedEncodingException) {
            return src.toByteArray()
        }

    }

    fun getField(name: String, orgName: String, type: Int): FieldPacket {
        val packet = FieldPacket()
        packet.charsetIndex = CharsetUtil.getIndex(CODE_PAGE_1252)
        packet.name = encode(name, CODE_PAGE_1252)
        packet.orgName = encode(orgName, CODE_PAGE_1252)
        packet.type = type.toByte().toInt()
        return packet
    }

    fun getField(name: String, type: Int): FieldPacket {
        val packet = FieldPacket()
        packet.charsetIndex = CharsetUtil.getIndex(CODE_PAGE_1252)
        packet.name = encode(name, CODE_PAGE_1252)
        packet.type = type.toByte().toInt()
        return packet
    }

    fun setFieldName(fieldName: FieldPacket, newname: String) {
        fieldName.name = encode(newname, CODE_PAGE_1252)
    }

    val shutdown: ErrorPacket
        get() {
            val error = ErrorPacket()
            error.packetId = 1
            error.errno = ErrorCode.ER_SERVER_SHUTDOWN
            error.message = "The server has been shutdown".toByteArray()
            return error
        }

    fun getField(src: BinaryPacket, fieldName: String): FieldPacket {
        val field = FieldPacket()
        field.read(src)
        field.name = encode(fieldName, CODE_PAGE_1252)
        field.packetLength = field.calcPacketSize()
        return field
    }

}