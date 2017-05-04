package io.jsql.sql.handler.data_mannipulation;

import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.SQLInsertStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlInsertStatement;
import io.jsql.config.ErrorCode;
import io.jsql.mysql.mysql.OkPacket;
import io.jsql.sql.OConnection;
import io.jsql.sql.handler.SqlStatementHander;
import io.jsql.storage.DB;
import io.jsql.storage.StorageException;
import org.springframework.stereotype.Component;

/**
 * Created by 长宏 on 2017/3/18 0018.
 * INSERT INTO tbl_name (a,b,c) VALUES(1,2,3),(4,5,6),(7,8,9);
 * The values list for each row must be enclosed within parentheses.
 * The following statement is illegal because the number of
 * values in the list does not match the number of column names:
 * <p>
 * INSERT INTO tbl_name (a,b,c) VALUES(1,2,3,4,5,6,7,8,9);
 */
@Component
public class Minsert extends SqlStatementHander{
    public static void handle(SQLInsertStatement x, OConnection connection) {
        if (connection.schema == null) {
            connection.writeErrMessage(ErrorCode.ER_NO_DB_ERROR, "没有选择数据库");
        }
        try {
            Object o = OConnection.DB_ADMIN.exesqlforResult(x.toString(), connection.schema);
            if (o instanceof Number) {
                OkPacket okPacket = new OkPacket();
                okPacket.read(OkPacket.OK);
                okPacket.affectedRows = (long) o;
                okPacket.write(connection.channelHandlerContext.channel());
                connection.writeok();
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
        return MySqlInsertStatement.class;
    }

    @Override
    protected Object handle(SQLStatement sqlStatement) throws Exception {
        if (c.schema == null) {
            return "没有选择数据库";
        }
        return OConnection.DB_ADMIN.exesqlforResult(sqlStatement.toString(), c.schema);
    }
}
