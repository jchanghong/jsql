package io.jsql.sql.handler.utilstatement

import com.orientechnologies.orient.core.db.document.ODatabaseDocument
import com.orientechnologies.orient.core.metadata.schema.OClass
import com.orientechnologies.orient.core.metadata.schema.OProperty
import io.jsql.config.ErrorCode
import io.jsql.config.Fields
import io.jsql.mysql.PacketUtil
import io.jsql.mysql.mysql.EOFPacket
import io.jsql.mysql.mysql.FieldPacket
import io.jsql.mysql.mysql.ResultSetHeaderPacket
import io.jsql.sql.OConnection
import io.jsql.storage.StorageException

import java.util.ArrayList
import java.util.function.Consumer

/**
 * Created by 长宏 on 2017/3/27.
 */
class ExplainStatement {
    /*


            for (ODocument name : data) {
                RowDataPacket row = new RowDataPacket(FIELD_COUNT);
                selects.forEach(a -> {
                    row.add(StringUtil.encode(name.field(a)==null?"null":name.field(a).toString(), c.getCharset()));
                });
                row.packetId = ++packetId;
                buffer = row.write(buffer, c, true);
            }*/
    // write last eof
    internal var lastEof = EOFPacket()

    companion object {

        fun isme(sql: String, c: OConnection): Boolean {
            val sqll = sql.toUpperCase().trim { it <= ' ' }
            val list = sqll.split("\\s+".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            if (list.size > 1 && list[0] == "EXPLAIN") {
                return true
            } else if (list.size > 2 && list[0] == "EXPLAIN") {
                return ExplainStatement.handle2(sql, c)
            }
            return false
        }

        /*
    explain select*from tb_name
     */
        private fun handle2(sql: String, c: OConnection): Boolean {
            return false
        }

        /*
    explain table完成
     */
        fun handle(statement: String, c: OConnection) {
            if (c.schema == null) {
                c.writeErrMessage(ErrorCode.ER_NO_DB_ERROR, "no database selected!!")
                return
            }

            val stmt = statement
            val FIELD_COUNT: Int
            val header: ResultSetHeaderPacket
            val fields: Array<FieldPacket>
            val eof: EOFPacket
            val selects: List<String>
            /*
        List<ODocument> data;
        try {
            data = DB.query(stmt,c.schema);
        } catch (StorageException e) {
            e.printStackTrace();
            c.writeErrMessage(ErrorCode.ERR_HANDLE_DATA, e.getMessage());
            return;
        }*/
            val documentTx: ODatabaseDocument
            try {
                documentTx = OConnection.DB_ADMIN!!.getdb(c.schema!!)
            } catch (e: StorageException) {
                e.printStackTrace()
                c.writeErrMessage(e.message!!)
                return
            }

            documentTx.activateOnCurrentThread()
            val sqll = stmt.trim { it <= ' ' }
            val list = sqll.split("\\s+".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()//以空格分隔开字符串取出tablename适用于explain tb_name不适用于explain select*from tb_name
            val oClass = documentTx.metadata.schema.getClass(list[1])

            if (oClass == null) {
                c.writeErrMessage(ErrorCode.ERR_HANDLE_DATA, "error")
                documentTx.close()
                return
            }
            val pro = ArrayList<OProperty>()
            oClass.properties().forEach(Consumer<OProperty> { pro.add(it) })

            //selects = MSQLutil.gettablenamefileds(stmt);

            FIELD_COUNT = 6
            header = PacketUtil.getHeader(FIELD_COUNT)
            fields = arrayOfNulls<FieldPacket>(FIELD_COUNT) as Array<FieldPacket>
            var i = 0
            var packetId: Byte = 0
            header.packetId = ++packetId
            fields[0] = PacketUtil.getField("Field", Fields.FIELD_TYPE_VAR_STRING)
            fields[i++].packetId = ++packetId
            fields[1] = PacketUtil.getField("Type", Fields.FIELD_TYPE_VAR_STRING)
            fields[i++].packetId = ++packetId
            fields[2] = PacketUtil.getField("Null", Fields.FIELD_TYPE_VAR_STRING)
            fields[i++].packetId = ++packetId
            fields[3] = PacketUtil.getField("Key", Fields.FIELD_TYPE_VAR_STRING)
            fields[i++].packetId = ++packetId
            fields[4] = PacketUtil.getField("Default", Fields.FIELD_TYPE_VAR_STRING)
            fields[i++].packetId = ++packetId
            fields[5] = PacketUtil.getField("Extra", Fields.FIELD_TYPE_VAR_STRING)
            fields[i++].packetId = ++packetId

            /*
        for (OProperty string : oClass.properties()) {
            if (selects.contains(string.getName())) {
                fields[i] = PacketUtil.getField(string.getName(), Fields.FIELD_TYPE_VAR_STRING);
                fields[i++].packetId = ++packetId;
            }
        }
        */
            eof = EOFPacket()
            eof.packetId = ++packetId


            //        ByteBuffer buffer = c.allocate();
            //        // write header
            //        buffer = header.write(buffer, c, true);
            //
            //        // write fields
            //        for (FieldPacket field : fields) {
            //            buffer = field.write(buffer, c, true);
            //        }
            //        // write eof
            //        buffer = eof.write(buffer, c, true);
            //        // write rows
            //for(OProperty name :pro){
            //    RowDataPacket row = new RowDataPacket(FIELD_COUNT);
            //    row.add(StringUtil.encode(name.getName(),c.getCharset()));
            //    row.add(StringUtil.encode(name.getType().toString(),c.getCharset()));
            //    row.add(StringUtil.encode(name.isNotNull()?"No":"Yes",c.getCharset()));
            //    row.add(StringUtil.encode("  ",c.getCharset()));
            //    row.add(StringUtil.encode("Null ",c.getCharset()));
            //    row.add(StringUtil.encode("  ",c.getCharset()));
            //    row.packetId = ++packetId;
            //    buffer = row.write(buffer, c, true);
        }
    }
    //        lastEof.packetId = ++packetId;
    //        buffer = lastEof.write(buffer, c, true);
    //        // post write
    //        c.write(buffer);
    //        documentTx.close();
}

