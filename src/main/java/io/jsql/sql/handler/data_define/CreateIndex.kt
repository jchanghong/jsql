package io.jsql.sql.handler.data_define

import com.alibaba.druid.sql.ast.SQLStatement
import com.alibaba.druid.sql.ast.statement.SQLCreateIndexStatement
import com.orientechnologies.orient.core.db.document.ODatabaseDocument
import com.orientechnologies.orient.core.index.OIndexDefinitionFactory
import io.jsql.sql.OConnection
import io.jsql.sql.handler.SqlStatementHander
import io.jsql.storage.StorageException
import org.springframework.stereotype.Component

/**
 * Created by 长宏 on 2017/3/18 0018.
 */
@Component
class CreateIndex : SqlStatementHander() {

    override fun supportSQLstatement(): Class<out SQLStatement> {
        return SQLCreateIndexStatement::class.java
    }

    @Throws(Exception::class)
    override fun handle0(sqlStatement: SQLStatement, c: OConnection): Any? {
        var sql = sqlStatement as SQLCreateIndexStatement
        val documentTx: ODatabaseDocument
        try {
            documentTx = OConnection.DB_ADMIN!!.getdb(c.schema!!)
        } catch (e: StorageException) {
            e.printStackTrace()
            return e.message!!
        }
        documentTx.activateOnCurrentThread()

        var syntax = sql.toString().toUpperCase().split(" ")
        var tablename = sql.table.toString()
        var s = syntax.get(syntax.size - 1)
        s = s.substring(s.indexOf("(") + 1, s.indexOf(")"))

        val oClass = documentTx.getClass(tablename)
        System.out.println()

        System.out.println(sql.name.toString())//index
        if (sql.type == null) {

            oClass.createIndex(sql.name.toString(), "NOTUNIQUE_HASH_INDEX", s)
        } else {
            oClass.createIndex(sql.name.toString(), sql.type.toString(), s)
        }


//     System.out.println(sql.type.toString())
//        System.out.println(sql.using.toString())


        return null
    }


    companion object {
        fun handle(x: SQLCreateIndexStatement, connection: OConnection) {

            connection.writeok()

        }
    }
}
