package io.jsql.sql.handler;

import com.orientechnologies.orient.core.record.OElement;
import io.jsql.config.Fields;
import io.jsql.mysql.PacketUtil;
import io.jsql.mysql.mysql.*;
import io.jsql.sql.OConnection;
import io.jsql.util.StringUtil;

import java.util.List;

/**
 * Created by 长宏 on 2017/2/26 0026.
 * 默认的处理
 */
public class DefaultHander {
    private static final int FIELD_COUNT = 1;
    private static final ResultSetHeaderPacket header = PacketUtil.getHeader(FIELD_COUNT);
    private static final FieldPacket[] fields = new FieldPacket[FIELD_COUNT];
    private static final EOFPacket eof = new EOFPacket();

    static {
        int i = 0;
        byte packetId = 0;
        header.packetId = ++packetId;
        fields[i] = PacketUtil.getField("DATABASE", Fields.FIELD_TYPE_VAR_STRING);
        fields[i++].packetId = ++packetId;
        eof.packetId = ++packetId;
    }

    /**
     * Response 1 column.只有一列的时候直接用这个函数
     *
     * @param columnname the columnname
     * @param data       the data
     * @param c          the c
     */
    public static void response_1_column(String columnname,List<String> data,OConnection c) {
        PacketUtil.setFieldName(fields[0], columnname);
        // write rows
        byte packetId = eof.packetId;
        MySQLPacket[] rows = new MySQLPacket[data.size()];
        int index = 0;
        for (String name :data) {
            RowDataPacket row = new RowDataPacket(FIELD_COUNT);
            row.add(StringUtil.encode(name, c.charset));
            row.packetId = ++packetId;
            rows[index++] = row;
        }
        // write last eof
        EOFPacket lastEof = new EOFPacket();
        lastEof.packetId = ++packetId;
        c.writeResultSet(header, fields, eof, rows, lastEof);
    }

    /**
     * Onerror.
     *
     * @param e          the e
     * @param connection the connection
     */
    public static void onerror(Exception e, OConnection connection) {

        connection.writeErrMessage(e.getMessage());

    }

    /**
     * Onseccess.
     *  @param data       the data
     * @param columns
     * @param connection the connection
     */
    public static void onseccess(List<OElement> datas, List<String> columns, OConnection connection) {
        int FIELD_COUNT;
        ResultSetHeaderPacket header;
        FieldPacket[] fields;
        EOFPacket eof;
        FIELD_COUNT = columns.size();
        header = PacketUtil.getHeader(FIELD_COUNT);
        fields = new FieldPacket[FIELD_COUNT];
        int i = 0;
        byte packetId = 0;
        header.packetId = ++packetId;
        for (String string : columns) {
                fields[i] = PacketUtil.getField(string, Fields.FIELD_TYPE_VAR_STRING);
                fields[i++].packetId = ++packetId;
        }
        eof = new EOFPacket();
        eof.packetId = ++packetId;


        int index = 0;
        MySQLPacket[] rows = new MySQLPacket[datas.size()];
        for (OElement name : datas) {
            RowDataPacket row = new RowDataPacket(FIELD_COUNT);
            columns.forEach(a -> {
                row.add(StringUtil.encode(name.getProperty(a) == null ? "null" : name.getProperty(a).toString(), connection.charset));
            });
            row.packetId = ++packetId;
            rows[index++] = row;
        }
//        // write last eof
        EOFPacket lastEof = new EOFPacket();
        lastEof.packetId = ++packetId;
        connection.writeResultSet(header, fields, eof, rows, lastEof);
    }

    /**
     * Onseccess.
     *
     * @param row        the row
     * @param connection the connection
     */
    public static void onseccess(long row, OConnection connection) {
        OkPacket okPacket = new OkPacket();
        okPacket.read(OkPacket.OK);
        okPacket.affectedRows = row;
        okPacket.write(connection.channelHandlerContext.channel());
    }
}
