package io.jsql.hazelcast.test;
import com.hazelcast.config.Config;
import com.hazelcast.core.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created by 长宏 on 2017/5/3 0003.
 */
public class QUEne {
    static Logger logger = LoggerFactory.getLogger("mew");

    public static void main(String[] args) throws InterruptedException {
        ItemListener<String> listener = new ItemListener<String>() {
            @Override
            public void itemAdded(ItemEvent<String> item) {
                logger.info(item.toString());

            }

            @Override
            public void itemRemoved(ItemEvent<String> item) {
                logger.info(item.toString());

            }
        };
        Config config = new Config();
        HazelcastInstance h = Hazelcast.newHazelcastInstance(config);
        BlockingQueue<String> queue = h.getQueue("my-distributed-queue");
        IQueue<String> iQueue = (IQueue<String>) queue;
        iQueue.addItemListener(listener, true);
        queue.offer("item");
        String item = queue.poll();

        logger.info(item);

        //Timed blocking Operations
        queue.offer("anotheritem", 500, TimeUnit.MILLISECONDS);
        String anotherItem = queue.poll(5, TimeUnit.SECONDS);

        logger.info(anotherItem);
        //Indefinitely blocking Operations
        queue.put("yetanotheritem");
        String yetanother = queue.take();
        logger.info(yetanother);

    }

}
