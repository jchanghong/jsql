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
import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import io.netty.channel.Channel

/**
 * <pre>
 * From server to client, in response to prepared statement initialization packet.
 * It is made up of:
 * 1.a PREPARE_OK packet
 * 2.if "number of parameters" > 0
 * (field packets) as in a Result Set Header Packet
 * (EOF packet)
 * 3.if "number of columns" > 0
 * (field packets) as in a Result Set Header Packet
 * (EOF packet)

 * -----------------------------------------------------------------------------------------

 * Bytes              Name
 * -----              ----
 * 1                  0 - marker for OK packet
 * 4                  statement_handler_id
 * 2                  number of columns in result set
 * 2                  number of parameters in query
 * 1                  filler (always 0)
 * 2                  warning count

 * @see http://dev.mysql.com/doc/internals/en/prepared-statement-initialization-packet.html
</pre> *


 * @author jsql
 * *
 * @author changhong
 */
class PreparedOkPacket : MySQLPacket() {

    val flag: Byte
    val filler: Byte
    var statementId: Long = 0
    var columnsNumber: Int = 0
    var parametersNumber: Int = 0
    var warningCount: Int = 0

    init {
        this.flag = 0
        this.filler = 0
    }

    override fun write(channel: Channel) {
        val size = calcPacketSize()
        val buffer = Unpooled.buffer(size)
        //        buffer = c.checkWriteBuffer(buffer, c.getPacketHeaderSize() + size,writeSocketIfFull);
        MBufferUtil.writeUB3(buffer, size)
        buffer.writeByte(packetId.toInt())
        buffer.writeByte(flag.toInt())
        MBufferUtil.writeUB4(buffer, statementId)
        MBufferUtil.writeUB2(buffer, columnsNumber)
        MBufferUtil.writeUB2(buffer, parametersNumber)
        buffer.writeByte(filler.toInt())
        MBufferUtil.writeUB2(buffer, warningCount)
        channel.writeAndFlush(buffer.array())
    }

    override fun read(data: ByteArray) {

    }

    override fun calcPacketSize(): Int {
        return 12
    }

    protected override val packetInfo: String
        get() = "MySQL PreparedOk Packet"

}