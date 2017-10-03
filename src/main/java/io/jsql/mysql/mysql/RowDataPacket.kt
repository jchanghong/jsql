/*
 * Java-based distributed database like Mysql
 */
package io.jsql.mysql.mysql

import io.jsql.mysql.MBufferUtil
import io.jsql.mysql.MySQLMessage
import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import io.netty.channel.Channel
import java.util.*

/**
 * From server to client. One packet for each row in the result set.
 *
 *
 * <pre>
 * Bytes                   Name
 * -----                   ----
 * n (Length Coded String) (column value)
 * ...

 * (column value):         The data in the column, as a character string.
 * If a column is defined as non-character, the
 * server converts the value into a character
 * before sending it. Since the value is a Length
 * Coded String, a NULL can be represented with a
 * single byte containing 251(see the description
 * of Length Coded Strings in section "Elements" above).

 * @see http://forge.mysql.com/wiki/MySQL_Internals_ClientServer_Protocol.Row_Data_Packet
</pre> *


 * @author jsql
 * *
 * @author changhong
 */
class RowDataPacket(var fieldCount: Int) : MySQLPacket() {
    val fieldValues: MutableList<ByteArray>
    var value: ByteArray? = null

    init {
        this.fieldValues = ArrayList<ByteArray>(fieldCount)
    }

    fun add(value: ByteArray) {
        //这里应该修改value
        fieldValues.add(value)
    }

    fun addFieldCount(add: Int) {
        //这里应该修改field
        fieldCount = fieldCount + add
    }

    override fun read(data: ByteArray) {
        value = data
        val mm = MySQLMessage(data)
        packetLength = mm.readUB3()
        packetId = mm.read()
        for (i in 0..fieldCount - 1) {
            fieldValues.add(mm.readBytesWithLength()!!)
        }
    }

    override fun write(channel: Channel) {
        val bb = Unpooled.buffer(calcPacketSize() + 4)
        //		bb = c.checkWriteBuffer(bb, c.getPacketHeaderSize(), writeSocketIfFull);
        MBufferUtil.writeUB3(bb, calcPacketSize())
        bb.writeByte(packetId.toInt())
        for (i in 0..fieldCount - 1) {
            val fv = fieldValues[i]
            if (fv == null) {
                //				bb = c.checkWriteBuffer(bb, 1, writeSocketIfFull);
                bb.writeByte(RowDataPacket.NULL_MARK.toInt())
            } else if (fv.size == 0) {
                //                bb = c.checkWriteBuffer(bb, 1, writeSocketIfFull);
                bb.writeByte(RowDataPacket.EMPTY_MARK.toInt())
            } else {
                //				bb = c.checkWriteBuffer(bb, BufferUtil.getLength(fv),
                //						writeSocketIfFull);
                MBufferUtil.writeLength(bb, fv.size.toLong())
                //				bb = c.writeToBuffer(fv, bb);
            }
        }
        channel.writeAndFlush(bb)
    }

    override fun write(bb: ByteBuf) {
        //		bb = c.checkWriteBuffer(bb, c.getPacketHeaderSize(), writeSocketIfFull);
        MBufferUtil.writeUB3(bb, calcPacketSize())
        bb.writeByte(packetId.toInt())
        for (i in 0..fieldCount - 1) {
            val fv = fieldValues[i]
            if (fv == null) {
                //				bb = c.checkWriteBuffer(bb, 1, writeSocketIfFull);
                bb.writeByte(RowDataPacket.NULL_MARK.toInt())
            } else if (fv.size == 0) {
                //                bb = c.checkWriteBuffer(bb, 1, writeSocketIfFull);
                bb.writeByte(RowDataPacket.EMPTY_MARK.toInt())
            } else {
                //				bb = c.checkWriteBuffer(bb, BufferUtil.getLength(fv),
                //						writeSocketIfFull);
                MBufferUtil.writeLength(bb, fv.size.toLong())
                bb.writeBytes(fv)
                //				bb = c.writeToBuffer(fv, bb);
            }
        }
    }

    override fun calcPacketSize(): Int {
        var size = 0
        for (i in 0..fieldCount - 1) {
            val v = fieldValues[i]
            size += if (v == null || v.size == 0) 1 else MBufferUtil.getLength(v)
        }
        return size
    }

    protected override val packetInfo: String
        get() = "MySQL RowData Packet"

    companion object {
        private val NULL_MARK = 251.toByte()
        private val EMPTY_MARK = 0.toByte()
    }

    //	public void write(BufferArray bufferArray) {
    //		int size = calcPacketSize();
    //		ByteBuffer buffer = bufferArray.checkWriteBuffer(packetHeaderSize + size);
    //		BufferUtil.writeUB3(buffer, size);
    //		buffer.put(packetId);
    //		for (int i = 0; i < fieldCount; i++) {
    //			byte[] fv = fieldValues.get(i);
    //			if (fv == null) {
    //				buffer = bufferArray.checkWriteBuffer(1);
    //				buffer.put(RowDataPacket.NULL_MARK);
    //			} else if (fv.length == 0) {
    //				buffer = bufferArray.checkWriteBuffer(1);
    //				buffer.put(RowDataPacket.EMPTY_MARK);
    //			} else {
    //				buffer = bufferArray.checkWriteBuffer(BufferUtil
    //						.getLength(fv.length));
    //				BufferUtil.writeLength(buffer, fv.length);
    //				bufferArray.write(fv);
    //			}
    //		}
    //	}

}