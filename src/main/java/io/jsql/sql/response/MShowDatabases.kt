/*
 * Java-based distributed database like Mysql
 */
package io.jsql.sql.response

import com.orientechnologies.orient.core.record.OElement
import com.orientechnologies.orient.core.record.impl.ODocument
import io.jsql.cache.MCache
import io.jsql.sql.OConnection
import io.jsql.sql.handler.MyResultSet
import io.jsql.storage.StorageException
import java.util.*

/**
 * @author showdb database语句
 */
object MShowDatabases {

    //    private static final int FIELD_COUNT = 1;
    //    private static final ResultSetHeaderPacket header = PacketUtil.getHeader(FIELD_COUNT);
    //    private static final FieldPacket[] fields = new FieldPacket[FIELD_COUNT];
    //    private static final EOFPacket eof = new EOFPacket();
    //
    //    static {
    //        int i = 0;
    //        byte packetId = 0;
    //        header.packetId = ++packetId;
    //        fields[i] = PacketUtil.getField("DATABASE", Fields.FIELD_TYPE_VAR_STRING);
    //        fields[i++].packetId = ++packetId;
    //        eof.packetId = ++packetId;
    //    }
    //
    //    public static void response(OConnection c) {
    //        // write rows
    //        byte packetId = eof.packetId;
    //
    //        List<String> strings;
    //        try {
    //            strings = OConnection.DB_ADMIN.getallDBs();
    //        } catch (StorageException e) {
    //            e.printStackTrace();
    //            c.writeErrMessage(e.getMessage());
    //            return;
    //        }
    //        MySQLPacket[] rows = new MySQLPacket[strings.size()];
    //        int index = 0;
    //        for (String name :strings) {
    //            RowDataPacket row = new RowDataPacket(FIELD_COUNT);
    //            row.add(StringUtil.encode(name, c.charset));
    //            row.packetId = ++packetId;
    //            rows[index++] = row;
    //
    //        }
    //        // write last eof
    //        EOFPacket lastEof = new EOFPacket();
    //        lastEof.packetId = ++packetId;
    //        c.writeResultSet(header, fields, eof, rows, lastEof);
    //    }

    fun getdata(): Any {
        var resultSet: MyResultSet? = MCache.showdb().get(MShowDatabases::class.java.name)
        if (resultSet != null) {
            return resultSet
        }
        println("do show--------------------------------")
        val strings: List<String>
        try {
            strings = OConnection.DB_ADMIN!!.getallDBs()
        } catch (e: StorageException) {
            e.printStackTrace()
            return e.message!!
        }

        val elements = ArrayList<OElement>()
        strings.forEach { a ->
            val element = ODocument()
            element.setProperty("DATABASE", a)
            elements.add(element)
        }
        resultSet = MyResultSet(elements, listOf("DATABASE"))
        MCache.showdb().put(MShowDatabases::class.java.name, resultSet)
        return resultSet
    }

}