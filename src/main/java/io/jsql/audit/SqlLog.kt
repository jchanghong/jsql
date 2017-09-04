/*
 * Java-based distributed database like Mysql
 */

package io.jsql.audit

import com.fasterxml.jackson.databind.ObjectMapper
import java.util.*
import java.util.concurrent.CountDownLatch


/**
\* Created with IntelliJ IDEA.
\* User: jiang
\* Date: 2017/6/15 0015
\* Time: 14:42
\*/
data class SqlLog(val sql: String, val user: String, val host: String, val time: Date = Date())


val jsonmapper = ObjectMapper()
fun SqlLog.sentoELServer() {
    elasticUtil.put(this)
}

/**
 * 测试数据*/
fun main(args: Array<String>) {
    println(jsonmapper.writeValueAsString(SqlLog("dd", "dd", "dd")))
    val  ran=Random()
    val s=System.currentTimeMillis()
    for (i in 1..1000) {
        Thread.sleep(ran.nextInt(100)+0L)
        elasticUtil.sentoServerSyn(SqlLog("select * from table$i","user $i","local"))
    }
    for (i in 1..1000) {
        Thread.sleep(ran.nextInt(100)+0L)
        elasticUtil.sentoServerSyn(LoginLog("user $i","localhost",true))
    }
    println("end------")
    println((System.currentTimeMillis()-s)/1000)
    elasticUtil.close()
}

//private fun insertdate() {
//    var cat=CountDownLatch(1)
//    for (i in 1..20) {
//        val login = LoginLog("user $i", "local", true)
//        login.sendesServer()
//    }
//    for (i in 1..20) {
//        val login = SqlLog("select * from blog", "user $i", "local")
//        login.sentoELServer()
//    }
//}