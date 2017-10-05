/*
 * Java-based distributed database like Mysql
 */

package io.jsql.audit

import java.util.*


/**
\* Created with IntelliJ IDEA.
\* User: jiang
\* Date: 2017/10/5 0005
\* Time: 14:52
\*/
/**
 * 插入elasticsearch测试数据
 * 直接运行就可
 * 长时间运行
 * */
fun main(args: Array<String>) {
   testlongtime_yunxing()
//    testinsertquikely()
}

//快速插入
fun testinsertquikely() {
    println(jsonmapper.writeValueAsString(SqlLog("dd", "dd", "dd")))
    val s = System.currentTimeMillis()
    for (i in 1..100) {
        elasticUtil.sentoServerSyn(SqlLog("select * from table$i", "user $i", "local"))
    }
    for (i in 1..5) {
        elasticUtil.sentoServerSyn(LoginLog("user $i", "localhost", true))
    }
    println("end------")
    println((System.currentTimeMillis() - s) / 1000)
    elasticUtil.close()
}

//长时间运行
fun testlongtime_yunxing() {
    println(jsonmapper.writeValueAsString(SqlLog("dd", "dd", "dd")))
    val ran = Random()
    val s = System.currentTimeMillis()
    for (i in 1..1000) {
        Thread.sleep(ran.nextInt(100) + 0L)
        elasticUtil.sentoServerSyn(SqlLog("select * from table$i", "user $i", "local"))
    }
    for (i in 1..1000) {
        Thread.sleep(ran.nextInt(100) + 0L)
        elasticUtil.sentoServerSyn(LoginLog("user $i", "localhost", true))
    }
    println("end------")
    println((System.currentTimeMillis() - s) / 1000)
    elasticUtil.close()
}