package io.jsql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * Created by 长宏 on 2017/5/2 0002.
 */
@EnableAutoConfiguration

@Component
public class Maintest implements CommandLineRunner{
    Maintest()
     {
         System.out.println("maintest()");
    }
    public static void main(String[] args) {
        try {
            SpringApplication.run(Maintest.class, args);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run(String... strings) throws Exception {
        System.out.println("in run...");
    }
}
