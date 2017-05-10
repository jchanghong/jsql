package io.jsql.springutil.test

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.Cacheable
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

/**
 * Created by 长宏 on 2017/5/2 0002.
 */
@Component
@Profile("dev")
class Bean2 : Testi {
    @Autowired
    internal var context: ApplicationContext? = null
    @Autowired
    internal var bean1: Bean1? = null

    fun printbean1() {
        println(bean1)
    }


    @Cacheable(cacheNames = arrayOf("me"))
    fun getBean1(): Bean1 {
        println("in getbean1()")
        return context!!.getBean(Bean1::class.java)
    }
}
