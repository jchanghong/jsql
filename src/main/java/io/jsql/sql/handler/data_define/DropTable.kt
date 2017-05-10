package io.jsql.sql.handler.data_define

import com.alibaba.druid.sql.ast.SQLStatement
import com.alibaba.druid.sql.ast.statement.SQLDropTableStatement
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource
import io.jsql.config.ErrorCode
import io.jsql.sql.OConnection
import io.jsql.sql.handler.SqlStatementHander
import io.jsql.storage.DB
import io.jsql.storage.StorageException
import org.springframework.stereotype.Component

/**
 * Created by 长宏 on 2017/3/18 0018.
 */
@Component
class DropTable : SqlStatementHander() {

    override fun supportSQLstatement(): Class<out SQLStatement> {
        return SQLDropTableStatement::class.java
    }

    @Throws(Exception::class)
    override fun handle(sqlStatement: SQLStatement): Any? {
        if (c!!.schema == null) {
            return "没有选择数据库"
        }
        val x = sqlStatement as SQLDropTableStatement
        for (sqlExprTableSource in x.tableSources) {
            OConnection.TABLE_ADMIN!!.droptableSyn(c!!.schema!!, sqlExprTableSource.toString())
        }
        return null
    }

    companion object {
        fun handle(x: SQLDropTableStatement, connection: OConnection) {
            if (connection.schema == null) {
                connection.writeErrMessage(ErrorCode.ER_NO_DB_ERROR, "没有选择数据库")
            }
            x.tableSources.forEach { table ->
                try {
                    OConnection.TABLE_ADMIN!!.droptableSyn(connection.schema!!, table.toString())
                } catch (e: StorageException) {
                    e.printStackTrace()
                    connection.writeErrMessage(e.message!!)
                }
            }
            connection.writeok()
        }
    }
}
