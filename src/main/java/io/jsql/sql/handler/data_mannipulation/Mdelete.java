package io.jsql.sql.handler.data_mannipulation;

import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.SQLDeleteStatement;
import io.jsql.config.ErrorCode;
import io.jsql.mysql.mysql.OkPacket;
import io.jsql.sql.OConnection;
import io.jsql.sql.handler.SqlStatementHander;
import io.jsql.storage.DB;
import io.jsql.storage.StorageException;
import org.aspectj.apache.bcel.generic.RET;
import org.springframework.stereotype.Component;

/**
 * Created by 长宏 on 2017/3/18 0018.
 * DELETE FROM somelog WHERE user = 'jcole'
 * ORDER BY timestamp_column LIMIT 1;
 */
@Component
public class Mdelete extends SqlStatementHander{
    @Override
    public Class<? extends SQLStatement> supportSQLstatement() {
        return SQLDeleteStatement.class;
    }

    @Override
    protected Object handle(SQLStatement sqlStatement) throws Exception {
        if (c.schema == null) {
            return "没有选择数据库";
        }
        return OConnection.DB_ADMIN.exesqlforResult(sqlStatement.toString(), c.schema);

    }

    public static void handle(SQLDeleteStatement x, OConnection connection) {
        if (connection.schema == null) {
            connection.writeErrMessage(ErrorCode.ER_NO_DB_ERROR, "没有选择数据库");
            return;
        }
        try {
            Object o = OConnection.DB_ADMIN.exesqlforResult(x.toString(), connection.schema);
            if (o instanceof Number) {
                OkPacket okPacket = new OkPacket();
                okPacket.read(OkPacket.OK);
                okPacket.affectedRows = Long.parseLong(o.toString());
                okPacket.write(connection.channelHandlerContext.channel());
                return;
            }
            connection.writeok();
        } catch (StorageException e) {
            e.printStackTrace();
            connection.writeErrMessage(e.getMessage());
        }
    }
}
