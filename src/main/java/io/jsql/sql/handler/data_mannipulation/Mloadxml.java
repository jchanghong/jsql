package io.jsql.sql.handler.data_mannipulation;

import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlLoadXmlStatement;
import io.jsql.config.ErrorCode;
import io.jsql.sql.OConnection;
import io.jsql.sql.handler.SqlStatementHander;
import io.jsql.storage.DB;
import io.jsql.storage.StorageException;
import org.springframework.stereotype.Component;

/**
 * Created by 长宏 on 2017/3/18 0018.
 * mysql> LOAD XML LOCAL INFILE 'person.xml'
 * ->   INTO TABLE person
 * ->   ROWS IDENTIFIED BY '<person>';
 * <p>
 * Query OK, 8 rows affected (0.00 sec)
 * Records: 8  Deleted: 0  Skipped: 0  Warnings: 0
 */
@Component
public class Mloadxml extends SqlStatementHander{
    public static void handle(MySqlLoadXmlStatement x, OConnection connection) {

        if (connection.schema == null) {
            connection.writeErrMessage(ErrorCode.ER_NO_DB_ERROR, "没有选择数据库");
        }
        try {
            OConnection.DB_ADMIN.exesqlforResult(x.toString(), connection.schema);
            connection.writeok();
        } catch (StorageException e) {
            e.printStackTrace();
            connection.writeErrMessage(e.getMessage());
        }
    }

    @Override
    public Class<? extends SQLStatement> supportSQLstatement() {
        return MySqlLoadXmlStatement.class;
    }

    @Override
    protected Object handle(SQLStatement sqlStatement) throws Exception {
        if (c.schema == null) {
            return "没有选择数据库";
        }
        return OConnection.DB_ADMIN.exesqlforResult(sqlStatement.toString(), c.schema);
    }
}
