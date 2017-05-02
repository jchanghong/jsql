//package io.jsql.springutil;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.core.annotation.Order;
//import org.springframework.stereotype.Component;
//
///**
// * Created by 长宏 on 2017/5/2 0002.
// */
//@SpringBootApplication
//@Order(1)
//public class Maintest implements CommandLineRunner {
//    @Autowired
//    MyLogger logger;
//    @Autowired
//    Bean1 bean1;
//    @Autowired
//    Bean2 bean2;
//    Maintest() {
//        System.out.println("maintest()");
//    }
//
//    public static void main(String[] args) {
//        try {
//            SpringApplication.run(Maintest.class, args);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    @Override
//    public void run(String... strings) throws Exception {
//        logger.info("in run,,,,,");
//        bean2.printbean1();
//        logger.info(bean1);
//        logger.info(bean2.getBean1());
//        logger.info(bean2.getBean1());
//    }
//}
