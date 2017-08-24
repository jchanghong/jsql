/*
 * Java-based distributed database like Mysql
 */

package io.jsql.sql.handler.data_mannipulation

import com.alibaba.druid.sql.ast.SQLStatement
import com.alibaba.druid.sql.ast.statement.SQLUpdateStatement
import io.jsql.config.ErrorCode
import io.jsql.mysql.mysql.OkPacket
import io.jsql.sql.OConnection
import io.jsql.sql.handler.SqlStatementHander
import io.jsql.storage.StorageException
import org.springframework.stereotype.Component

/**
 * Created by 长宏 on 2017/3/18 0018.
 * UPDATE t1 SET col1 = col1 + 1;
 */
@Component
class Mupdate : SqlStatementHander() {

    override fun supportSQLstatement(): Class<out SQLStatement> {
        return SQLUpdateStatement::class.java
    }

    @Throws(Exception::class)
    override fun handle0(sqlStatement: SQLStatement, c: OConnection): Any? {
        if (c!!.schema == null) {
            return "没有选择数据库"
        }
        return OConnection.DB_ADMIN!!.exesqlforResult(sqlStatement.toString(), c!!.schema!!)
    }

    companion object {
        fun handle(x: SQLUpdateStatement, connection: OConnection) {
            if (connection.schema == null) {
                connection.writeErrMessage(ErrorCode.ER_NO_DB_ERROR, "没有选择数据库")
            }
            try {
                val o = OConnection.DB_ADMIN!!.exesqlforResult(x.toString(), connection.schema!!)
                val okPacket = OkPacket()
                okPacket.read(OkPacket.OK)
                okPacket.affectedRows = o as Long
                okPacket.write(connection.channelHandlerContext!!.channel())

            } catch (e: StorageException) {
                e.printStackTrace()
                connection.writeErrMessage(e.message!!)
            }

        }
    }
}
