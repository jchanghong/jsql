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
import io.jsql.sql.handler.MyResultSet

import java.util.Collections

/**
 * @author jsql
 */
object MSelectIdentity {

    //    private static final int FIELD_COUNT = 1;
    //    private static final ResultSetHeaderPacket header = PacketUtil.getHeader(FIELD_COUNT);
    //
    //    static {
    //        byte packetId = 0;
    //        header.packetId = ++packetId;
    //    }
    //
    //    public static void response(OConnection c, String stmt, int aliasIndex, final String orgName) {
    //        String alias = null;
    //        if (alias == null) {
    //            alias = orgName;
    //        }
    //
    //
    //        // write fields
    //        byte packetId = header.packetId;
    //        FieldPacket field = PacketUtil.getField(alias, orgName, Fields.FIELD_TYPE_LONGLONG);
    //        field.packetId = ++packetId;
    //
    //        // write eof
    //        EOFPacket eof = new EOFPacket();
    //        eof.packetId = ++packetId;
    //
    //        // write rows
    //        RowDataPacket row = new RowDataPacket(FIELD_COUNT);
    //        row.add(LongUtil.toBytes(c.lastInsertId));
    //        row.packetId = ++packetId;
    //
    //        // write last eof
    //        EOFPacket lastEof = new EOFPacket();
    //        lastEof.packetId = ++packetId;
    //
    //        // post write
    //        c.writeResultSet(header, new FieldPacket[]{field}, eof, new RowDataPacket[]{row}, lastEof);
    //    }

    fun getdata(): Any {
        val element = ODocument()
        element.setProperty("id", "1")
        return MyResultSet(listOf<OElement>(element), listOf("id"))
    }
}