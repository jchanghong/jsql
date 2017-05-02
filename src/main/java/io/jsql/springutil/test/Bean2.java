package io.jsql.springutil.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * Created by 长宏 on 2017/5/2 0002.
 */
@Component
@Profile("dev")
public class Bean2 {
    @Autowired
    ApplicationContext context;
    @Autowired
    Bean1 bean1;
    public void printbean1() {
        System.out.println(bean1);
    }


    @Cacheable(cacheNames = "me")
    public Bean1 getBean1() {
        System.out.println("in getbean1()");
        return context.getBean(Bean1.class);
    }
}
