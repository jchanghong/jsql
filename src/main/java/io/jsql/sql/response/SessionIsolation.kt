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
package io.jsql.sql.response

import com.orientechnologies.orient.core.record.OElement
import com.orientechnologies.orient.core.record.impl.ODocument
import io.jsql.config.Fields
import io.jsql.mysql.PacketUtil
import io.jsql.mysql.mysql.*
import io.jsql.sql.OConnection
import io.jsql.sql.handler.MyResultSet
import io.jsql.util.StringUtil

import java.util.Collections

/**
 * @author jsql
 */
object SessionIsolation {

    private val FIELD_COUNT = 1
    private val header = PacketUtil.getHeader(FIELD_COUNT)
    private val fields = arrayOfNulls<FieldPacket>(FIELD_COUNT)
    private val eof = EOFPacket()

    init {
        var i = 0
        var packetId: Byte = 0
        header.packetId = ++packetId
        fields[i] = PacketUtil.getField("@@session.tx_isolation", Fields.FIELD_TYPE_STRING)
        fields[i++]!!.packetId = ++packetId
        eof.packetId = ++packetId
    }

    fun response(c: OConnection) {

        var packetId = eof.packetId
        val row = RowDataPacket(FIELD_COUNT)
        row.add(StringUtil.encode("REPEATABLE-READ", c.charset)!!)
        row.packetId = ++packetId
        val lastEof = EOFPacket()
        lastEof.packetId = ++packetId
        c.writeResultSet(header, fields as Array<MySQLPacket>, eof, arrayOf(row), lastEof)
    }

    fun getdata(): Any {
        val element = ODocument()
        element.setProperty("@@session.tx_isolation", "5")
        return MyResultSet(listOf<OElement>(element), listOf("@@session.tx_isolation"))
    }
}