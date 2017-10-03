/*
 * Java-based distributed database like Mysql
 */
package io.jsql.mysql.mysql

import io.jsql.mysql.BufferUtil
import io.jsql.mysql.MBufferUtil
import io.jsql.mysql.MySQLMessage
import io.netty.buffer.Unpooled
import io.netty.channel.Channel
import java.nio.ByteBuffer

/**
 * From server to client in response to command, if error.
 *
 *
 * <pre>
 * Bytes                       Name
 * -----                       ----
 * 1                           field_count, always = 0xff
 * 2                           errno
 * 1                           (sqlstate marker), always '#'
 * 5                           sqlstate (5 characters)
 * n                           message

 * @see http://forge.mysql.com/wiki/MySQL_Internals_ClientServer_Protocol.Error_Packet
</pre> *


 * @author jsql
 * *
 * @author changhong
 */
class ErrorPacket : MySQLPacket() {
    val mark = SQLSTATE_MARKER
    var fieldCount = FIELD_COUNT
    var errno: Int = 0
    var sqlState = DEFAULT_SQLSTATE
    var message: ByteArray? = null

    fun read(bin: BinaryPacket) {
        packetLength = bin.packetLength
        packetId = bin.packetId
        val mm = MySQLMessage(bin.data!!)
        fieldCount = mm.read()
        errno = mm.readUB2()
        if (mm.hasRemaining() && mm.read(mm.position()) == SQLSTATE_MARKER) {
            mm.read()
            sqlState = mm.readBytes(5)
        }
        message = mm.readBytes()
    }

    override fun read(data: ByteArray) {
        val mm = MySQLMessage(data)
        packetLength = mm.readUB3()
        packetId = mm.read()
        fieldCount = mm.read()
        errno = mm.readUB2()
        if (mm.hasRemaining() && mm.read(mm.position()) == SQLSTATE_MARKER) {
            mm.read()
            sqlState = mm.readBytes(5)
        }
        message = mm.readBytes()
    }

    //	public byte[] writeToBytes(FrontendConnection c) {
    //		ByteBuffer buffer = c.allocate();
    //		buffer = write(buffer, c, false);
    //		buffer.flip();
    //		byte[] data = new byte[buffer.limit()];
    //		buffer.get(data);
    //		c.recycle(buffer);
    //		return data;
    //	}
    fun writeToBytes(): ByteArray {
        val buffer = ByteBuffer.allocate(calcPacketSize() + 4)
        val size = calcPacketSize()
        BufferUtil.writeUB3(buffer, size)
        buffer.put(packetId)
        buffer.put(fieldCount)
        BufferUtil.writeUB2(buffer, errno)
        buffer.put(mark)
        buffer.put(sqlState)
        if (message != null) {
            buffer.put(message!!)
        }
        buffer.flip()
        val data = ByteArray(buffer.limit())
        buffer.get(data)

        return data
    }

    fun writeToBytes2(): ByteArray {
        val buffer = Unpooled.buffer(calcPacketSize() + 4)
        val size = calcPacketSize()
        MBufferUtil.writeUB3(buffer, size)
        buffer.writeByte(packetId.toInt())
        buffer.writeByte(fieldCount.toInt())
        MBufferUtil.writeUB2(buffer, errno)
        buffer.writeByte(mark.toInt())
        buffer.writeBytes(sqlState)
        if (message != null) {
            buffer.writeBytes(message)
        }
        val data = ByteArray(buffer.readableBytes())
        buffer.readBytes(data)
        buffer.release()
        return data
    }

    override fun write(channel: Channel) {
        channel.writeAndFlush(Unpooled.wrappedBuffer(writeToBytes2()))
    }


    //	public void write(OConnection c) {
    //		ByteBuffer buffer = c.allocate();
    //		buffer = this.write(buffer, c, true);
    //		c.write(buffer);
    //	}

    override fun calcPacketSize(): Int {
        var size = 9// 1 + 2 + 1 + 5
        if (message != null) {
            size += message!!.size
        }
        return size
    }

    protected override val packetInfo: String
        get() = "MySQL Error Packet"

    companion object {
        val FIELD_COUNT = 0xff.toByte()
        private val SQLSTATE_MARKER = '#'.toByte()
        private val DEFAULT_SQLSTATE = "HY000".toByteArray()
    }

}