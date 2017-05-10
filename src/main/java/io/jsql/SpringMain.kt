package io.jsql

import com.hazelcast.map.impl.client.MapPortableHook
import io.jsql.hazelcast.MyHazelcast
import io.jsql.netty.NettyServer
import io.jsql.sql.OConnection
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.core.annotation.Order

/**
 * Created by 长宏 on 2017/5/2 0002.
 * 启动类，通过spring boot启动。
 * ioc，aop功能。
 */
@SpringBootApplication
@Order(1)
open class SpringMain : CommandLineRunner {
    private val logger = LoggerFactory.getLogger(SpringMain::class.java.name)
    @Autowired
   lateinit private var nettyServer: NettyServer
    private var hook=Mhook()

    @Throws(Exception::class)
    override fun run(vararg strings: String) {
        Runtime.getRuntime().addShutdownHook(hook)
        logger.info("begin start....................................")
        try {
            nettyServer.start()
        } catch (e: Exception) {
            e.printStackTrace()
            System.exit(-1)
        }

    }

    @Autowired
  lateinit  internal var myHazelcast: MyHazelcast

    private inner class Mhook : Thread() {
        override fun run() {
            logger.info("in shutdow hook.........")
            myHazelcast.hazelcastInstance?.shutdown()
            OConnection.DB_ADMIN?.close()
        }
    }

    companion object {
        @JvmStatic fun main(args: Array<String>) {
            SpringApplication.run(SpringMain::class.java, *args)
        }
    }
}
