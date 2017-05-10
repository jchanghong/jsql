package io.jsql.springutil.test

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Profile
import org.springframework.core.annotation.Order
import org.springframework.core.env.Environment

/**
 * Created by 长宏 on 2017/5/2 0002.
 */
@SpringBootApplication
@Order(1)
@Profile("dev")
@EnableCaching(proxyTargetClass = true)
class Maintest internal constructor() : CommandLineRunner {
    @Autowired
    internal var logger: MyLogger? = null
    @Autowired
    internal var bean1: Bean1? = null
    @Autowired
    internal var bean2: Bean2? = null

    @Autowired
    internal var list: List<Testi>? = null

    init {
        println("maintest()")
    }

    @Autowired
    internal var environment: Environment? = null
    @Autowired
    internal var applicationContext: ApplicationContext? = null
    @Autowired
    internal var cacheManager: CacheManager? = null

    @Throws(Exception::class)
    override fun run(vararg strings: String) {
        logger!!.info(environment!!.getProperty("server.port"))
        for (i in 0..99) {
            applicationContext!!.getBean(bean3::class.java)
        }
        bean2!!.getBean1()
        bean2!!.getBean1()
        bean2!!.getBean1()
        logger!!.info(list!!.contains(bean1!!))
        logger!!.info(list!!.contains(bean2!!))
        logger!!.info(applicationContext!!.beanDefinitionNames.size)

    }

    companion object {
        @JvmStatic fun main(args: Array<String>) {
            try {
                SpringApplication.run(Maintest::class.java, *args)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }
}
