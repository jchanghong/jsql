/*
 * Java-based distributed database like Mysql
 */

package io.jsql.sql.handler.data_define

import com.alibaba.druid.sql.ast.SQLStatement
import com.alibaba.druid.sql.ast.statement.SQLAlterTableStatement
import com.orientechnologies.orient.core.db.document.ODatabaseDocument
import com.orientechnologies.orient.core.metadata.schema.OClass
import com.orientechnologies.orient.core.metadata.schema.OType
import io.jsql.sql.OConnection
import io.jsql.sql.handler.SqlStatementHander
import io.jsql.storage.StorageException
import org.springframework.stereotype.Component

/**
 * Created by 长宏 on 2017/3/18 0018.
 * ALTER TABLE t2 DROP COLUMN c, DROP COLUMN d;
 * 暂未考虑索引
 */
@Component
class AlterTable : SqlStatementHander() {

    override fun supportSQLstatement(): Class<out SQLStatement> {
        return SQLAlterTableStatement::class.java
    }

    @Throws(Exception::class)
    override fun handle0(sqlStatement: SQLStatement, c: OConnection): Any? {

        val sql = sqlStatement as SQLAlterTableStatement
        val documentTx: ODatabaseDocument
        try {
            documentTx = OConnection.DB_ADMIN!!.getdb(c.schema!!)
        } catch (e: StorageException) {
            e.printStackTrace()
            return e.message!!
        }
        documentTx.activateOnCurrentThread()

        var syntax = sql.toString().toUpperCase().split(" ")
        var tablename = sql.tableSource.toString()
        val oClass = documentTx.getClass(tablename)

        if (sql.toString().contains("DROP")) {
            var field = syntax.get(syntax.size - 1)
            drop(field, oClass)
        } else if (sql.toString().contains("ADD")) {
            var column = syntax.get(syntax.size - 2)
            add(syntax, oClass, column)
        } else if (sql.toString().contains("MODIFY")) {
            var pre = syntax.get(syntax.size - 2)
            drop(pre, oClass)
            add(syntax, oClass, pre)
        } else if (sql.toString().contains("CHANGE")) {
            var pre = syntax.get(syntax.size - 3)
            var new = syntax.get(syntax.size - 2)
            drop(pre, oClass)
            add(syntax, oClass, new)
        }
        return null
    }

    private fun drop(field: String, oClass: OClass) {
        oClass.dropProperty(field)

    }

    private fun add(syntax: List<String>, oClass: OClass, column: String) {
        if (syntax.contains("INT")) {
            oClass.createProperty(column, OType.INTEGER)
        } else if (syntax.contains("VARCHAR")) {
            oClass.createProperty(column, OType.STRING)
        } else if (syntax.contains("DATETIME")) {
            oClass.createProperty(column, OType.DATETIME)

        } else if (syntax.contains("TIMES")) {
            oClass.createProperty(column, OType.DATE)

        } else {
            oClass.createProperty(column, OType.STRING)
        }
    }

    companion object {
        fun handle(x: SQLAlterTableStatement, connection: OConnection) {

            connection.writeok()

        }
    }
}
