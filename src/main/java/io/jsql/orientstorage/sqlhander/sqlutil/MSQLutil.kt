package io.jsql.orientstorage.sqlhander.sqlutil

import com.alibaba.druid.sql.SQLUtils
import com.alibaba.druid.sql.ast.statement.SQLColumnDefinition
import com.alibaba.druid.sql.ast.statement.SQLCreateDatabaseStatement
import com.alibaba.druid.sql.ast.statement.SQLSelectStatement
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlCreateTableStatement
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlSelectQueryBlock
import com.alibaba.druid.sql.dialect.mysql.parser.MySqlStatementParser

import java.util.ArrayList
import java.util.HashMap

/**
 * Created by 长宏 on 2016/12/17 0017.
 */
object MSQLutil {
    /**
     * Gets .

     * @param sqlStatement the sql statement
     * *
     * @return the
     */
    fun gettablename(sqlStatement: SQLSelectStatement): String {
        val select = sqlStatement.select.query as MySqlSelectQueryBlock
        return select.from.toString()
    }

    /**
     * Gets .

     * @param sqlStatement the sql statement
     * *
     * @return the
     */
    fun gettablenamefileds(sqlStatement: SQLSelectStatement): List<String> {
        val select = sqlStatement.select.query as MySqlSelectQueryBlock
        val list = ArrayList<String>()
        select.selectList.forEach { a -> list.add(a.expr.toString()) }
        return list
    }

    /**
     * Gets .

     * @param sqlStatement the sql statement
     * *
     * @return the
     */
    fun gettablenamefileds(sqlStatement: MySqlCreateTableStatement): Map<String, String> {
        val list = HashMap<String, String>()
        sqlStatement.tableElementList.forEach { item ->
            val columnDefinition = item as SQLColumnDefinition
            val name = columnDefinition.name.toString()
            val type = columnDefinition.dataType.name
            list.put(name, type)
        }
        return list
    }


    /**
     * Gets .

     * @param sqlStatement the sql statement
     * *
     * @return the
     */
    fun gettablename(sqlselectStatement: String): String {
        var sqlSelectStatement: SQLSelectStatement? = null
        val parser = MySqlStatementParser(sqlselectStatement)
        sqlSelectStatement = parser.parseStatement() as SQLSelectStatement
        return gettablename(sqlSelectStatement)
    }

    /**
     * Gets .

     * @param sqlStatement the sql statement
     * *
     * @return the
     */
    fun gettablenamefileds(sqlStatement: String): List<String> {
        var sqlSelectStatement: SQLSelectStatement? = null
        val parser = MySqlStatementParser(sqlStatement)
        sqlSelectStatement = parser.parseSelect() as SQLSelectStatement
        return gettablenamefileds(sqlSelectStatement)
    }

    /**
     * Gets .

     * @param statement the statement
     * *
     * @return the
     */
    fun getdbname(statement: SQLCreateDatabaseStatement): String {
        return statement.name.simpleName
    }

    /**
     * The entry point of application.

     * @param args the input arguments
     */
    @JvmStatic fun main(args: Array<String>) {

        val sql = "create table t1(id int,name varchar);"
        gettablenamefileds(SQLUtils.parseStatements(sql, "mysql")[0] as MySqlCreateTableStatement)
        //        System.out.println(gettablenamefileds("select *,rff from dd"));
        //        System.out.println(gettablename("select * from dd"));
    }
}
