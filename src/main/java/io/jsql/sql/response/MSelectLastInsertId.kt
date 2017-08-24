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
object MSelectLastInsertId {

    //    private static final String ORG_NAME = "LAST_INSERT_ID()";
    //    private static final int FIELD_COUNT = 1;
    //    private static final ResultSetHeaderPacket header = PacketUtil.getHeader(FIELD_COUNT);
    //
    //    static {
    //        byte packetId = 0;
    //        header.packetId = ++packetId;
    //    }
    //
    //    public static void response(OConnection c, String stmt, int aliasIndex) {
    //        String alias = null;
    ////        String alias = ParseUtil.parseAlias(stmt, aliasIndex);
    //        if (alias == null) {
    //            alias = ORG_NAME;
    //        }
    //
    //
    //        // write fields
    //        byte packetId = header.packetId;
    //        FieldPacket field = PacketUtil.getField(alias, ORG_NAME, Fields.FIELD_TYPE_LONGLONG);
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
    //        c.writeResultSet(header, new FieldPacket[]{field}, eof, new RowDataPacket[]{row}, lastEof);
    //    }

    fun getdata(): Any {
        val element = ODocument()
        element.setProperty("id", "1")
        return MyResultSet(listOf<OElement>(element), listOf("id"))
    }
}