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
import com.sun.org.apache.bcel.internal.generic.NEW;
import io.jsql.config.Fields;
import io.jsql.mysql.PacketUtil;
import io.jsql.mysql.mysql.*;
import io.jsql.sql.OConnection;
import io.jsql.sql.handler.MyResultSet;
import io.jsql.sql.handler.data_mannipulation.MSelectHandler;
import io.jsql.util.RandomUtil;
import io.jsql.util.StringUtil;

import java.util.Collections;

/**
 * @author jsql
 */
public class MSelectConnnectID {

    private static final int FIELD_COUNT = 1;
    private static final ResultSetHeaderPacket header = PacketUtil.getHeader(FIELD_COUNT);
    private static final FieldPacket[] fields = new FieldPacket[FIELD_COUNT];
    private static final EOFPacket eof = new EOFPacket();
    private static final ErrorPacket error = PacketUtil.getShutdown();

    static {
        int i = 0;
        byte packetId = 0;
        header.packetId = ++packetId;
        fields[i] = PacketUtil.getField("CONNECTION_ID()", Fields.FIELD_TYPE_VAR_STRING);
        fields[i++].packetId = ++packetId;
        eof.packetId = ++packetId;
    }

    public static void response(OConnection c) {

        byte packetId = eof.packetId;
        RowDataPacket row = new RowDataPacket(FIELD_COUNT);
        row.add(getConnectID(c));
        row.packetId = ++packetId;

        EOFPacket lastEof = new EOFPacket();
        lastEof.packetId = ++packetId;
        c.writeResultSet(header, fields, eof, new RowDataPacket[]{row}, lastEof);
    }

    private static byte[] getConnectID(OConnection c) {
        return StringUtil.encode(new String(RandomUtil.randomBytes(10000)), c.charset);
//        return null;
    }

    public static Object getdata() {
        OElement element = new ODocument();
        element.setProperty("CONNECTION_ID()","1");
        return new MyResultSet(Collections.singletonList(element), Collections.singletonList("CONNECTION_ID()"));
    }
}