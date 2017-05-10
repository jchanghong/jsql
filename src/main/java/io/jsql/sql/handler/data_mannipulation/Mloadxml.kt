package io.jsql.sql.handler.data_mannipulation

import com.alibaba.druid.sql.ast.SQLStatement
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlLoadXmlStatement
import io.jsql.config.ErrorCode
import io.jsql.sql.OConnection
import io.jsql.sql.handler.SqlStatementHander
import io.jsql.storage.StorageException
import org.springframework.stereotype.Component

/**
 * Created by 长宏 on 2017/3/18 0018.
 * mysql> LOAD XML LOCAL INFILE 'person.xml'
 * ->   INTO TABLE person
 * ->   ROWS IDENTIFIED BY '<person>';
 *
 *
 * Query OK, 8 rows affected (0.00 sec)
 * Records: 8  Deleted: 0  Skipped: 0  Warnings: 0
</person> */
@Component
class Mloadxml : SqlStatementHander() {

    override fun supportSQLstatement(): Class<out SQLStatement> {
        return MySqlLoadXmlStatement::class.java
    }

    @Throws(Exception::class)
    override fun handle(sqlStatement: SQLStatement): Any? {
        if (c!!.schema == null) {
            return "没有选择数据库"
        }
        return OConnection.DB_ADMIN!!.exesqlforResult(sqlStatement.toString(), c!!.schema!!)
    }

    companion object {
        fun handle(x: MySqlLoadXmlStatement, connection: OConnection) {

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
