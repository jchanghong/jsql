/*
 * Java-based distributed database like Mysql
 */

package io.jsql.test

import java.sql.DriverManager

/**
 * Created by 长宏 on 2017/4/30 0030.
 * test
 */
object JDBC {
    @JvmStatic
    fun main(args: Array<String>) {
//        val url = "jdbc:mysql://localhost:3306/changhong?" + "user=root&password=0000&useUnicode=" +
//                "true&characterEncoding=UTF8"
//        val url2 = "jdbc:mysql://localhost:9999/changhong?" + "user=root&password=0000&useUnicode=" +
//                "true&characterEncoding=UTF8"
//        try {
//            val connection = DriverManager.getConnection(url2)
//            val statement = connection.createStatement()
//            println(connection.autoCommit)
//            val result = statement.executeQuery("select * from test")
//            while (result.next()) {
//                println(result.getString(1))
//            }
//        } catch (e: SQLException) {
//            e.printStackTrace()
//        }
        JDBC.test()
    }

    fun test() {
        val sqlnumber = intArrayOf(10, 100, 1000, 3000, 5000, 10000) //sql执行次数
        val mysql = "jdbc:mysql://localhost:3306/changhong?" + "user=root&" +
                "password=0000&useUnicode=" + "true&characterEncoding=UTF8"
        var connection = DriverManager.getConnection(mysql)
        var statement = connection.createStatement()
        println("mysql sqlnumber to times:")
        for (number in sqlnumber) {
            val start = System.currentTimeMillis()
            for (i in 1..number) {
                statement.executeQuery("select * from test")
            }
            val end = System.currentTimeMillis()
            println("$number      ${end - start}")
        }
        connection.close()
        val jsql = "jdbc:mysql://localhost:9999/changhong?" + "user=root&" +
                "password=0000&useUnicode=" + "true&characterEncoding=UTF8"
        connection = DriverManager.getConnection(jsql)
        statement = connection.createStatement()
        println("jsql sqlnumber to times:")
        for (number in sqlnumber) {
            val start = System.currentTimeMillis()
            for (i in 1..number) {
                statement.executeQuery("select * from test")
            }
            val end = System.currentTimeMillis()
            println("$number      ${end - start}")
        }
        connection.close()

    }
}
