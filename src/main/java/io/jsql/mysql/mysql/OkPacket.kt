/*
 * Java-based distributed database like Mysql
 */
package io.jsql.mysql.mysql

import io.jsql.mysql.MBufferUtil
import io.jsql.mysql.MySQLMessage
import io.netty.buffer.Unpooled
import io.netty.channel.Channel

/**
 * From server to client in response to command, if no error and no result set.
 *
 *
 * <pre>
 * Bytes                       Name
 * -----                       ----
 * 1                           field_count, always = 0
 * 1-9 (Length Coded Binary)   affected_rows
 * 1-9 (Length Coded Binary)   insert_id
 * 2                           server_status
 * 2                           warning_count
 * n   (until end of packet)   message fix:(Length Coded String)

 * @see http://forge.mysql.com/wiki/MySQL_Internals_ClientServer_Protocol.OK_Packet
</pre> *


 * @author jsql
 * *
 * @author changhong
 */
class OkPacket : MySQLPacket() {

    var fieldCount = FIELD_COUNT
    var affectedRows: Long = 0
    var insertId: Long = 0
    var serverStatus: Int = 0
    var warningCount: Int = 0
    var message: ByteArray? = null

    fun read(bin: BinaryPacket) {
        packetLength = bin.packetLength
        packetId = bin.packetId
        val mm = MySQLMessage(bin.data!!)
        fieldCount = mm.read()
        affectedRows = mm.readLength()
        insertId = mm.readLength()
        serverStatus = mm.readUB2()
        warningCount = mm.readUB2()
        if (mm.hasRemaining()) {
            this.message = mm.readBytesWithLength()
        }
    }

    override fun read(data: ByteArray) {
        val mm = MySQLMessage(data)
        packetLength = mm.readUB3()
        packetId = mm.read()
        fieldCount = mm.read()
        affectedRows = mm.readLength()
        insertId = mm.readLength()
        serverStatus = mm.readUB2()
        warningCount = mm.readUB2()
        if (mm.hasRemaining()) {
            this.message = mm.readBytesWithLength()
        }
    }

    //	public byte[] writeToBytes(FrontendConnection c) {
    //		ByteBuffer buffer = c.allocate();
    //		this.write(buffer, c);
    //		buffer.flip();
    //		byte[] data = new byte[buffer.limit()];
    //		buffer.get(data);
    //		c.recycle(buffer);
    //		return data;
    //	}


    //	private ByteBuffer write(ByteBuffer buffer, FrontendConnection c) {
    //
    //		int size = calcPacketSize();
    //		buffer = c.checkWriteBuffer(buffer, c.getPacketHeaderSize() + size,
    //				true);
    //		BufferUtil.writeUB3(buffer, calcPacketSize());
    //		buffer.put(packetId);
    //		buffer.put(fieldCount);
    //		BufferUtil.writeLength(buffer, affectedRows);
    //		BufferUtil.writeLength(buffer, insertId);
    //		BufferUtil.writeUB2(buffer, serverStatus);
    //		BufferUtil.writeUB2(buffer, warningCount);
    //		if (message != null) {
    //			BufferUtil.writeWithLength(buffer, message);
    //		}
    //
    //		return buffer;
    //
    //	}
    //
    //	public void write(FrontendConnection c) {
    //		ByteBuffer buffer = write(c.allocate(), c);
    //		c.write(buffer);
    //	}

    override fun calcPacketSize(): Int {
        var i = 1
        i += MBufferUtil.getLength(affectedRows)
        i += MBufferUtil.getLength(insertId)
        i += 4
        if (message != null) {
            i += MBufferUtil.getLength(message!!)
        }
        return i
    }

    protected override val packetInfo: String
        get() = "MySQL OK Packet"

    //	 public byte[] writeToBytes() {
    //
    //	   int totalSize = calcPacketSize() + packetHeaderSize;
    //		 ByteBuf1 buffer = Unpooled.buffer(totalSize);
    //		 MBufferUtil.writeUB3(buffer, calcPacketSize());
    //        buffer.writeByte(packetId);
    //        buffer.writeByte(fieldCount);
    //        MBufferUtil.writeLength(buffer, affectedRows);
    //        MBufferUtil.writeLength(buffer, insertId);
    //        MBufferUtil.writeUB2(buffer, serverStatus);
    //        MBufferUtil.writeUB2(buffer, warningCount);
    //        if (message != null) {
    //            MBufferUtil.writeWithLength(buffer, message);
    //        }
    //		 return buffer.array();
    //	 }

    override fun write(c: Channel) {
        val totalSize = calcPacketSize() + MySQLPacket.packetHeaderSize
        val buffer = Unpooled.buffer(totalSize)
        MBufferUtil.writeUB3(buffer, calcPacketSize())
        buffer.writeByte(packetId.toInt())
        buffer.writeByte(fieldCount.toInt())
        MBufferUtil.writeLength(buffer, affectedRows)
        MBufferUtil.writeLength(buffer, insertId)
        MBufferUtil.writeUB2(buffer, serverStatus)
        MBufferUtil.writeUB2(buffer, warningCount)
        if (message != null) {
            MBufferUtil.writeWithLength(buffer, message!!)
        }
        c.writeAndFlush(buffer)
    }

    companion object {
        val FIELD_COUNT: Byte = 0x00
        val OK = byteArrayOf(7, 0, 0, 1, 0, 0, 0, 2, 0, 0, 0)
    }
}