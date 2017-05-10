package io.jsql.springutil.test

import org.springframework.context.annotation.Profile
import org.springframework.context.annotation.PropertySource
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component

import java.io.Serializable

/**
 * Created by 长宏 on 2017/5/2 0002.
 */
@Component
@Scope("prototype")
@Profile("dev")
@PropertySource("file:./config/config.properties")
class Bean1 internal constructor() : Serializable, Testi {
    //    @Value("${port}")
    internal var port: String? = null
}
