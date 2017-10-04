/*
 * Java-based distributed database like Mysql
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
object MSelectUser {

    //    private static final int FIELD_COUNT = 1;
    //    private static final ResultSetHeaderPacket header = PacketUtil.getHeader(FIELD_COUNT);
    //    private static final FieldPacket[] fields = new FieldPacket[FIELD_COUNT];
    //    private static final EOFPacket eof = new EOFPacket();
    //    private static final ErrorPacket error = PacketUtil.getShutdown();
    //
    //    static {
    //        int i = 0;
    //        byte packetId = 0;
    //        header.packetId = ++packetId;
    //        fields[i] = PacketUtil.getField("USER()", Fields.FIELD_TYPE_VAR_STRING);
    //        fields[i++].packetId = ++packetId;
    //        eof.packetId = ++packetId;
    //    }
    //
    //    public static void response(OConnection c) {
    ////        if (MycatServer.getInstance().isOnline()) {
    //
    //        byte packetId = eof.packetId;
    //        RowDataPacket row = new RowDataPacket(FIELD_COUNT);
    //        row.add(getUser(c));
    //        row.packetId = ++packetId;
    //        EOFPacket lastEof = new EOFPacket();
    //        lastEof.packetId = ++packetId;
    //        c.writeResultSet(header, fields, eof, new RowDataPacket[]{row}, lastEof);
    ////        } else {
    ////            error.write(c);
    ////        }
    //    }

    //    private static byte[] getUser(OConnection c) {
    //        return StringUtil.encode(c.user + '@' + c.user, c.charset);
    //    }

    fun getdata(): Any {
        val element = ODocument()
        element.setProperty("USER()", "root")
        return MyResultSet(listOf<OElement>(element), listOf("USER()"))
    }
}