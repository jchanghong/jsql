package io.jsql.springutil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Created by 长宏 on 2017/5/2 0002.
 */
@Component
@Scope("prototype")
@PropertySource("file:./config/config.properties")
public class Bean1 {
    @Value("${port}")
    String port ;

    Bean1() {
    }
}
