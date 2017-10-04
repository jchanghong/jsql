/*
 * Java-based distributed database like Mysql
 */

package io.jsql.test

import java.sql.DriverManager
import java.sql.SQLException

/**
 * Created by 长宏 on 2017/4/30 0030.
 * test
 */
object JDBC {
    @JvmStatic
    fun main(args: Array<String>) {
        val url = "jdbc:mysql://localhost:9999/changhong?" + "user=root&password=0000&useUnicode=true&characterEncoding=UTF8"
        try {
            val connection = DriverManager.getConnection(url)
            println(connection.autoCommit)
            println(connection.catalog)
        } catch (e: SQLException) {
            e.printStackTrace()
        }

    }
}
