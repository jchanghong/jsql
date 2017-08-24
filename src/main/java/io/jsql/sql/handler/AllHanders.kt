/*
 * Java-based distributed database like Mysql
 */

package io.jsql.sql.handler

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.*
import javax.annotation.PostConstruct

/**
 * Created by 长宏 on 2017/5/3 0003.
 */
@Component
class AllHanders {
    @Autowired
 lateinit  private var handerList: List<SqlStatementHander>
    var handerMap: MutableMap<Class<*>, SqlStatementHander> = IdentityHashMap()
    internal var logger = LoggerFactory.getLogger(AllHanders::class.java.name)
    @PostConstruct
    fun init() {
        handerList.forEach {  handerMap.put(it.supportSQLstatement(), it) }
    }
}
