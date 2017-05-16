package io.jsql.sql.handler.data_mannipulation

import com.alibaba.druid.sql.ast.SQLStatement
import com.alibaba.druid.sql.ast.statement.SQLDeleteStatement
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlDeleteStatement
import io.jsql.config.ErrorCode
import io.jsql.mysql.mysql.OkPacket
import io.jsql.sql.OConnection
import io.jsql.sql.handler.SqlStatementHander
import io.jsql.storage.StorageException
import org.springframework.stereotype.Component

/**
 * Created by 长宏 on 2017/3/18 0018.
 * DELETE FROM somelog WHERE user = 'jcole'
 * ORDER BY timestamp_column LIMIT 1;
 */
@Component
class Mdelete : SqlStatementHander() {
    override fun supportSQLstatement(): Class<out SQLStatement> {
        return MySqlDeleteStatement::class.java
    }

    @Throws(Exception::class)
    override fun handle0(sqlStatement: SQLStatement, c: OConnection): Any? {
        if (c!!.schema == null) {
            return "没有选择数据库"
        }
        return OConnection.DB_ADMIN!!.exesqlforResult(sqlStatement.toString(), c!!.schema!!)

    }

    companion object {

        fun handle(x: SQLDeleteStatement, connection: OConnection) {
            if (connection.schema == null) {
                connection.writeErrMessage(ErrorCode.ER_NO_DB_ERROR, "没有选择数据库")
                return
            }
            try {
                val o = OConnection.DB_ADMIN!!.exesqlforResult(x.toString(), connection.schema!!)
                if (o is Number) {
                    val okPacket = OkPacket()
                    okPacket.read(OkPacket.OK)
                    okPacket.affectedRows = java.lang.Long.parseLong(o.toString())
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
