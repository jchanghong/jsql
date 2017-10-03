/*
 * Java-based distributed database like Mysql
 */
package io.jsql.sql.response

import com.orientechnologies.orient.core.record.OElement
import com.orientechnologies.orient.core.record.impl.ODocument
import io.jsql.sql.handler.MyResultSet


/**
 * @author jsql
 * *
 * @author changhong
 */
object MSelectVersionComment {

    //    private static final byte[] VERSION_COMMENT = "jsql sql".getBytes();
    //    private static final int FIELD_COUNT = 1;
    //    private static final ResultSetHeaderPacket header = PacketUtil.getHeader(FIELD_COUNT);
    //    private static final FieldPacket[] fields = new FieldPacket[FIELD_COUNT];
    //    private static final EOFPacket eof = new EOFPacket();
    //
    //    static {
    //        int i = 0;
    //        byte packetId = 0;
    //        header.packetId = ++packetId;
    //        fields[i] = PacketUtil.getField("@@VERSION_COMMENT", Fields.FIELD_TYPE_VAR_STRING);
    //        fields[i++].packetId = ++packetId;
    //        eof.packetId = ++packetId;
    //    }
    //
    //    public static void response(OConnection c) {
    //        byte packetId = eof.packetId;
    //        RowDataPacket row = new RowDataPacket(FIELD_COUNT);
    //        row.add(VERSION_COMMENT);
    //        row.packetId = ++packetId;
    //        EOFPacket lastEof = new EOFPacket();
    //        lastEof.packetId = ++packetId;
    //        c.writeResultSet(header, fields, eof, new MySQLPacket[]{row}, lastEof);
    //    }

    fun getdata(): Any {
        val element = ODocument()
        element.setProperty("@@VERSION_COMMENT", "jsql sql")
        return MyResultSet(listOf<OElement>(element), listOf("@@VERSION_COMMENT"))
    }
}