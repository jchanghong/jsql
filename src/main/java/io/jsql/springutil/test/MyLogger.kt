package io.jsql.springutil.test

import org.springframework.context.annotation.Profile
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component

/**
 * Created by 长宏 on 2017/5/2 0002.
 */
@Component
@Scope("singleton")
class MyLogger {
    fun info(o: Any) {
        println(o.toString())
    }
}
