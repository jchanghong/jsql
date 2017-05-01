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

import io.jsql.mysql.ByteUtil;
import io.jsql.mysql.MBufferUtil;
import io.jsql.mysql.StreamUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author jsql
 * @author changhong
 */
public class BinaryPacket extends MySQLPacket {
    public static final byte OK = 1;
    public static final byte ERROR = 2;
    public static final byte HEADER = 3;
    public static final byte FIELD = 4;
    public static final byte FIELD_EOF = 5;
    public static final byte ROW = 6;
    public static final byte PACKET_EOF = 7;

    public byte[] data;

    public void read(InputStream in) throws IOException {
        packetLength = StreamUtil.readUB3(in);
        packetId = StreamUtil.read(in);
        byte[] ab = new byte[packetLength];
        StreamUtil.read(in, ab, 0, ab.length);
        data = ab;
    }

    @Override
    public void write(Channel channel) {
        ByteBuf buffer = Unpooled.buffer(50);
//        buffer = c.checkWriteBuffer(buffer, c.getPacketHeaderSize(),writeSocketIfFull);
        MBufferUtil.writeUB3(buffer, calcPacketSize());
        buffer.writeByte(packetId);
//        buffer = c.writeToBuffer(data, buffer);
        buffer.writeBytes(data);
        channel.writeAndFlush(buffer);
//        return buffer;
    }

    @Override
    public void read(byte[] data) {

        packetLength = ByteUtil.readUB3(data, 0);
        packetId = data[3];
        byte[] ab = new byte[packetLength];
        System.arraycopy(data, 4, ab, 0, packetLength);
        data = ab;
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

    @Override
    public int calcPacketSize() {
        return data == null ? 0 : data.length;
    }

    @Override
    protected String getPacketInfo() {
        return "MySQL Binary Packet";
    }

}