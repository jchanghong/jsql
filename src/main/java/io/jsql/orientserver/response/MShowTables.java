package io.jsql.orientserver.response;

import com.alibaba.druid.sql.ast.statement.SQLShowTablesStatement;
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import io.jsql.mysql.PacketUtil;
import io.jsql.config.ErrorCode;
import io.jsql.config.Fields;
import io.jsql.databaseorient.adapter.MDBadapter;
import io.jsql.databaseorient.adapter.MtableAdapter;
import io.jsql.mysql.mysql.*;
import io.jsql.orientserver.OConnection;
import io.jsql.util.StringUtil;

import java.util.Set;

/**
 * show tables impl
 *
 * @author yanglixue
 */
public class MShowTables {

    private static final int FIELD_COUNT = 1;
    private static final ResultSetHeaderPacket header = PacketUtil.getHeader(FIELD_COUNT);
    private static final FieldPacket[] fields = new FieldPacket[FIELD_COUNT];
    private static final EOFPacket eof = new EOFPacket();

//    private static final String SCHEMA_KEY = "schemaName";
//    private static final String LIKE_KEY = "like";
//    private static final Pattern pattern = Pattern.compile("^\\s*(SHOW)\\s+(TABLES)(\\s+(FROM)\\s+([a-zA-Z_0-9]+))?(\\s+(LIKE\\s+'(.*)'))?\\s*", Pattern.CASE_INSENSITIVE);

    /**
     * response method.
     *
     * @param c
     * @param stmt
     */
    public static void response(OConnection c, SQLShowTablesStatement stmt, int type) {
        if (stmt.getDatabase() != null) {
            MDBadapter.currentDB = stmt.getDatabase().getSimpleName();
            if (MDBadapter.currentDB.startsWith("`")||MDBadapter.currentDB.startsWith("'")) {
                MDBadapter.currentDB = MDBadapter.currentDB.substring(1, MDBadapter.currentDB.length() - 1);
            }
        }
        if (MDBadapter.currentDB == null) {
            c.writeErrMessage(ErrorCode.ER_NO_DB_ERROR, "no database selected!!!");
            return;
        }
        int i = 0;
        byte packetId = 0;
        header.packetId = ++packetId;
        fields[i] = PacketUtil.getField("Tables in " + c.sqlHander, Fields.FIELD_TYPE_VAR_STRING);
        fields[i++].packetId = ++packetId;
        eof.packetId = ++packetId;

        // write rows
        packetId = eof.packetId;
        ODatabaseDocumentTx documentTx = MDBadapter.getCurrentDB();
        Set<String> getalltable = MtableAdapter.getalltable(documentTx);
        MySQLPacket[] rowss = new MySQLPacket[getalltable.size()];
        int index = 0;
        for (String name : getalltable) {
            RowDataPacket row = new RowDataPacket(FIELD_COUNT);
            row.add(StringUtil.encode(name.toLowerCase(), c.charset));
            row.packetId = ++packetId;
            rowss[index++] = row;
        }
        // write last eof
        EOFPacket lastEof = new EOFPacket();
        lastEof.packetId = ++packetId;
        c.writeResultSet(header, fields, eof, rowss, lastEof);
        documentTx.close();

    }
}
