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