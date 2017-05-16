package io.jsql.sql.handler.data_mannipulation

import com.alibaba.druid.sql.ast.SQLStatement
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlLoadDataInFileStatement
import io.jsql.config.ErrorCode
import io.jsql.sql.OConnection
import io.jsql.sql.handler.SqlStatementHander
import io.jsql.storage.StorageException
import org.springframework.stereotype.Component

/**
 * Created by 长宏 on 2017/3/18 0018.
 * LOAD DATA INFILE 'data.txt' INTO TABLE db2.my_table;
 */
@Component
class MloaddataINfile : SqlStatementHander() {

    override fun supportSQLstatement(): Class<out SQLStatement> {
        return MySqlLoadDataInFileStatement::class.java
    }

    @Throws(Exception::class)
    override fun handle0(sqlStatement: SQLStatement, c: OConnection): Any? {
        if (c!!.schema == null) {
            return "没有选择数据库"
        }

        return OConnection.DB_ADMIN!!.exesqlforResult(sqlStatement.toString(), c!!.schema!!)

    }

    companion object {
        fun handle(x: MySqlLoadDataInFileStatement, connection: OConnection) {

            if (connection.schema == null) {
                connection.writeErrMessage(ErrorCode.ER_NO_DB_ERROR, "没有选择数据库")
            }
            try {
                OConnection.DB_ADMIN!!.exesqlforResult(x.toString(), connection.schema!!)
                connection.writeok()
            } catch (e: StorageException) {
                e.printStackTrace()
                connection.writeErrMessage(e.message!!)
            }

        }
    }
}
