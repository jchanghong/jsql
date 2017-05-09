package io.jsql;

import com.hazelcast.map.impl.client.MapPortableHook;
import io.jsql.hazelcast.MyHazelcast;
import io.jsql.netty.NettyServer;
import io.jsql.sql.OConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.annotation.Order;

/**
 * Created by 长宏 on 2017/5/2 0002.
 * 启动类，通过spring boot启动。
 * ioc，aop功能。
 */
@SpringBootApplication
@Order(1)
public class SpringMain implements CommandLineRunner {
    private  Logger logger = LoggerFactory.getLogger(SpringMain.class.getName());
    @Autowired
    private NettyServer nettyServer;
    private Thread hook;
    public static void main(String[] args) {
        SpringApplication.run(SpringMain.class, args);
    }

    @Override
    public void run(String... strings) throws Exception {
        hook = new Mhook();
        Runtime.getRuntime().addShutdownHook(hook);
        logger.info("begin start....................................");
        try {
            nettyServer.start();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
    @Autowired
    MyHazelcast myHazelcast;
   private  class Mhook extends Thread {
        @Override
        public void run() {
            myHazelcast.getHazelcastInstance().shutdown();
            logger.info("in shutdow hook.........");
            OConnection.DB_ADMIN.close();
        }
    }
}
