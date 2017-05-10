/*
 * Copyright (c) 2013, OpenCloudDB/MyCAT and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software;Designed and Developed mainly by many Chinese 
 * opensource volunteers. you can redistribute it and/or modify it under the 
 * terms of the GNU General Public License version 2 only, as published by the
 * Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 * 
 * Any questions about this component can be directed to it's project Web address 
 * https://code.google.com/p/opencloudb/.
 *
 */
package io.jsql.mysql.mysql

import io.jsql.mysql.MBufferUtil
import io.jsql.mysql.MySQLMessage
import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import io.netty.channel.Channel

/**
 * From Server To Client, part of Result Set Packets. One for each column in the
 * result set. Thus, if the value of field_columns in the Result Set Header
 * Packet is 3, then the Field Packet occurs 3 times.
 *
 *
 * <pre>
 * Bytes                      Name
 * -----                      ----
 * n (Length Coded String)    catalog
 * n (Length Coded String)    db
 * n (Length Coded String)    table
 * n (Length Coded String)    org_table
 * n (Length Coded String)    name
 * n (Length Coded String)    org_name
 * 1                          (filler)
 * 2                          charsetNumber
 * 4                          length
 * 1                          type
 * 2                          flags
 * 1                          decimals
 * 2                          (filler), always 0x00
 * n (Length Coded Binary)    default

 * @see http://forge.mysql.com/wiki/MySQL_Internals_ClientServer_Protocol.Field_Packet
</pre> *


 * @author jsql
 * *
 * @author changhong
 */
class FieldPacket : MySQLPacket() {

    var catalog: ByteArray? = DEFAULT_CATALOG
    var db: ByteArray? = null
    var table: ByteArray? = null
    var orgTable: ByteArray? = null
    var name: ByteArray? = null
    var orgName: ByteArray? = null
    var charsetIndex: Int = 0
    var length: Long = 0
    var type: Int = 0
    var flags: Int = 0
    var decimals: Byte = 0
    var definition: ByteArray? = null

    /**
     * 把字节数组转变成FieldPacket
     */
    override fun read(data: ByteArray) {
        val mm = MySQLMessage(data)
        this.packetLength = mm.readUB3()
        this.packetId = mm.read()
        readBody(mm)
    }

    /**
     * 把BinaryPacket转变成FieldPacket
     */
    fun read(bin: BinaryPacket) {
        this.packetLength = bin.packetLength
        this.packetId = bin.packetId
        readBody(MySQLMessage(bin.data!!))
    }

    /*public ByteBuffer write(ByteBuffer buffer
             ) {
        int size = calcPacketSize();
//		buffer = c.checkWriteBuffer(buffer, c.getPacketHeaderSize() + size,
//				writeSocketIfFull);
        BufferUtil.writeUB3(buffer, size);
        buffer.put(packetId);
        writeBody(buffer);
        return buffer;
    }*/
    fun write2(buffer: ByteBuf
    ): ByteBuf {
        val size = calcPacketSize()
        //		buffer = c.checkWriteBuffer(buffer, c.getPacketHeaderSize() + size,
        //				writeSocketIfFull);
        MBufferUtil.writeUB3(buffer, size)
        buffer.writeByte(packetId.toInt())
        writeBody(buffer)
        return buffer
    }

    override fun write(c: Channel) {
        var buf = Unpooled.buffer(512)
        buf = write2(buf)
        c.writeAndFlush(buf)

    }

    override fun write(buffer: ByteBuf) {
        val size = calcPacketSize()
        //		buffer = c.checkWriteBuffer(buffer, c.getPacketHeaderSize() + size,
        //				writeSocketIfFull);
        MBufferUtil.writeUB3(buffer, size)
        buffer.writeByte(packetId.toInt())
        writeBody(buffer)
    }

    override fun calcPacketSize(): Int {
        var size = if (catalog == null) 1 else MBufferUtil.getLength(catalog!!)
        size += if (db == null) 1 else MBufferUtil.getLength(db!!)
        size += if (table == null) 1 else MBufferUtil.getLength(table!!)
        size += if (orgTable == null) 1 else MBufferUtil.getLength(orgTable!!)
        size += if (name == null) 1 else MBufferUtil.getLength(name!!)
        size += if (orgName == null) 1 else MBufferUtil.getLength(orgName!!)
        size += 13// 1+2+4+1+2+1+2
        if (definition != null) {
            size += MBufferUtil.getLength(definition!!)
        }
        return size
    }

    protected override val packetInfo: String
        get() = "MySQL Field Packet"

    private fun readBody(mm: MySQLMessage) {
        this.catalog = mm.readBytesWithLength()
        this.db = mm.readBytesWithLength()
        this.table = mm.readBytesWithLength()
        this.orgTable = mm.readBytesWithLength()
        this.name = mm.readBytesWithLength()
        this.orgName = mm.readBytesWithLength()
        mm.move(1)
        this.charsetIndex = mm.readUB2()
        this.length = mm.readUB4()
        this.type = mm.read().toInt() and 0xff
        this.flags = mm.readUB2()
        this.decimals = mm.read()
        mm.move(FILLER.size)
        if (mm.hasRemaining()) {
            this.definition = mm.readBytesWithLength()
        }
    }

    private fun writeBody(buffer: ByteBuf) {
        val nullVal: Byte = 0
        MBufferUtil.writeWithLength(buffer, catalog, nullVal)
        MBufferUtil.writeWithLength(buffer, db, nullVal)
        MBufferUtil.writeWithLength(buffer, table, nullVal)
        MBufferUtil.writeWithLength(buffer, orgTable, nullVal)
        MBufferUtil.writeWithLength(buffer, name, nullVal)
        MBufferUtil.writeWithLength(buffer, orgName, nullVal)
        buffer.writeByte(0x0C.toByte().toInt())
        MBufferUtil.writeUB2(buffer, charsetIndex)
        MBufferUtil.writeUB4(buffer, length)
        buffer.writeByte((type and 0xff).toByte().toInt())
        MBufferUtil.writeUB2(buffer, flags)
        buffer.writeByte(decimals.toInt())
        buffer.writeByte(0x00.toByte().toInt())
        buffer.writeByte(0x00.toByte().toInt())
        if (definition != null) {
            MBufferUtil.writeWithLength(buffer, definition!!)
        }
    }

    companion object {
        private val DEFAULT_CATALOG = "def".toByteArray()
        private val FILLER = ByteArray(2)
    }

    //	public  void write(BufferArray bufferArray) {
    //		int size = calcPacketSize();
    //		ByteBuffer buffer = bufferArray.checkWriteBuffer(packetHeaderSize + size);
    //		BufferUtil.writeUB3(buffer, size);
    //		buffer.put(packetId);
    //		writeBody(buffer);
    //	}

}