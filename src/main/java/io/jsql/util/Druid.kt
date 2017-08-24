/*
 * Java-based distributed database like Mysql
 */

package io.jsql.util

import com.alibaba.druid.sql.SQLUtils
import com.alibaba.druid.sql.ast.SQLStatement
import com.alibaba.druid.util.JdbcConstants


/**
\* Created with IntelliJ IDEA.
\* User: jiang
\* Date: 2017/8/2 0002
\* Time: 20:32
\*/
fun String.tosql(): SQLStatement {
    val dbType = JdbcConstants.MYSQL // 可以是ORACLE、POSTGRESQL、SQLSERVER、ODPS等
    val stmtList = SQLUtils.parseStatements(this, dbType)
    return stmtList[0]
}

fun main(args: Array<String>) {
    var sql = "select * from t1"
    println(sql.tosql().javaClass.name)
}