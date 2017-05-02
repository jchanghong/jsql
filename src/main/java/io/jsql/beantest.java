package io.jsql;

import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * Created by 长宏 on 2017/5/2 0002.
 */
@Component
@PropertySource("file:./config/config.properties")
public class beantest {
    int port ;
}
