/*
 * Java-based distributed database like Mysql
 */
package io.jsql.mysql.mysql

import io.jsql.mysql.MBufferUtil
import io.jsql.mysql.MySQLMessage

/**
 * From client to server when the client do heartbeat between jsql cluster.
 *
 *
 * <pre>
 * Bytes         Name
 * -----         ----
 * 1             command
 * n             id

 * @author jsql
 * *
 * @author changhong
</pre> */
class HeartbeatPacket : MySQLPacket() {

    var command: Byte = 0
    var id: Long = 0

    override fun read(data: ByteArray) {
        val mm = MySQLMessage(data)
        packetLength = mm.readUB3()
        packetId = mm.read()
        command = mm.read()
        id = mm.readLength()
    }

    //    @Override
    //    public void write(BackendAIOConnection c) {
    //        ByteBuffer buffer = c.allocate();
    //        BufferUtil.writeUB3(buffer, calcPacketSize());
    //        buffer.put(packetId);
    //        buffer.put(command);
    //        BufferUtil.writeLength(buffer, id);
    //        c.write(buffer);
    //    }

    override fun calcPacketSize(): Int {
        return 1 + MBufferUtil.getLength(id)
    }

    protected override val packetInfo: String
        get() = "Mycat Heartbeat Packet"

}