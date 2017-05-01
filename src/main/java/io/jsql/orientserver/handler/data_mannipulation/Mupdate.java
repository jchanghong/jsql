package io.jsql.orientserver.handler.data_mannipulation;

import com.alibaba.druid.sql.ast.statement.SQLUpdateStatement;
import io.jsql.config.ErrorCode;
import io.jsql.databaseorient.adapter.MDBadapter;
import io.jsql.databaseorient.adapter.MException;
import io.jsql.mysql.mysql.OkPacket;
import io.jsql.orientserver.OConnection;

/**
 * Created by 长宏 on 2017/3/18 0018.
 * UPDATE t1 SET col1 = col1 + 1;
 */
public class Mupdate {
    public static void handle(SQLUpdateStatement x, OConnection connection) {
        if (MDBadapter.currentDB == null) {
            connection.writeErrMessage(ErrorCode.ER_NO_DB_ERROR, "没有选择数据库");
        }
        try {
            Object o = MDBadapter.exesql(x.toString(), MDBadapter.currentDB);
            OkPacket okPacket = new OkPacket();
            okPacket.read(OkPacket.OK);
            okPacket.affectedRows = (long) o;
            okPacket.write(connection.channelHandlerContext.channel());

        } catch (MException e) {
            e.printStackTrace();
            connection.writeErrMessage(e.getMessage());
        }
    }
}
