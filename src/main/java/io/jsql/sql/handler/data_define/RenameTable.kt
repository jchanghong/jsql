/*
 * Java-based distributed database like Mysql
 */

package io.jsql.sql.handler.data_define

import com.orientechnologies.orient.core.db.document.ODatabaseDocument
import io.jsql.sql.OConnection
import io.jsql.sql.handler.SqlStatementHander
import io.jsql.storage.StorageException
import org.springframework.stereotype.Component
import java.util.*

/**
 * Created by 长宏 on 2017/3/18 0018.
 * For example, a table named old_table can be renamed to new_table as shown here:
 *
 *
 * RENAME TABLE old_table TO new_table;
 * This statement is equivalent to the following ALTER TABLE statement:
 *
 *
 * ALTER TABLE old_table RENAME new_table;
 *
 *
 *
 *
 * --------------------------
 * Update the class name from Account to Seller :
 * orientdb> ALTER CLASS Account NAME Seller
 */
@Component
class RenameTable : SqlStatementHander() {

    override fun supportSQLstatement(): Class<out SQLStatement> {
        return MySqlRenameTableStatement::class.java
    }

    @Throws(Exception::class)
    override fun handle0(sqlStatement: SQLStatement, c: OConnection): Any? {
        if (c!!.schema == null) {
            return "没有选择数据库"
        }
        val tx = OConnection.DB_ADMIN!!.getdb(c!!.schema!!)
        tx.activateOnCurrentThread()
        val x = sqlStatement as MySqlRenameTableStatement
        val oldname = x.items[0].name.toString()
        val newname = x.items[0].to.toString()
        if (!tx.metadata.schema.existsClass(oldname)) {
            return "表不存在"
        }
        val builder = StringBuilder()
        builder.append("ALTER CLASS ")
        builder.append(oldname)
        builder.append("  NAME ")
        builder.append(newname)
        map.clear()
        OConnection.DB_ADMIN!!.exesqlNoResultAsyn(builder.toString(), c!!.schema!!)
        return null
    }

    companion object {
        internal val map: MutableMap<String, StorageException> = HashMap()

        fun handle(x: MySqlRenameTableStatement, connection: OConnection) {
            if (connection.schema == null) {
                connection.writeErrMessage("没有选择数据库")
            }
            val tx: ODatabaseDocument
            try {
                tx = OConnection.DB_ADMIN!!.getdb(connection.schema!!)
            } catch (e: StorageException) {
                e.printStackTrace()
                connection.writeErrMessage(e.message!!)
                return
            }

            tx.activateOnCurrentThread()
            val oldname = x.items[0].name.toString()
            val newname = x.items[0].to.toString()
            if (!tx.metadata.schema.existsClass(oldname)) {
                connection.writeErrMessage("表不存在")
                return
            }
            val builder = StringBuilder()
            builder.append("ALTER CLASS ")
            builder.append(oldname)
            builder.append("  NAME ")
            builder.append(newname)
            map.clear()
            OConnection.DB_ADMIN!!.exesqlNoResultAsyn(builder.toString(), connection.schema!!)
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
            connection.writeok()
        }
    }
}
