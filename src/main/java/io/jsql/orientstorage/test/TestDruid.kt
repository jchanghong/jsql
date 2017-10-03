/*
 * Java-based distributed database like Mysql
 */

package io.jsql.orientstorage.test

/**
 * Created by 长宏 on 2017/3/23 0023.
 */
object TestDruid {
    @JvmStatic
    fun main(args: Array<String>) {
        val sql = "SELECT  @@session.auto_increment_increment AS auto_increment_increment"
        val sql1 = "select @@se.ii"
        val parser = MySqlStatementParser(sql)
        val sqlStatement = parser.parseStatement()
        val visitor = MySqlSchemaStatVisitor()
        sqlStatement.accept(visitor)

        println(visitor.columns)
    }
}
