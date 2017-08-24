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
object MSelectTxReadOnly {
    //    private static final int FIELD_COUNT = 1;
    //    private static final ResultSetHeaderPacket header = PacketUtil.getHeader(FIELD_COUNT);
    //    private static final FieldPacket[] fields = new FieldPacket[FIELD_COUNT];
    //    private static final EOFPacket eof = new EOFPacket();
    //    private static final byte[] longbt = LongUtil.toBytes(0);
    //
    //    static {
    //        int i = 0;
    //        byte packetId = 0;
    //        header.packetId = ++packetId;
    //        fields[i] = PacketUtil.getField("@@session.tx_read_only", Fields.FIELD_TYPE_LONG);
    //        fields[i++].packetId = ++packetId;
    //        eof.packetId = ++packetId;
    //
    //    }
    //
    //    public static void response(OConnection c) {
    //
    //        byte packetId = eof.packetId;
    //        RowDataPacket row = new RowDataPacket(FIELD_COUNT);
    //        row.add(longbt);
    //        row.packetId = ++packetId;
    //
    //        EOFPacket lastEof = new EOFPacket();
    //        lastEof.packetId = ++packetId;
    //        c.writeResultSet(header, fields, eof, new RowDataPacket[]{row}, lastEof);
    //
    //    }

    fun getdata(): Any {
        val element = ODocument()
        element.setProperty("@@session.tx_read_only", "true")
        return MyResultSet(listOf<OElement>(element), listOf("@@session.tx_read_only"))
    }
}
