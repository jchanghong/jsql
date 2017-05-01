package io.jsql.orientserver.handler.data_mannipulation;

import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlLoadXmlStatement;
import io.jsql.config.ErrorCode;
import io.jsql.orientserver.OConnection;
import io.jsql.storage.DBAdmin;
import io.jsql.storage.MException;

/**
 * Created by 长宏 on 2017/3/18 0018.
 * mysql> LOAD XML LOCAL INFILE 'person.xml'
 * ->   INTO TABLE person
 * ->   ROWS IDENTIFIED BY '<person>';
 * <p>
 * Query OK, 8 rows affected (0.00 sec)
 * Records: 8  Deleted: 0  Skipped: 0  Warnings: 0
 */
public class Mloadxml {
    public static void handle(MySqlLoadXmlStatement x, OConnection connection) {

        if (DBAdmin.currentDB == null) {
            connection.writeErrMessage(ErrorCode.ER_NO_DB_ERROR, "没有选择数据库");
        }
        try {
            OConnection.DB_ADMIN.exesqlforResult(x.toString(), DBAdmin.currentDB);
            connection.writeok();
        } catch (MException e) {
            e.printStackTrace();
            connection.writeErrMessage(e.getMessage());
        }
    }
}
