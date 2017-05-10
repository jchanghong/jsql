package io.jsql.sql.handler.data_mannipulation

import com.alibaba.druid.sql.ast.SQLStatement
import com.alibaba.druid.sql.ast.statement.SQLInsertStatement
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlInsertStatement
import io.jsql.config.ErrorCode
import io.jsql.mysql.mysql.OkPacket
import io.jsql.sql.OConnection
import io.jsql.sql.handler.SqlStatementHander
import io.jsql.storage.DB
import io.jsql.storage.StorageException
import org.springframework.stereotype.Component

/**
 * Created by 长宏 on 2017/3/18 0018.
 * INSERT INTO tbl_name (a,b,c) VALUES(1,2,3),(4,5,6),(7,8,9);
 * The values list for each row must be enclosed within parentheses.
 * The following statement is illegal because the number of
 * values in the list does not match the number of column names:
 *
 *
 * INSERT INTO tbl_name (a,b,c) VALUES(1,2,3,4,5,6,7,8,9);
 */
@Component
class Minsert : SqlStatementHander() {

    override fun supportSQLstatement(): Class<out SQLStatement> {
        return MySqlInsertStatement::class.java
    }

    @Throws(Exception::class)
    override fun handle(sqlStatement: SQLStatement): Any? {
        if (c!!.schema == null) {
            return "没有选择数据库"
        }
        return OConnection.DB_ADMIN!!.exesqlforResult(sqlStatement.toString(), c!!.schema!!)
    }

    companion object {
        fun handle(x: SQLInsertStatement, connection: OConnection) {
            if (connection.schema == null) {
                connection.writeErrMessage(ErrorCode.ER_NO_DB_ERROR, "没有选择数据库")
            }
            try {
                val o = OConnection.DB_ADMIN!!.exesqlforResult(x.toString(), connection.schema!!)
                if (o is Number) {
                    val okPacket = OkPacket()
                    okPacket.read(OkPacket.OK)
                    okPacket.affectedRows = o as Long
                    okPacket.write(connection.channelHandlerContext!!.channel())
                    connection.writeok()
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
