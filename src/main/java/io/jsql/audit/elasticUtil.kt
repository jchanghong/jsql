package io.jsql.audit

import org.apache.http.HttpHost
import org.apache.http.entity.ContentType
import org.apache.http.nio.entity.NStringEntity
import org.elasticsearch.client.RestClient
import java.util.concurrent.LinkedBlockingQueue
import kotlin.concurrent.thread

/**
\* Created with IntelliJ IDEA.
\* User: jiang
\* Date: 2017/6/15 0015
\* Time: 16:16
\*/
object elasticUtil {
    val logquene = LinkedBlockingQueue<Any>()
    fun put(x: LoginLog): Unit {
        run()
        logquene.put(x)
    }

    fun put(x: SqlLog): Unit {
        run()
        logquene.put(x)
    }

    val esrestClient = RestClient.builder(
            HttpHost("localhost", 9200, "http")
    ).build()
    var runing = false
    fun run(): Unit {
        if (runing) {
            return
        }
        runing = true
        thread(start = true) {
            println("thread start")
            while (true) {
                val any = logquene.take()
                val path = if (any is SqlLog) "/sqlindex/sqllog" else "/sqlindex/loginlog"
                var entity = NStringEntity(jsonmapper.writeValueAsString(any), ContentType.APPLICATION_JSON)
                print(entity.toString())
                esrestClient.performRequest("post", path, emptyMap(), entity)
            }
            println("thread end")
        }
    }
}