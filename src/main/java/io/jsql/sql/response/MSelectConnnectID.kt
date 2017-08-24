/*
 * Java-based distributed database like Mysql
 */
package io.jsql.sql.response

import com.orientechnologies.orient.core.record.OElement
import com.orientechnologies.orient.core.record.impl.ODocument
import io.jsql.sql.handler.MyResultSet

import java.util.Collections

/**
 * @author jsql
 */
object MSelectConnnectID {

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
    //        fields[i] = PacketUtil.getField("CONNECTION_ID()", Fields.FIELD_TYPE_VAR_STRING);
    //        fields[i++].packetId = ++packetId;
    //        eof.packetId = ++packetId;
    //    }
    //
    //    public static void response(OConnection c) {
    //
    //        byte packetId = eof.packetId;
    //        RowDataPacket row = new RowDataPacket(FIELD_COUNT);
    //        row.add(getConnectID(c));
    //        row.packetId = ++packetId;
    //
    //        EOFPacket lastEof = new EOFPacket();
    //        lastEof.packetId = ++packetId;
    //        c.writeResultSet(header, fields, eof, new RowDataPacket[]{row}, lastEof);
    //    }
    //
    //    private static byte[] getConnectID(OConnection c) {
    //        return StringUtil.encode(new String(RandomUtil.randomBytes(10000)), c.charset);
    ////        return null;
    //    }

    fun getdata(): Any {
        val element = ODocument()
        element.setProperty("CONNECTION_ID()", "1")
        return MyResultSet(listOf<OElement>(element), listOf("CONNECTION_ID()"))
    }
}