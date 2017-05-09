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
package io.jsql.sql.response;

import com.orientechnologies.orient.core.record.OElement;
import com.orientechnologies.orient.core.record.impl.ODocument;
import io.jsql.sql.handler.MyResultSet;

import java.util.Collections;


/**
 * @author jsql
 */
public class MSelectVersion {
//    /**
//     * 服务器版本
//     **/
//    public static final byte[] SERVER_VERSION = "jsql 1.0".getBytes();
//    private static final int FIELD_COUNT = 1;
//    private static final ResultSetHeaderPacket header = PacketUtil.getHeader(FIELD_COUNT);
//    private static final FieldPacket[] fields = new FieldPacket[FIELD_COUNT];
//    private static final EOFPacket eof = new EOFPacket();
//
//    static {
//        int i = 0;
//        byte packetId = 0;
//        header.packetId = ++packetId;
//        fields[i] = PacketUtil.getField("VERSION()", Fields.FIELD_TYPE_VAR_STRING);
//        fields[i++].packetId = ++packetId;
//        eof.packetId = ++packetId;
//    }
//
//    public static void response(OConnection c) {
//
//        byte packetId = eof.packetId;
//        RowDataPacket row = new RowDataPacket(FIELD_COUNT);
//        row.add(SERVER_VERSION);
//        row.packetId = ++packetId;
//        EOFPacket lastEof = new EOFPacket();
//        lastEof.packetId = ++packetId;
//        c.writeResultSet(header, fields, eof, new RowDataPacket[]{row}, lastEof);
//    }

    public static Object getdata() {
        OElement element = new ODocument();
        element.setProperty("VERSION()","1.1");
        return new MyResultSet(Collections.singletonList(element), Collections.singletonList("VERSION()"));
    }
}