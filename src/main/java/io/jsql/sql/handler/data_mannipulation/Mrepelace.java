package io.jsql.sql.handler.data_mannipulation;

import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlReplaceStatement;
import com.orientechnologies.orient.core.db.document.ODatabaseDocument;
import com.orientechnologies.orient.core.metadata.schema.OClass;
import io.jsql.config.ErrorCode;
import io.jsql.mysql.mysql.OkPacket;
import io.jsql.sql.OConnection;
import io.jsql.sql.handler.SqlStatementHander;
import io.jsql.storage.StorageException;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by 长宏 on 2017/3/18 0018.
 * mysql> REPLACE INTO test VALUES (1, 'Old', '2014-08-20 18:47:00');
 * Query OK, 1 row affected (0.04 sec)
 * <p>
 * mysql> REPLACE INTO test VALUES (1, 'New', '2014-08-20 18:47:42');
 * Query OK, 2 rows affected (0.04 sec)
 */
@Component
public class Mrepelace extends SqlStatementHander{
    public static void handle(MySqlReplaceStatement x, OConnection connection) {
        if (connection.schema == null) {
            connection.writeErrMessage(ErrorCode.ER_NO_DB_ERROR, "没有选择数据库");
        }
        ODatabaseDocument getdbtx;
        try {
            getdbtx = OConnection.DB_ADMIN.getdb(connection.schema);
        } catch (StorageException e) {
            e.printStackTrace();
            connection.writeErrMessage(e.getMessage());
            return;
        }
        try {
            String table = x.getTableName().toString();
            getdbtx.activateOnCurrentThread();
            OClass oClass = OConnection.TABLE_ADMIN.gettableclass(table, connection.schema);
            Set<String> sets = new HashSet<>();
            oClass.properties().forEach(a -> sets.add(a.getName()));
            StringBuilder builder = new StringBuilder();
            builder.append(table).append("(");
            sets.forEach(a -> builder.append(a + ","));
            builder.deleteCharAt(builder.length() - 1);
            String sql = x.toString().replace(table, builder.toString());
            Object o = OConnection.DB_ADMIN.exesqlforResult(sql, connection.schema);
//            getdbtx.close();
            OConnection.DB_ADMIN.close(getdbtx);
            if (o instanceof Number) {
                OkPacket okPacket = new OkPacket();
                okPacket.read(OkPacket.OK);
                okPacket.affectedRows = (long) o;
                okPacket.write(connection.channelHandlerContext.channel());
                return;
            }
            connection.writeok();
        } catch (StorageException e) {
            e.printStackTrace();
            connection.writeErrMessage(e.getMessage());
        }
    }

    @Override
    public Class<? extends SQLStatement> supportSQLstatement() {
        return MySqlReplaceStatement.class;
    }

    @Override
    protected Object handle(SQLStatement sqlStatement) throws Exception {
        if (c.schema == null) {
            return "没有选择数据库";
        }
        MySqlReplaceStatement x = (MySqlReplaceStatement) sqlStatement;
        ODatabaseDocument getdbtx;
            getdbtx = OConnection.DB_ADMIN.getdb(c.schema);
            String table = x.getTableName().toString();
            getdbtx.activateOnCurrentThread();
            OClass oClass = OConnection.TABLE_ADMIN.gettableclass(table, c.schema);
            Set<String> sets = new HashSet<>();
            oClass.properties().forEach(a -> sets.add(a.getName()));
            StringBuilder builder = new StringBuilder();
            builder.append(table).append("(");
            sets.forEach(a -> builder.append(a + ","));
            builder.deleteCharAt(builder.length() - 1);
            String sql = x.toString().replace(table, builder.toString());
            Object o = OConnection.DB_ADMIN.exesqlforResult(sql, c.schema);
            getdbtx.close();

        return o;
    }
}
