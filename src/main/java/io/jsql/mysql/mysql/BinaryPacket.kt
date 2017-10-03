/*
 * Java-based distributed database like Mysql
 */
package io.jsql.mysql.mysql

import io.jsql.mysql.ByteUtil
import io.jsql.mysql.MBufferUtil
import io.jsql.mysql.StreamUtil
import io.netty.buffer.Unpooled
import io.netty.channel.Channel
import java.io.IOException
import java.io.InputStream

/**
 * @author jsql
 * *
 * @author changhong
 */
class BinaryPacket : MySQLPacket() {

    var data: ByteArray? = null

    @Throws(IOException::class)
    fun read(`in`: InputStream) {
        packetLength = StreamUtil.readUB3(`in`)
        packetId = StreamUtil.read(`in`)
        val ab = ByteArray(packetLength)
        StreamUtil.read(`in`, ab, 0, ab.size)
        data = ab
    }

    override fun write(channel: Channel) {
        val buffer = Unpooled.buffer(50)
        //        buffer = c.checkWriteBuffer(buffer, c.getPacketHeaderSize(),writeSocketIfFull);
        MBufferUtil.writeUB3(buffer, calcPacketSize())
        buffer.writeByte(packetId.toInt())
        //        buffer = c.writeToBuffer(data, buffer);
        buffer.writeBytes(data)
        channel.writeAndFlush(buffer)
        //        return buffer;
    }

    override fun read(data: ByteArray) {
        var data = data

        packetLength = ByteUtil.readUB3(data, 0)
        packetId = data[3]
        val ab = ByteArray(packetLength)
        System.arraycopy(data, 4, ab, 0, packetLength)
        data = ab
    }
    //    @Override
    //    public void write(BackendAIOConnection c) {
    //        ByteBuffer buffer = c.allocate();
    //        buffer=  c.checkWriteBuffer(buffer,c.getPacketHeaderSize()+calcPacketSize(),false);
    //        BufferUtil.writeUB3(buffer, calcPacketSize());
    //        buffer.put(packetId);
    //        buffer.put(data);
    //        c.write(buffer);
    //    }

    override fun calcPacketSize(): Int {
        return if (data == null) 0 else data!!.size
    }

    protected override val packetInfo: String
        get() = "MySQL Binary Packet"

    companion object {
        val OK: Byte = 1
        val ERROR: Byte = 2
        val HEADER: Byte = 3
        val FIELD: Byte = 4
        val FIELD_EOF: Byte = 5
        val ROW: Byte = 6
        val PACKET_EOF: Byte = 7
    }

}