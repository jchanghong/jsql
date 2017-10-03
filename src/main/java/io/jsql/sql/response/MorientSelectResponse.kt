/*
 * Java-based distributed database like Mysql
 */
package io.jsql.sql.response

import com.orientechnologies.orient.core.db.document.ODatabaseDocument
import com.orientechnologies.orient.core.record.OElement
import io.jsql.orientstorage.sqlhander.sqlutil.MSQLutil
import io.jsql.sql.OConnection
import io.jsql.sql.handler.MyResultSet
import io.jsql.storage.StorageException
import java.util.*
import java.util.stream.Stream
import kotlin.streams.toList

/**
 * @author changhong 默认的响应
 * *         调用orientdb的select
 * *         orientdb select
 */
object MorientSelectResponse {

    //
    //    /**
    //     * Responseselect.
    //     * statement====stmt
    //     * stem可以是mysql的语句，也可以经过一定的变化
    //     *
    //     * @param c    the c
    //     * @param stmt the stmt
    //     */
    //    public static void responseselect(OConnection c, String stmt, SQLSelectStatement statement) {
    //        if (c.schema == null) {
    //            c.writeErrMessage(ErrorCode.ER_NO_DB_ERROR, "no database selected!!");
    //            return;
    //        }
    //        int FIELD_COUNT;
    //        ResultSetHeaderPacket header;
    //        FieldPacket[] fields;
    //        EOFPacket eof;
    //        List<String> selects;
    //        Stream<OElement> data;
    //        try {
    //            data = OConnection.DB_ADMIN.query(stmt, c.schema);
    //        } catch (StorageException e) {
    //            e.printStackTrace();
    //            c.writeErrMessage(ErrorCode.ERR_HANDLE_DATA, e.getMessage());
    //            return;
    //        }
    //        ODatabaseDocument documentTx = null;
    //        try {
    //            documentTx = OConnection.DB_ADMIN.getdb(c.schema);
    //        } catch (StorageException e) {
    //            e.printStackTrace();
    //            c.writeErrMessage(e.getMessage());
    //            return;
    //        }
    //        documentTx.activateOnCurrentThread();
    //        OClass oClass = documentTx.getMetadata().getSchema().getClass(MSQLutil.gettablename(statement));
    //        if (oClass == null) {
    //            c.writeErrMessage(ErrorCode.ERR_HANDLE_DATA, "error");
    ////            documentTx.close();
    //            OConnection.DB_ADMIN.close(documentTx);
    //            return;
    //        }
    //        List<String> selectall = new ArrayList<>();
    //        oClass.properties().forEach(a -> selectall.add(a.getName()));
    //        selects = MSQLutil.gettablenamefileds(stmt);
    //        if (selects.contains("*")) {
    //            selects = selectall;
    //        }
    //        FIELD_COUNT = selects.size();
    //        header = PacketUtil.getHeader(FIELD_COUNT);
    //        fields = new FieldPacket[FIELD_COUNT];
    //        int i = 0;
    //        byte packetId = 0;
    //        header.packetId = ++packetId;
    //        for (OProperty string : oClass.properties()) {
    //            if (selects.contains(string.getName())) {
    //                fields[i] = PacketUtil.getField(string.getName(), Fields.FIELD_TYPE_VAR_STRING);
    //                fields[i++].packetId = ++packetId;
    //            }
    //        }
    //        eof = new EOFPacket();
    //        eof.packetId = ++packetId;
    //
    //
    ////        ByteBuffer buffer = c.allocate();
    ////        // write header
    ////        buffer = header.write(buffer, c, true);
    ////
    ////        // write fields
    ////        for (FieldPacket field : fields) {
    ////            buffer = field.write(buffer, c, true);
    ////        }
    ////        // write eof
    ////        buffer = eof.write(buffer, c, true);
    ////        // write rows
    //        int index = 0;
    //        List<OElement> datas = new ArrayList<>();
    //        data.forEach(a -> datas.add(a));
    //        MySQLPacket[] rows = new MySQLPacket[datas.size()];
    //        for (OElement name : datas) {
    //            RowDataPacket row = new RowDataPacket(FIELD_COUNT);
    //            selects.forEach(a -> {
    //                row.add(StringUtil.encode(name.getProperty(a) == null ? "null" : name.getProperty(a).toString(), c.charset));
    //            });
    //            row.packetId = ++packetId;
    //            rows[index++] = row;
    //        }
    ////        // write last eof
    //        EOFPacket lastEof = new EOFPacket();
    //        lastEof.packetId = ++packetId;
    ////        buffer = lastEof.write(buffer, c, true);
    ////        // post write
    ////        c.write(buffer);
    //        OConnection.DB_ADMIN.close(documentTx);
    //        c.writeResultSet(header, fields, eof, rows, lastEof);
    //    }


    fun getdata(selectStatement: SQLSelectStatement, c: OConnection): Any {
        if (c.schema == null) {
            return "no database selected!!"
        }
        var selects: List<String>
        val data: Stream<OElement>
        try {
            data = OConnection.DB_ADMIN!!.query(selectStatement.toString(), c.schema!!)
        } catch (e: StorageException) {
            e.printStackTrace()
            return e.message!!
        }

        val documentTx: ODatabaseDocument
        try {
            documentTx = OConnection.DB_ADMIN!!.getdb(c.schema!!)
        } catch (e: StorageException) {
            e.printStackTrace()
            return e.message!!
        }

        documentTx.activateOnCurrentThread()
        val oClass = documentTx.getClass(MSQLutil.gettablename(selectStatement))
        if (oClass == null) {
            //                documentTx.close();
            OConnection.DB_ADMIN!!.close(documentTx)
            return "table  error"
        }
        val selectall = ArrayList<String>()
        oClass.properties().forEach { a -> selectall.add(a.name) }
        selects = MSQLutil.gettablenamefileds(selectStatement)
        if (selects.contains("*")) {
            selects = selectall
        }

        val datas = data.toList()
        //            documentTx.close();
        OConnection.DB_ADMIN!!.close(documentTx)

        return MyResultSet(datas, selects)
    }
}