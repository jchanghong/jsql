package io.jsql.sql.handler.adminstatement

import com.alibaba.druid.sql.ast.SQLStatement
import com.alibaba.druid.sql.ast.statement.SQLExplainStatement
import io.jsql.config.ErrorCode
import io.jsql.config.Fields
import io.jsql.mysql.PacketUtil
import io.jsql.mysql.mysql.FieldPacket
import io.jsql.sql.OConnection
import io.jsql.sql.handler.SqlStatementHander
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

import java.util.regex.Pattern

/**
 * @author jsql
 */
@Component
class ExplainHandler : SqlStatementHander() {

    override fun supportSQLstatement(): Class<out SQLStatement> {
        return SQLExplainStatement::class.java
    }

    @Throws(Exception::class)
    override fun handle(sqlStatement: SQLStatement): Any? {
        return null
    }

    companion object {

        private val logger = LoggerFactory.getLogger(ExplainHandler::class.java)
        private val pattern = Pattern.compile("(?:(\\s*next\\s+value\\s+for\\s*MYCATSEQ_(\\w+))(,|\\)|\\s)*)+", Pattern.CASE_INSENSITIVE)
        //    private static final RouteResultsetNode[] EMPTY_ARRAY = new RouteResultsetNode[0];
        private val FIELD_COUNT = 2
        private val fields = arrayOfNulls<FieldPacket>(FIELD_COUNT)

        init {
            fields[0] = PacketUtil.getField("DATA_NODE",
                    Fields.FIELD_TYPE_VAR_STRING)
            fields[1] = PacketUtil.getField("SQL", Fields.FIELD_TYPE_VAR_STRING)
        }

        fun handle(stmt: String, c: OConnection, offset: Int) {
            var stmt = stmt
            stmt = stmt.substring(offset).trim { it <= ' ' }

            //        RouteResultset rrs = null;
            //        if (rrs == null) {
            //            return;
            //        }
            //
            //        ByteBuffer buffer = c.allocate();

            // write header
            //        ResultSetHeaderPacket header = PacketUtil.getHeader(FIELD_COUNT);
            //        byte packetId = header.packetId;
            //        buffer = header.write(buffer, c, true);
            //
            //        // write fields
            //        for (FieldPacket field : fields) {
            //            field.packetId = ++packetId;
            //            buffer = field.write(buffer, c, true);
            //        }
            //
            //        // write eof
            //        EOFPacket eof = new EOFPacket();
            //        eof.packetId = ++packetId;
            //        buffer = eof.write(buffer, c, true);
            //
            //        // write rows
            //        RouteResultsetNode[] rrsn = rrs.getNodes();
            //        for (RouteResultsetNode node : rrsn) {
            //            RowDataPacket row = getRow(node, c.getCharset());
            //            row.packetId = ++packetId;
            //            buffer = row.write(buffer, c, true);
            //        }
            //
            //        // write last eof
            //        EOFPacket lastEof = new EOFPacket();
            //        lastEof.packetId = ++packetId;
            //        buffer = lastEof.write(buffer, c, true);
            //
            //        // post write
            //        c.write(buffer);

        }

        //    private static RowDataPacket getRow(RouteResultsetNode node, String charset) {
        //        RowDataPacket row = new RowDataPacket(FIELD_COUNT);
        //        row.add(StringUtil.encode(node.getName(), charset));
        //        row.add(StringUtil.encode(node.getStatement().replaceAll("[\\t\\n\\r]", " "), charset));
        //        return row;
        //    }

        //    private static boolean isMycatSeq(String stmt, SchemaConfig schema) {
        //        if (pattern.matcher(stmt).find()) {
        //            return true;
        //        }
        //        SQLStatementParser parser = new MySqlStatementParser(stmt);
        //        MySqlInsertStatement statement = (MySqlInsertStatement) parser.parseStatement();
        //        String tableName = statement.getTableName().getSimpleName();
        //        TableConfig tableConfig = schema.getTables().get(tableName.toUpperCase());
        //        if (tableConfig == null) {
        //            return false;
        //        }
        //        if (tableConfig.isAutoIncrement()) {
        //            boolean isHasIdInSql = false;
        //            String primaryKey = tableConfig.getPrimaryKey();
        //            List<SQLExpr> columns = statement.getColumns();
        //            for (SQLExpr column : columns) {
        //                String columnName = column.toString();
        //                if (primaryKey.equalsIgnoreCase(columnName)) {
        //                    isHasIdInSql = true;
        //                    break;
        //                }
        //            }
        //            if (!isHasIdInSql) {
        //                return true;
        //            }
        //        }
        //
        //
        //        return false;
        //    }

        fun handle(x: SQLExplainStatement, connection: OConnection) {
            connection.writeErrMessage(ErrorCode.ER_CHECK_NO_SUCH_TABLE, "not soupot")
        }
    }
}
