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
package io.jsql.mysql.mysql;

import io.jsql.mysql.MBufferUtil;
import io.jsql.mysql.MySQLMessage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;

/**
 * From server to client after command, if no error and result set -- that is,
 * if the command was a query which returned a result set. The Result Set Header
 * Packet is the first of several, possibly many, packets that the server sends
 * for result sets. The order of packets for a result set is:
 * <p>
 * <pre>
 * (Result Set Header Packet)   the number of columns
 * (Field Packets)              column descriptors
 * (EOF Packet)                 marker: end of Field Packets
 * (Row Data Packets)           row contents
 * (EOF Packet)                 marker: end of Data Packets
 *
 * Bytes                        Name
 * -----                        ----
 * 1-9   (Length-Coded-Binary)  field_count
 * 1-9   (Length-Coded-Binary)  extra
 *
 * @see http://forge.mysql.com/wiki/MySQL_Internals_ClientServer_Protocol#Result_Set_Header_Packet
 * </pre>
 *
 * @author jsql
 * @author changhong
 */
public class ResultSetHeaderPacket extends MySQLPacket {

    public int fieldCount;
    public long extra;

    @Override
    public void read(byte[] data) {
        MySQLMessage mm = new MySQLMessage(data);
        this.packetLength = mm.readUB3();
        this.packetId = mm.read();
        this.fieldCount = (int) mm.readLength();
        if (mm.hasRemaining()) {
            this.extra = mm.readLength();
        }
    }

    @Override
    public void write(Channel channel) {
        int size = calcPacketSize();
        ByteBuf buffer = Unpooled.buffer(size);
//        buffer = c.checkWriteBuffer(buffer, c.getPacketHeaderSize() + size,writeSocketIfFull);
        MBufferUtil.writeUB3(buffer, size);
        buffer.writeByte(packetId);
        MBufferUtil.writeLength(buffer, fieldCount);
        if (extra > 0) {
            MBufferUtil.writeLength(buffer, extra);
        }
//        return buffer;
        channel.writeAndFlush(buffer.array());
    }

    @Override
    public void write(ByteBuf buffer) {
        int size = calcPacketSize();
//        buffer = c.checkWriteBuffer(buffer, c.getPacketHeaderSize() + size,writeSocketIfFull);
        MBufferUtil.writeUB3(buffer, size);
        buffer.writeByte(packetId);
        MBufferUtil.writeLength(buffer, fieldCount);
        if (extra > 0) {
            MBufferUtil.writeLength(buffer, extra);
        }
    }

    //    public void write(BufferArray bufferArray) {
//		int size = calcPacketSize();
//		ByteBuffer buffer = bufferArray
//				.checkWriteBuffer(packetHeaderSize + size);
//		BufferUtil.writeUB3(buffer, size);
//		buffer.put(packetId);
//		BufferUtil.writeLength(buffer, fieldCount);
//		if (extra > 0) {
//			BufferUtil.writeLength(buffer, extra);
//		}
//	}

    @Override
    public int calcPacketSize() {
        int size = MBufferUtil.getLength(fieldCount);
        if (extra > 0) {
            size += MBufferUtil.getLength(extra);
        }
        return size;
    }

    @Override
    protected String getPacketInfo() {
        return "MySQL ResultSetHeader Packet";
    }


}