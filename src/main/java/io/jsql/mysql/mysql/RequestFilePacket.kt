/*
 * Java-based distributed database like Mysql
 */
package io.jsql.mysql.mysql

/**
 * load data local infile 向客户端请求发送文件用

 * @author changhong
 */
class RequestFilePacket : MySQLPacket() {
    var command = FIELD_COUNT
    var fileName: ByteArray? = null

    //
    //    @Override
    //    public ByteBuffer write(ByteBuffer buffer, FrontendConnection c, boolean writeSocketIfFull)
    //    {
    //        int size = calcPacketSize();
    //        buffer = c.checkWriteBuffer(buffer, c.getPacketHeaderSize() + size, writeSocketIfFull);
    //        BufferUtil.writeUB3(buffer, size);
    //        buffer.put(packetId);
    //        buffer.put(command);
    //        if (fileName != null)
    //        {
    //
    //            buffer.put(fileName);
    //
    //        }
    //
    //        c.write(buffer);
    //
    //        return buffer;
    //    }

    override fun read(data: ByteArray) {

    }

    override fun calcPacketSize(): Int {
        return if (fileName == null) 1 else 1 + fileName!!.size
    }

    protected override val packetInfo: String
        get() = "MySQL Request File Packet"

    companion object {
        val FIELD_COUNT = 251.toByte()
    }


}