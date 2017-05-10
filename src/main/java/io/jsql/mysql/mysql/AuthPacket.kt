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

import io.jsql.config.Capabilities
import io.jsql.mysql.MBufferUtil
import io.jsql.mysql.MySQLMessage
import io.jsql.mysql.StreamUtil
import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled

import java.io.IOException
import java.io.OutputStream

/**
 * From client to server during initial handshake.
 *
 *
 * <pre>
 * Bytes                        Name
 * -----                        ----
 * 4                            client_flags
 * 4                            max_packet_size
 * 1                            charset_number
 * 23                           (filler) always 0x00...
 * n (Null-Terminated String)   user
 * n (Length Coded Binary)      scramble_buff (1 + x bytes)
 * n (Null-Terminated String)   databasename (optional)

 * @see http://forge.mysql.com/wiki/MySQL_Internals_ClientServer_Protocol.Client_Authentication_Packet
</pre> *


 * @author jsql
 * *
 * @author changhong
 */
class AuthPacket : MySQLPacket() {

    var clientFlags: Long = 0
    var maxPacketSize: Long = 0
    var charsetIndex: Int = 0
    var extra: ByteArray?=null // from FILLER(23)
    var user: String? = null
    var password: ByteArray? = null
    var database: String? = null

    override fun read(data: ByteArray) {
        val mm = MySQLMessage(data)
        packetLength = mm.readUB3()
        packetId = mm.read()
        clientFlags = mm.readUB4()
        maxPacketSize = mm.readUB4()
        charsetIndex = mm.read().toInt() and 0xff
        // read extra
        val current = mm.position()
        val len = mm.readLength().toInt()
        if (len > 0 && len < FILLER.size) {
            val ab = ByteArray(len)
            System.arraycopy(mm.bytes(), mm.position(), ab, 0, len)
            this.extra = ab
        }
        mm.position(current + FILLER.size)
        user = mm.readStringWithNull()
        password = mm.readBytesWithLength()
        if (!(clientFlags and Capabilities.CLIENT_CONNECT_WITH_DB.toLong() ).equals(0) && mm.hasRemaining()) {
            database = mm.readStringWithNull()
        }
    }

    @Throws(IOException::class)
    fun write(out: OutputStream) {
        StreamUtil.writeUB3(out, calcPacketSize())
        StreamUtil.write(out, packetId)
        StreamUtil.writeUB4(out, clientFlags)
        StreamUtil.writeUB4(out, maxPacketSize)
        StreamUtil.write(out, charsetIndex.toByte())
        out.write(FILLER)
        if (user == null) {
            StreamUtil.write(out, 0.toByte())
        } else {
            StreamUtil.writeWithNull(out, user!!.toByteArray())
        }
        if (password == null) {
            StreamUtil.write(out, 0.toByte())
        } else {
            StreamUtil.writeWithLength(out, password!!)
        }
        if (database == null) {
            StreamUtil.write(out, 0.toByte())
        } else {
            StreamUtil.writeWithNull(out, database!!.toByteArray())
        }
    }

    override fun write(c: io.netty.channel.Channel) {
        val buffer = Unpooled.buffer(100)
        //        ByteBuffer buffer = c.allocate();
        MBufferUtil.writeUB3(buffer, calcPacketSize())
        buffer.writeByte(packetId.toInt())
        MBufferUtil.writeUB4(buffer, clientFlags)
        MBufferUtil.writeUB4(buffer, maxPacketSize)
        buffer.writeByte(charsetIndex.toByte().toInt())
        buffer.writeBytes(FILLER)
        //        buffer = c.writeToBuffer(FILLER, buffer);
        if (user == null) {
            //            buffer = c.checkWriteBuffer(buffer, 1,true);
            buffer.writeByte(0.toByte().toInt())
        } else {
            val userData = user!!.toByteArray()
            //            buffer = c.checkWriteBuffer(buffer, userData.length + 1,true);
            MBufferUtil.writeWithNull(buffer, userData)
        }
        if (password == null) {
            //            buffer = c.checkWriteBuffer(buffer, 1,true);
            buffer.writeByte(0.toByte().toInt())
        } else {
            //            buffer = c.checkWriteBuffer(buffer, MBufferUtil.getLength(password),true);
            MBufferUtil.writeWithLength(buffer, password!!)
        }
        if (database == null) {
            //            buffer = c.checkWriteBuffer(buffer, 1,true);
            buffer.writeByte(0.toByte().toInt())
        } else {
            val databaseData = database!!.toByteArray()
            //            buffer = c.checkWriteBuffer(buffer, databaseData.length + 1,true);
            MBufferUtil.writeWithNull(buffer, databaseData)
        }
        c.writeAndFlush(buffer)
    }

    override fun calcPacketSize(): Int {
        var size = 32// 4+4+1+23;
        size += if (user == null) 1 else user!!.length + 1
        size += if (password == null) 1 else MBufferUtil.getLength(password!!)
        size += if (database == null) 1 else database!!.length + 1
        return size
    }

    protected override val packetInfo: String
        get() = "MySQL Authentication Packet"

    companion object {
        private val FILLER = ByteArray(23)
    }

}