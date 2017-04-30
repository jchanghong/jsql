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
package io.jsql.orientserver.response;

import io.jsql.mysql.PacketUtil;
import io.jsql.config.Fields;
import io.jsql.mysql.mysql.EOFPacket;
import io.jsql.mysql.mysql.FieldPacket;
import io.jsql.mysql.mysql.ResultSetHeaderPacket;
import io.jsql.mysql.mysql.RowDataPacket;
import io.jsql.orientserver.OConnection;
import io.jsql.util.StringUtil;

import java.util.List;

/**
 * @author changhong
 * select n个列，只有一行value
 */
public class MselectNResponse {


    /**
     * Responseselect.
     *
     * @param c      the c
     * @param colums the colums
     * @param values the values只有一行
     */
    public static void response(OConnection c, List<String> colums, List<String> values) {
        int FIELD_COUNT;
        ResultSetHeaderPacket header;
        FieldPacket[] fields;
        EOFPacket eof;
        FIELD_COUNT = colums.size();
        header = PacketUtil.getHeader(FIELD_COUNT);
        fields = new FieldPacket[FIELD_COUNT];
        int i = 0;
        byte packetId = 0;
        header.packetId = ++packetId;
        for (String string : colums) {
                fields[i] = PacketUtil.getField(string, Fields.FIELD_TYPE_VAR_STRING);
                fields[i++].packetId = ++packetId;
        }
        eof = new EOFPacket();
        eof.packetId = ++packetId;



            RowDataPacket row = new RowDataPacket(FIELD_COUNT);
        for (String name : values) {
            row.add(StringUtil.encode(name, c.charset));
        }
        row.packetId = ++packetId;
        // write last eof
        EOFPacket lastEof = new EOFPacket();
        lastEof.packetId = ++packetId;
        c.writeResultSet(header, fields, eof, new RowDataPacket[]{row}, lastEof);
    }


}