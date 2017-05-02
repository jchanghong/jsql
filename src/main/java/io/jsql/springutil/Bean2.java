package io.jsql.springutil;

import io.jsql.springutil.Bean1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * Created by 长宏 on 2017/5/2 0002.
 */
@Component
public class Bean2 {
    @Autowired
    ApplicationContext context;
    @Autowired
    Bean1 bean1;
    public void printbean1() {
        System.out.println(bean1);
    }


    public Bean1 getBean1() {
        return context.getBean(Bean1.class);
    }
}
