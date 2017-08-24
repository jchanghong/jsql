/*
 * Java-based distributed database like Mysql
 */

package io.jsql.audit

import org.apache.http.HttpHost
import org.apache.http.entity.ContentType
import org.apache.http.nio.entity.NStringEntity
import org.elasticsearch.client.RestClient
import org.slf4j.LoggerFactory
import java.util.concurrent.LinkedBlockingQueue
import kotlin.concurrent.thread

/**
\* Created with IntelliJ IDEA.
\* User: jiang
\* Date: 2017/6/15 0015
\* Time: 16:16
\*/
object elasticUtil {
    val log=LoggerFactory.getLogger(elasticUtil::class.java)
    init {
        thread(start = true) {
            log.info("audit thread start-------------------")
            while (true) {
                val any = logquene.take()
                println(any.toString())
                val path = if (any is SqlLog) "/sqlindex/sqllog" else "/sqlindex/loginlog"
                var entity = NStringEntity(jsonmapper.writeValueAsString(any), ContentType.APPLICATION_JSON)
//                esrestClient.performRequest("post", path, emptyMap(), entity)
            }
            log.info("audit thread end-------------------")
        }
    }
    val logquene = LinkedBlockingQueue<Any>()
    fun put(x: LoginLog): Unit {
        logquene.put(x)
    }

    fun put(x: SqlLog): Unit {
        logquene.put(x)
    }
    val esrestClient = RestClient.builder(
            HttpHost("localhost", 9200, "http")
    ).build()

}