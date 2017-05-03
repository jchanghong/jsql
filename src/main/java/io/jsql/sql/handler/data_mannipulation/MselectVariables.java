package io.jsql.sql.handler.data_mannipulation;

import com.alibaba.druid.sql.ast.statement.SQLSelectItem;
import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlSelectQueryBlock;
import com.orientechnologies.orient.core.record.OElement;
import com.orientechnologies.orient.core.record.impl.ODocument;
import io.jsql.config.Fields;
import io.jsql.orientstorage.constant.Minformation_schama;
import io.jsql.orientstorage.constant.MvariableTable;
import io.jsql.mysql.PacketUtil;
import io.jsql.mysql.mysql.*;
import io.jsql.sql.OConnection;
import io.jsql.storage.StorageException;
import io.jsql.util.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by 长宏 on 2017/3/23 0023.
 */
public class MselectVariables {

    private static final int FIELD_COUNT = 2;
    private static final ResultSetHeaderPacket header = PacketUtil.getHeader(FIELD_COUNT);
    private static final FieldPacket[] fields = new FieldPacket[FIELD_COUNT];
    private static final EOFPacket eof = new EOFPacket();

    static {
        int i = 0;
        byte packetId = 0;
        header.packetId = ++packetId;
        fields[i] = PacketUtil.getField("Variable_name", Fields.FIELD_TYPE_VAR_STRING);
        fields[i++].packetId = ++packetId;
        fields[i] = PacketUtil.getField("values", Fields.FIELD_TYPE_VAR_STRING);
        fields[i++].packetId = ++packetId;
        eof.packetId = ++packetId;
    }

    /**
     * Handle.
     *
     * @param c         the c
     * @param statement the statement 一定是show variables 或者select @@这样的语句
     *                  然后变成select * from variables
     */
    public static void handle(OConnection c, String statement) {
        // write rows
        byte packetId = eof.packetId;
        List<OElement> datas = new ArrayList<>();
        try {
            OConnection.DB_ADMIN.query(statement, Minformation_schama.dbname).forEach(a -> datas.add(a));
        } catch (StorageException e) {
            e.printStackTrace();
            c.writeErrMessage(e.getMessage());
            return;
        }
        MySQLPacket[] rows = new MySQLPacket[datas.size()];
        int index = 0;
        for (OElement name : datas) {
            RowDataPacket row = new RowDataPacket(FIELD_COUNT);
            row.add(StringUtil.encode(name.getProperty("Variable_name"), c.charset));
            row.add(StringUtil.encode(name.getProperty("value"), c.charset));
            row.packetId = ++packetId;
            rows[index++] = row;
        }
        // write last eof
        EOFPacket lastEof = new EOFPacket();
        lastEof.packetId = ++packetId;
        c.writeResultSet(header, fields, eof, rows, lastEof);
    }

    public static List<String> getcolumn(SQLSelectStatement selectStatement) {
        MySqlSelectQueryBlock block = (MySqlSelectQueryBlock) selectStatement.getSelect().getQuery();
        List<SQLSelectItem> items = block.getSelectList();
        List<String> list = new ArrayList<>();
        items.forEach(a -> list.add(a.getAlias()));
        return list;
    }

    public static List<OElement> getbs(SQLSelectStatement selectStatement, List<String> column) {
        try {
            return  OConnection.DB_ADMIN.query("select * from " + MvariableTable.tablename, Minformation_schama.dbname).collect(Collectors.toList());
        } catch (StorageException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String getv(List<OElement> datas, String c) {
        for (OElement d :
                datas) {
            if (d.getProperty("Variable_name").equals(c)) {
                return d.getProperty("value");
            }
        }
        return null;
    }
}
