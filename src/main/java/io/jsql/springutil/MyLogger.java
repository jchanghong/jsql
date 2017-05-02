package io.jsql.springutil;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Created by 长宏 on 2017/5/2 0002.
 */
@Component
@Scope("singleton")
public class MyLogger {
    public void info(Object o) {
        System.out.println(o.toString());
    }
}
