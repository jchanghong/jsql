package io.jsql.sql.handler.data_mannipulation;

import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.SQLUpdateStatement;
import io.jsql.config.ErrorCode;
import io.jsql.mysql.mysql.OkPacket;
import io.jsql.sql.OConnection;
import io.jsql.sql.handler.SqlStatementHander;
import io.jsql.storage.DB;
import io.jsql.storage.StorageException;
import org.springframework.stereotype.Component;

/**
 * Created by 长宏 on 2017/3/18 0018.
 * UPDATE t1 SET col1 = col1 + 1;
 */
@Component
public class Mupdate extends SqlStatementHander{
    public static void handle(SQLUpdateStatement x, OConnection connection) {
        if (connection.schema == null) {
            connection.writeErrMessage(ErrorCode.ER_NO_DB_ERROR, "没有选择数据库");
        }
        try {
            Object o = OConnection.DB_ADMIN.exesqlforResult(x.toString(), connection.schema);
            OkPacket okPacket = new OkPacket();
            okPacket.read(OkPacket.OK);
            okPacket.affectedRows = (long) o;
            okPacket.write(connection.channelHandlerContext.channel());

        } catch (StorageException e) {
            e.printStackTrace();
            connection.writeErrMessage(e.getMessage());
        }
    }

    @Override
    public Class<? extends SQLStatement> supportSQLstatement() {
        return SQLUpdateStatement.class;
    }

    @Override
    protected Object handle(SQLStatement sqlStatement) throws Exception {
        if (c.schema == null) {
            return "没有选择数据库";
        }
        return OConnection.DB_ADMIN.exesqlforResult(sqlStatement.toString(), c.schema);
    }
}
