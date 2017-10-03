/*
 * Java-based distributed database like Mysql
 */

package io.jsql.sql.handler.data_define

import io.jsql.config.ErrorCode
import io.jsql.sql.OConnection
import io.jsql.sql.handler.SqlStatementHander
import io.jsql.storage.StorageException
import org.springframework.stereotype.Component

/**
 * Created by 长宏 on 2017/2/26 0026.
 */
@Component
class CreateTable : SqlStatementHander() {

    override fun supportSQLstatement(): Class<out SQLStatement> {
        return MySqlCreateTableStatement::class.java
    }

    @Throws(Exception::class)
    override fun handle0(sqlStatement: SQLStatement, c: OConnection): Any? {
        if (c!!.schema == null) {
            return "no database selected!!"
        }
        val x = sqlStatement as MySqlCreateTableStatement
        OConnection.TABLE_ADMIN!!.createtableSyn(c!!.schema!!, x)
        return null
    }

    companion object {
        fun handle(x: MySqlCreateTableStatement, c: OConnection) {
            if (c.schema == null) {
                c.writeErrMessage(ErrorCode.ER_NO_DB_ERROR, "no database selected!!")
                return
            }
            try {
                OConnection.TABLE_ADMIN!!.createtableSyn(c.schema!!, x)
                c.writeok()
            } catch (e: StorageException) {
                e.printStackTrace()
                c.writeErrMessage(e.message!!)
            }

        }

        fun handle(connection: OConnection, x: SQLCreateTableStatement) {


        }
    }
}
