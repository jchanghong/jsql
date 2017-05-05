package io.jsql.springutil.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;

import java.util.List;
import java.util.Map;

/**
 * Created by 长宏 on 2017/5/2 0002.
 */
@SpringBootApplication
@Order(1)
@Profile("dev")
@EnableCaching(proxyTargetClass = true)
public class Maintest implements CommandLineRunner {
    @Autowired
    MyLogger logger;
    @Autowired
    Bean1 bean1;
    @Autowired
    Bean2 bean2;

    @Autowired
    List<Testi> list;
    Maintest() {
        System.out.println("maintest()");
    }

    @Autowired
    Environment environment;
    public static void main(String[] args) {
        try {
            SpringApplication.run(Maintest.class, args);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    @Autowired
    ApplicationContext applicationContext;
    @Autowired
    CacheManager cacheManager;
    @Override
    public void run(String... strings) throws Exception {
        logger.info(environment.getProperty("server.port"));
        for (int i = 0; i < 100; i++) {
            applicationContext.getBean(bean3.class);
        }
        bean2.getBean1();
        bean2.getBean1();
        bean2.getBean1();
        logger.info(list.contains(bean1));
        logger.info(list.contains(bean2));
        logger.info(applicationContext.getBeanDefinitionNames().length);

    }
}
