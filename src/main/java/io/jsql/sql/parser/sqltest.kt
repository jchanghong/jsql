/*
 * Java-based distributed database like Mysql
 */

package io.jsql.sql.parser

import com.alibaba.druid.sql.dialect.mysql.parser.MySqlStatementParser
import io.jsql.sql.ONullConnection

/**
 * Created by 长宏 on 2017/2/25 0025.
 * 测试
 */
object sqltest {
    @JvmStatic fun main(args: Array<String>) {
        val sql = "showdb tables;"
        val parser = MySqlStatementParser(sql)
        val mySqlStatement = parser.parseStatement()
        val visitor = MSQLvisitor(ONullConnection())
        mySqlStatement.accept(visitor)

    }
}
