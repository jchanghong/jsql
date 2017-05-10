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