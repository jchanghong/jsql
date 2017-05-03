package io.jsql.sql.handler.data_define;

import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlRenameTableStatement;
import com.orientechnologies.orient.core.db.document.ODatabaseDocument;
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import io.jsql.mysql.handler.SQLHander;
import io.jsql.sql.OConnection;
import io.jsql.sql.handler.SqlStatementHander;
import io.jsql.storage.DB;
import io.jsql.storage.StorageException;
import javafx.scene.control.ChoiceBox;
import org.springframework.stereotype.Component;

import javax.swing.text.rtf.RTFEditorKit;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 长宏 on 2017/3/18 0018.
 * For example, a table named old_table can be renamed to new_table as shown here:
 * <p>
 * RENAME TABLE old_table TO new_table;
 * This statement is equivalent to the following ALTER TABLE statement:
 * <p>
 * ALTER TABLE old_table RENAME new_table;
 * <p>
 * <p>
 * --------------------------
 * Update the class name from Account to Seller :
 * orientdb> ALTER CLASS Account NAME Seller
 */
@Component
public class RenameTable extends SqlStatementHander{
    static final Map<String, StorageException> map = new HashMap<>();

    public static void handle(MySqlRenameTableStatement x, OConnection connection) {
        if (connection.schema == null) {
            connection.writeErrMessage("没有选择数据库");
        }
        ODatabaseDocument tx;
        try {
            tx = OConnection.DB_ADMIN.getdb(connection.schema);
        } catch (StorageException e) {
            e.printStackTrace();
            connection.writeErrMessage(e.getMessage());
            return;
        }
        tx.activateOnCurrentThread();
        String oldname = x.getItems().get(0).getName().toString();
        String newname = x.getItems().get(0).getTo().toString();
        if (!tx.getMetadata().getSchema().existsClass(oldname)) {
            connection.writeErrMessage("表不存在");
            return;
        }
        StringBuilder builder = new StringBuilder();
        builder.append("ALTER CLASS ");
        builder.append(oldname);
        builder.append("  NAME ");
        builder.append(newname);
        map.clear();
        OConnection.DB_ADMIN.exesqlNoResultAsyn(builder.toString(), connection.schema);
//        ODB.executor.execute(() -> {
//            try {
//                ODB.exesql(builder.toString(), Oc.schema);
//            } catch (StorageException e) {
//                e.printStackTrace();
//                map.put("k", e);
//            }
//        });
//        if (map.size() > 0) {
//            connection.writeErrMessage(map.get("k").getMessage());
//            return;
//        }
        connection.writeok();
    }

    @Override
    public Class<? extends SQLStatement> supportSQLstatement() {
        return MySqlRenameTableStatement.class;
    }

    @Override
    protected Object handle(SQLStatement sqlStatement) throws Exception {
        if (c.schema == null) {
            return "没有选择数据库";
        }
        ODatabaseDocument
                tx = OConnection.DB_ADMIN.getdb(c.schema);
        tx.activateOnCurrentThread();
        MySqlRenameTableStatement x = (MySqlRenameTableStatement) sqlStatement;
        String oldname = x.getItems().get(0).getName().toString();
        String newname = x.getItems().get(0).getTo().toString();
        if (!tx.getMetadata().getSchema().existsClass(oldname)) {
            return "表不存在";
        }
        StringBuilder builder = new StringBuilder();
        builder.append("ALTER CLASS ");
        builder.append(oldname);
        builder.append("  NAME ");
        builder.append(newname);
        map.clear();
        OConnection.DB_ADMIN.exesqlNoResultAsyn(builder.toString(), c.schema);
        return null;
    }
}
