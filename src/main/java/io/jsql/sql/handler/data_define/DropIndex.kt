/*
 * Java-based distributed database like Mysql
 */

package io.jsql.sql.handler.data_define

import com.alibaba.druid.sql.ast.SQLStatement
import com.alibaba.druid.sql.ast.statement.SQLDropIndexStatement
import com.orientechnologies.orient.core.db.document.ODatabaseDocument
import com.orientechnologies.orient.core.metadata.schema.OProperty
import io.jsql.sql.OConnection
import io.jsql.sql.handler.SqlStatementHander
import io.jsql.storage.StorageException
import org.springframework.stereotype.Component

/**
 * Created by 长宏 on 2017/3/18 0018.
 */
@Component
class DropIndex : SqlStatementHander() {

    override fun supportSQLstatement(): Class<out SQLStatement> {
        return SQLDropIndexStatement::class.java
    }

    @Throws(Exception::class)
    override fun handle0(sqlStatement: SQLStatement, c: OConnection): Any? {
        var sql = sqlStatement as SQLDropIndexStatement
        var index = sql.indexName
        var table = sql.tableName
        val documentTx: ODatabaseDocument
        try {
            documentTx = OConnection.DB_ADMIN!!.getdb(c.schema!!)
        } catch (e: StorageException) {
            e.printStackTrace()
            return e.message!!
        }
        documentTx.activateOnCurrentThread()
        val oClass = documentTx.getClass(table.toString())
        oClass.areIndexed()
        oClass.getInvolvedIndexes(index.toString())
        oClass.indexedProperties.forEach { a ->
            var s = oClass.getInvolvedIndexes(a.name).toString()
            if ((s.substring(s.indexOf("[") + 1, s.indexOf("]"))).toUpperCase() == index.toString().toUpperCase()) {
                a.dropIndexes()

            }
        }
//        oClass.getProperty(index.toString()).dropIndexes()

        return null
    }

    companion object {
        fun handle(x: SQLDropIndexStatement, connection: OConnection) {

            connection.writeok()

        }
    }
}
