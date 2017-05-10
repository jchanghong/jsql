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

import io.jsql.config.Fields
import io.jsql.mysql.PacketUtil
import io.jsql.mysql.mysql.*
import io.jsql.sql.OConnection
import io.jsql.util.StringUtil

/**
 * @author changhong
 * *         select n个列，只有一行value
 */
object MselectNResponse {


    /**
     * Responseselect.

     * @param c      the c
     * *
     * @param colums the colums
     * *
     * @param values the values只有一行
     */
    fun response(c: OConnection, colums: List<String>, values: List<String>) {
        val FIELD_COUNT: Int
        val header: ResultSetHeaderPacket
        val fields: Array<FieldPacket?>
        val eof: EOFPacket
        FIELD_COUNT = colums.size
        header = PacketUtil.getHeader(FIELD_COUNT)
        fields = arrayOfNulls<FieldPacket>(FIELD_COUNT)
        var i = 0
        var packetId: Byte = 0
        header.packetId = ++packetId
        for (string in colums) {
            fields[i] = PacketUtil.getField(string, Fields.FIELD_TYPE_VAR_STRING)
            fields[i++]!!.packetId = ++packetId
        }
        eof = EOFPacket()
        eof.packetId = ++packetId


        val row = RowDataPacket(FIELD_COUNT)
        for (name in values) {
            row.add(StringUtil.encode(name, c.charset)!!)
        }
        row.packetId = ++packetId
        // write last eof
        val lastEof = EOFPacket()
        lastEof.packetId = ++packetId
        c.writeResultSet(header, fields as Array<MySQLPacket>, eof, arrayOf(row), lastEof)
    }


}