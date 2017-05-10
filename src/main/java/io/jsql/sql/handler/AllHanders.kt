package io.jsql.sql.handler

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

import javax.annotation.PostConstruct
import java.util.HashMap

/**
 * Created by 长宏 on 2017/5/3 0003.
 */
@Component
class AllHanders {
    @Autowired
    private val handerList: List<SqlStatementHander>? = null
    var handerMap: MutableMap<Class<*>, SqlStatementHander> = HashMap()
    internal var logger = LoggerFactory.getLogger(AllHanders::class.java.name)
    @PostConstruct
    fun init() {
        handerList!!.forEach { a -> handerMap.put(a.supportSQLstatement(), a) }
    }
}
