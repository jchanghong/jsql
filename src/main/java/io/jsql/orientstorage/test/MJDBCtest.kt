package io.jsql.orientstorage.test

import java.sql.*

/**
 * Created by 长宏 on 2017/3/22 0022.
 */
object MJDBCtest {
    @JvmStatic fun main(args: Array<String>) {
        try {
            val connection = DriverManager.getConnection("jdbc:mysql://localhost:9999/db1?user=root&password=123456")
            val statement = connection.createStatement()
            val resultSet = statement.executeQuery("show tables")
            while (resultSet.next()) {
                println(resultSet.getString(1))

            }

        } catch (e: SQLException) {
            e.printStackTrace()
        }

    }
}
