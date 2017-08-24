/*
 * Java-based distributed database like Mysql
 */

package io.jsql.my_config

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.PropertySource
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct

/**
 * Created by 长宏 on 2017/5/2 0002.
 */
@Component
@PropertySource("file:./config/config.properties")
class MyConfig {
    @Value("\${jsql.distributed}")
    val distributed = true
    @Value("\${jsql.audit}")
    val audit = true
    @PostConstruct
    fun afterinit() {
        log.info(this.toString())
    }

    override fun toString(): String {
        return "distribute:$distributed, audit:$audit"
    }
    companion object {
        val log=LoggerFactory.getLogger(MyConfig::class.java)
    }

}
