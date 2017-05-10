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
 * EOF_Packet
 * If CLIENT_PROTOCOL_41 is enabled, the EOF packet contains a warning count and status flags.

 * Note
 * In the MySQL client/server protocol, the EOF_Packet and OK_Packet packets serve the same purpose, to mark the end of a query execution result. Due to changes in MySQL 5.7 in the OK_Packet packets (such as session state tracking), and to avoid repeating the changes in the EOF_Packet packet, the OK_Packet is deprecated as of MySQL 5.7.5.
 * Warning
 * The EOF_Packet packet may appear in places where a Protocol::LengthEncodedInteger may appear. You must check whether the packet length is less than 9 to make sure that it is a EOF_Packet packet.
 * The Payload of an EOF Packet
 * Type	Name	Description
 * int<1>	header	0xFE EOF packet header
 * if capabilities & CLIENT_PROTOCOL_41 {
 * int<2>	warnings	number of warnings
 * int<2>	status_flags	SERVER_STATUS_flags_enum
 * From Server To Client, at the end of a series of Field Packets, and at the
 * end of a series of Data Packets.With prepared statements, EOF Packet can also
 * end parameter information, which we'll describe later.
 *
 *
 * <pre>
 * Bytes                 Name
 * -----                 ----
 * 1                     field_count, always = 0xfe
 * 2                     warning_count
 * 2                     Status Flags

 * @see http://forge.mysql.com/wiki/MySQL_Internals_ClientServer_Protocol.EOF_Packet
</pre> *


 * @author jsql
 * *
 * @author changhong
 */
class EOFPacket : MySQLPacket() {

    var fieldCount = FIELD_COUNT
    var warningCount: Int = 0
    var status = 2

    override fun read(data: ByteArray) {
        val mm = MySQLMessage(data)
        packetLength = mm.readUB3()
        packetId = mm.read()
        fieldCount = mm.read()
        warningCount = mm.readUB2()
        status = mm.readUB2()
    }

    override fun write(channel: Channel) {
        val size = calcPacketSize()
        val buffer = Unpooled.buffer(50)
        //        buffer = c.checkWriteBuffer(buffer, c.getPacketHeaderSize() + size,writeSocketIfFull);
        MBufferUtil.writeUB3(buffer, size)
        buffer.writeByte(packetId.toInt())
        buffer.writeByte(fieldCount.toInt())
        MBufferUtil.writeUB2(buffer, warningCount)
        MBufferUtil.writeUB2(buffer, status)
        channel.writeAndFlush(buffer)
        //        return buffer;
    }

    override fun write(buffer: ByteBuf) {
        val size = calcPacketSize()
        //        buffer = c.checkWriteBuffer(buffer, c.getPacketHeaderSize() + size,writeSocketIfFull);
        MBufferUtil.writeUB3(buffer, size)
        buffer.writeByte(packetId.toInt())
        buffer.writeByte(fieldCount.toInt())
        MBufferUtil.writeUB2(buffer, warningCount)
        MBufferUtil.writeUB2(buffer, status)
    }

    override fun calcPacketSize(): Int {
        return 5// 1+2+2;
    }

    protected override val packetInfo: String
        get() = "MySQL EOF Packet"

    companion object {
        val FIELD_COUNT = 0xfe.toByte()
    }

    //	public void write(BufferArray bufferArray) {
    //		int size = calcPacketSize();
    //		ByteBuffer buffer = bufferArray.checkWriteBuffer(packetHeaderSize
    //				+ size);
    //		BufferUtil.writeUB3(buffer, size);
    //		buffer.put(packetId);
    //		buffer.put(fieldCount);
    //		BufferUtil.writeUB2(buffer, warningCount);
    //		BufferUtil.writeUB2(buffer, status);
    //	}

}