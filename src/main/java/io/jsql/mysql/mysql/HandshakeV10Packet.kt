/*
 * Java-based distributed database like Mysql
 */
package io.jsql.mysql.mysql

import io.jsql.config.Capabilities
import io.jsql.mysql.MBufferUtil
import io.netty.buffer.Unpooled
import io.netty.channel.Channel

/**
 * Connection Phase
 * The Connection Phase performs these tasks:

 * exchange the capabilities of client and server
 * setup SSL communication channel if requested
 * authenticate the client against the server
 * It starts with the client connect()ing to the server which may send a ERR packet and finish the handshake or
 * send a Initial Handshake Packet which the client answers with a Handshake Response Packet. At this stage
 * client can request SSL connection, in which case an SSL communication channel
 * is established before client sends its authentication response.

 * Note
 * In case the server sent a ERR packet as first packet it will happen before the
 * client and server negotiated any capabilities. Therefore the ERR packet will not contain the SQL-state.

 * From jsql server to client during initial handshake.
 *
 *
 * <pre>
 * Bytes                        Name
 * -----                        ----
 * 1                            protocol_version (always 0x0a)
 * n (string[NULL])             server_version
 * 4                            thread_id
 * 8 (string[8])                auth-plugin-data-part-1
 * 1                            (filler) always 0x00
 * 2                            capability flags (lower 2 bytes)
 * if more data in the packet:
 * 1                            character set
 * 2                            status flags
 * 2                            capability flags (upper 2 bytes)
 * if capabilities & CLIENT_PLUGIN_AUTH {
 * 1                            length of auth-plugin-data
 * } else {
 * 1                            0x00
 * }
 * 10 (string[10])              reserved (all 0x00)
 * if capabilities & CLIENT_SECURE_CONNECTION {
 * string[ln]   auth-plugin-data-part-2 ($len=MAX(13, length of auth-plugin-data - 8))
 * }
 * if capabilities & CLIENT_PLUGIN_AUTH {
 * string[11]    auth-plugin name
 * }

 * @see http://dev.mysql.com/doc/internals/en/connection-phase-packets.html.Protocol::HandshakeV10
</pre> *


 * @author CrazyPig
 * *
 * @author changhong
 * *
 * @since 2016-11-13
 */
class HandshakeV10Packet : MySQLPacket() {
    val authPluginName = DEFAULT_AUTH_PLUGIN_NAME
    var protocolVersion: Byte = 0
    var serverVersion: ByteArray? = null
    var threadId: Long = 0
    var seed: ByteArray? = null // auth-plugin-data-part-1
    var serverCapabilities: Int = 0
    var serverCharsetIndex: Byte = 0
    var serverStatus: Int = 0
    var restOfScrambleBuff: ByteArray? = null // auth-plugin-data-part-2

    override fun write(c: Channel) {

        val buffer = Unpooled.buffer(100)
        MBufferUtil.writeUB3(buffer, calcPacketSize())
        buffer.writeByte(packetId.toInt())
        buffer.writeByte(protocolVersion.toInt())
        MBufferUtil.writeWithNull(buffer, serverVersion!!)
        MBufferUtil.writeUB4(buffer, threadId)
        buffer.writeBytes(seed)
        buffer.writeByte(0.toByte().toInt()) // [00] filler
        MBufferUtil.writeUB2(buffer, serverCapabilities) // capability flags (lower 2 bytes)
        buffer.writeByte(serverCharsetIndex.toInt())
        MBufferUtil.writeUB2(buffer, serverStatus)
        MBufferUtil.writeUB2(buffer, serverCapabilities shr 16) // capability flags (upper 2 bytes)
        if (serverCapabilities and Capabilities.CLIENT_PLUGIN_AUTH != 0) {
            if (restOfScrambleBuff!!.size <= 13) {
                buffer.writeByte((seed!!.size + 13).toByte().toInt())
            } else {
                buffer.writeByte((seed!!.size + restOfScrambleBuff!!.size).toByte().toInt())
            }
        } else {
            buffer.writeByte(0.toByte().toInt())
        }
        buffer.writeBytes(FILLER_10)
        if (serverCapabilities and Capabilities.CLIENT_SECURE_CONNECTION != 0) {
            buffer.writeBytes(restOfScrambleBuff)
            // restOfScrambleBuff.length always to be 12
            if (restOfScrambleBuff!!.size < 13) {
                for (i in 13 - restOfScrambleBuff!!.size downTo 1) {
                    buffer.writeByte(0.toByte().toInt())
                }
            }
        }
        if (serverCapabilities and Capabilities.CLIENT_PLUGIN_AUTH != 0) {
            MBufferUtil.writeWithNull(buffer, authPluginName)
        }
        c.writeAndFlush(buffer)
    }

    override fun read(data: ByteArray) {
        throw NullPointerException()
    }

    override fun calcPacketSize(): Int {
        var size = 1 // protocol version
        size += serverVersion!!.size + 1 // server version
        size += 4 // connection id
        size += seed!!.size
        size += 1 // [00] filler
        size += 2 // capability flags (lower 2 bytes)
        size += 1 // character set
        size += 2 // status flags
        size += 2 // capability flags (upper 2 bytes)
        size += 1
        size += 10 // reserved (all [00])
        if (serverCapabilities and Capabilities.CLIENT_SECURE_CONNECTION != 0) {
            // restOfScrambleBuff.length always to be 12
            if (restOfScrambleBuff!!.size <= 13) {
                size += 13
            } else {
                size += restOfScrambleBuff!!.size
            }
        }
        if (serverCapabilities and Capabilities.CLIENT_PLUGIN_AUTH != 0) {
            size += authPluginName.size + 1 // auth-plugin name
        }
        return size
    }

    protected override val packetInfo: String
        get() = "MySQL HandshakeV10 Packet"

    companion object {
        private val FILLER_10 = byteArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
        private val DEFAULT_AUTH_PLUGIN_NAME = "mysql_native_password".toByteArray()
    }

}