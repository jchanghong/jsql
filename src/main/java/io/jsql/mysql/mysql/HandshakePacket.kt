/*
 * Java-based distributed database like Mysql
 */
package io.jsql.mysql.mysql

import io.jsql.mysql.MBufferUtil
import io.jsql.mysql.MySQLMessage
import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import io.netty.channel.Channel

/**
 * From server to client during initial handshake.
 *
 *
 * <pre>
 * Bytes                        Name
 * -----                        ----
 * 1                            protocol_version
 * n (Null-Terminated String)   server_version
 * 4                            thread_id
 * 8                            scramble_buff
 * 1                            (filler) always 0x00
 * 2                            server_capabilities
 * 1                            server_language
 * 2                            server_status
 * 13                           (filler) always 0x00 ...
 * 13                           rest of scramble_buff (4.1)

 * @see http://forge.mysql.com/wiki/MySQL_Internals_ClientServer_Protocol.Handshake_Initialization_Packet
</pre> *


 * @author jsql
 * *
 * @author changhong
 */
class HandshakePacket : MySQLPacket() {

    var protocolVersion: Byte = 0
    var serverVersion: ByteArray?=null
    var threadId: Long = 0
    var seed: ByteArray?=null
    var serverCapabilities: Int = 0
    var serverCharsetIndex: Byte = 0
    var serverStatus: Int = 0
    var restOfScrambleBuff: ByteArray?=null

    fun read(bin: BinaryPacket) {
        packetLength = bin.packetLength
        packetId = bin.packetId
        val mm = MySQLMessage(bin.data!!)
        protocolVersion = mm.read()
        serverVersion = mm.readBytesWithNull()
        threadId = mm.readUB4()
        seed = mm.readBytesWithNull()
        serverCapabilities = mm.readUB2()
        serverCharsetIndex = mm.read()
        serverStatus = mm.readUB2()
        mm.move(13)
        restOfScrambleBuff = mm.readBytesWithNull()
    }

    override fun read(data: ByteArray) {
        val mm = MySQLMessage(data)
        packetLength = mm.readUB3()
        packetId = mm.read()
        protocolVersion = mm.read()
        serverVersion = mm.readBytesWithNull()
        threadId = mm.readUB4()
        seed = mm.readBytesWithNull()
        serverCapabilities = mm.readUB2()
        serverCharsetIndex = mm.read()
        serverStatus = mm.readUB2()
        mm.move(13)
        restOfScrambleBuff = mm.readBytesWithNull()
    }

    override fun write(c: Channel) {
        val buffer = Unpooled.buffer(100)

        MBufferUtil.writeUB3(buffer, calcPacketSize())
        buffer.writeByte(packetId.toInt())
        buffer.writeByte(protocolVersion.toInt())
        MBufferUtil.writeWithNull(buffer, serverVersion!!)
        MBufferUtil.writeUB4(buffer, threadId)
        MBufferUtil.writeWithNull(buffer, seed!!)
        MBufferUtil.writeUB2(buffer, serverCapabilities)
        buffer.writeByte(serverCharsetIndex.toInt())
        MBufferUtil.writeUB2(buffer, serverStatus)
        buffer.writeBytes(FILLER_13)
        //        buffer.position(buffer.position() + 13);
        MBufferUtil.writeWithNull(buffer, restOfScrambleBuff!!)
        c.writeAndFlush(buffer)
    }

    override fun calcPacketSize(): Int {
        var size = 1
        size += serverVersion!!.size// n
        size += 5// 1+4
        size += seed!!.size// 8
        size += 19// 1+2+1+2+13
        size += restOfScrambleBuff!!.size// 12
        size += 1// 1
        return size
    }

    protected override val packetInfo: String
        get() = "MySQL Handshake Packet"

    companion object {
        private val FILLER_13 = byteArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
    }

}