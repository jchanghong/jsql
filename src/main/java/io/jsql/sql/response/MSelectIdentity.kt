/*
 * Java-based distributed database like Mysql
 */
package io.jsql.sql.response

import com.orientechnologies.orient.core.record.OElement
import com.orientechnologies.orient.core.record.impl.ODocument
import io.jsql.sql.handler.MyResultSet

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