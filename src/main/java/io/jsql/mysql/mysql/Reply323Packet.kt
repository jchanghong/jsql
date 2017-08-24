/*
 * Java-based distributed database like Mysql
 */
package io.jsql.mysql.mysql

import io.jsql.mysql.StreamUtil

import java.io.IOException
import java.io.OutputStream

/**
 * @author jsql
 * *
 * @author changhong
 */
class Reply323Packet : MySQLPacket() {

    var seed: ByteArray? = null

    @Throws(IOException::class)
    fun write(out: OutputStream) {
        StreamUtil.writeUB3(out, calcPacketSize())
        StreamUtil.write(out, packetId)
        if (seed == null) {
            StreamUtil.write(out, 0.toByte())
        } else {
            StreamUtil.writeWithNull(out, seed!!)
        }
    }

    //    @Override
    //    public void write(BackendAIOConnection c) {
    //        ByteBuffer buffer = c.allocate();
    //        BufferUtil.writeUB3(buffer, calcPacketSize());
    //        buffer.put(packetId);
    //        if (seed == null) {
    //            buffer.put((byte) 0);
    //        } else {
    //            BufferUtil.writeWithNull(buffer, seed);
    //        }
    //        c.write(buffer);
    //    }

    override fun read(data: ByteArray) {

    }

    override fun calcPacketSize(): Int {
        return if (seed == null) 1 else seed!!.size + 1
    }

    protected override val packetInfo: String
        get() = "MySQL Auth323 Packet"

}