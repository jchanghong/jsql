package io.jsql.sql.response;

import com.alibaba.druid.sql.ast.statement.SQLShowTablesStatement;
import com.orientechnologies.orient.core.db.document.ODatabaseDocument;
import com.orientechnologies.orient.core.record.OElement;
import com.orientechnologies.orient.core.record.impl.ODocument;
import io.jsql.config.ErrorCode;
import io.jsql.config.Fields;
import io.jsql.mysql.PacketUtil;
import io.jsql.mysql.mysql.*;
import io.jsql.sql.OConnection;
import io.jsql.sql.handler.MyResultSet;
import io.jsql.storage.StorageException;
import io.jsql.util.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * showdb tables impl
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
            c.schema = stmt.getDatabase().getSimpleName();
            if (c.schema.startsWith("`") || c.schema.startsWith("'")) {
                c.schema = c.schema.substring(1, c.schema.length() - 1);
            }
        }
        if (c.schema == null) {
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
        ODatabaseDocument documentTx;
        try {
            documentTx = OConnection.DB_ADMIN.getdb(c.schema);
        } catch (StorageException e) {
            e.printStackTrace();
            c.writeErrMessage(e.getMessage());
            return;
        }
        documentTx.activateOnCurrentThread();
        List<String> getalltable = null;
        try {
            getalltable = OConnection.TABLE_ADMIN.getalltable(c.schema);
        } catch (StorageException e) {
            e.printStackTrace();
        }
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
        OConnection.DB_ADMIN.close(documentTx);
        c.writeResultSet(header, fields, eof, rowss, lastEof);
//        documentTx.close();

    }


    public static Object getdata(SQLShowTablesStatement stmt, OConnection c) {
        if (stmt.getDatabase() != null) {
            c.schema = stmt.getDatabase().getSimpleName();
            if (c.schema.startsWith("`") || c.schema.startsWith("'")) {
                c.schema = c.schema.substring(1, c.schema.length() - 1);
            }
        }
        if (c.schema == null) {
            return "no database selected!!!";
        }
        List<String> getalltable = null;
        try {
            getalltable = OConnection.TABLE_ADMIN.getalltable(c.schema);
        } catch (StorageException e) {
            return e.getMessage();
        }
        List<OElement> elements = new ArrayList<>();
        getalltable.forEach(a->{
            OElement element = new ODocument();
            element.setProperty("table", a);
            elements.add(element);
        });
        return new MyResultSet(elements, Collections.singletonList("table"));
    }
}
