package io.jsql.sql.handler.utilstatement

import com.alibaba.druid.sql.ast.statement.SQLUseStatement
import io.jsql.config.ErrorCode
import io.jsql.sql.OConnection
import io.jsql.storage.DB
import io.jsql.storage.StorageException
import io.jsql.util.StringUtil

/**
 * Created by 长宏 on 2017/2/26 0026.
 */
object Usedatabase {
    fun handle(x: SQLUseStatement, connection: OConnection) {
        var schema = x.database.toString()
        var length = schema.length
        if (length > 0) {
            if (schema.endsWith(";")) {
                schema = schema.substring(0, schema.length - 1)
            }
            schema = schema.replace("`","")
            length = schema.length
            if (schema[0] == '\'' && schema[length - 1] == '\'') {
                schema = schema.substring(1, length - 1)
            }
        }
        // 检查schema的有效性
        try {
            if (!OConnection.DB_ADMIN!!.getallDBs().contains(schema)) {
                connection.writeErrMessage(ErrorCode.ER_BAD_DB_ERROR, "Unknown database '$schema'")
                return
            }
        } catch (e: StorageException) {
            e.printStackTrace()
            connection.writeErrMessage(e.message!!)
            return
        }

        connection.writeok()
    }
}
