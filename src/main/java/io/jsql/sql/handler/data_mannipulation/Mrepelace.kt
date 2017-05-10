package io.jsql.sql.handler.data_mannipulation

import com.alibaba.druid.sql.ast.SQLStatement
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlReplaceStatement
import com.orientechnologies.orient.core.db.document.ODatabaseDocument
import com.orientechnologies.orient.core.metadata.schema.OClass
import io.jsql.config.ErrorCode
import io.jsql.mysql.mysql.OkPacket
import io.jsql.sql.OConnection
import io.jsql.sql.handler.SqlStatementHander
import io.jsql.storage.StorageException
import org.springframework.stereotype.Component

import java.util.HashSet

/**
 * Created by 长宏 on 2017/3/18 0018.
 * mysql> REPLACE INTO test VALUES (1, 'Old', '2014-08-20 18:47:00');
 * Query OK, 1 row affected (0.04 sec)
 *
 *
 * mysql> REPLACE INTO test VALUES (1, 'New', '2014-08-20 18:47:42');
 * Query OK, 2 rows affected (0.04 sec)
 */
@Component
class Mrepelace : SqlStatementHander() {

    override fun supportSQLstatement(): Class<out SQLStatement> {
        return MySqlReplaceStatement::class.java
    }

    @Throws(Exception::class)
    override fun handle(sqlStatement: SQLStatement): Any? {
        if (c!!.schema == null) {
            return "没有选择数据库"
        }
        val x = sqlStatement as MySqlReplaceStatement
        val getdbtx: ODatabaseDocument
        getdbtx = OConnection.DB_ADMIN!!.getdb(c!!.schema!!)
        val table = x.tableName.toString()
        getdbtx.activateOnCurrentThread()
        val oClass = OConnection.TABLE_ADMIN!!.gettableclass(table, c!!.schema!!)
        val sets = HashSet<String>()
        oClass.properties().forEach { a -> sets.add(a.name) }
        val builder = StringBuilder()
        builder.append(table).append("(")
        sets.forEach { a -> builder.append(a + ",") }
        builder.deleteCharAt(builder.length - 1)
        val sql = x.toString().replace(table, builder.toString())
        val o = OConnection.DB_ADMIN!!.exesqlforResult(sql, c!!.schema!!)
        getdbtx.close()

        return o
    }

    companion object {
        fun handle(x: MySqlReplaceStatement, connection: OConnection) {
            if (connection.schema == null) {
                connection.writeErrMessage(ErrorCode.ER_NO_DB_ERROR, "没有选择数据库")
            }
            val getdbtx: ODatabaseDocument
            try {
                getdbtx = OConnection.DB_ADMIN!!.getdb(connection.schema!!)
            } catch (e: StorageException) {
                e.printStackTrace()
                connection.writeErrMessage(e.message!!)
                return
            }

            try {
                val table = x.tableName.toString()
                getdbtx.activateOnCurrentThread()
                val oClass = OConnection.TABLE_ADMIN!!.gettableclass(table, connection.schema!!)
                val sets = HashSet<String>()
                oClass.properties().forEach { a -> sets.add(a.name) }
                val builder = StringBuilder()
                builder.append(table).append("(")
                sets.forEach { a -> builder.append(a + ",") }
                builder.deleteCharAt(builder.length - 1)
                val sql = x.toString().replace(table, builder.toString())
                val o = OConnection.DB_ADMIN!!.exesqlforResult(sql, connection.schema!!)
                //            getdbtx.close();
                OConnection.DB_ADMIN!!.close(getdbtx)
                if (o is Number) {
                    val okPacket = OkPacket()
                    okPacket.read(OkPacket.OK)
                    okPacket.affectedRows = o as Long
                    okPacket.write(connection.channelHandlerContext!!.channel())
                    return
                }
                connection.writeok()
            } catch (e: StorageException) {
                e.printStackTrace()
                connection.writeErrMessage(e.message!!)
            }

        }
    }
}
