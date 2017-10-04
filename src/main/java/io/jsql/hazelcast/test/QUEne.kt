/*
 * Java-based distributed database like Mysql
 */

package io.jsql.hazelcast.test

import com.hazelcast.config.Config
import com.hazelcast.core.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import java.util.concurrent.BlockingQueue
import java.util.concurrent.TimeUnit

/**
 * Created by 长宏 on 2017/5/3 0003.
 */
object QUEne {
    internal var logger = LoggerFactory.getLogger("mew")

    @Throws(InterruptedException::class)
    @JvmStatic
    fun main(args: Array<String>) {
        val listener = object : ItemListener<String> {
            override fun itemAdded(item: ItemEvent<String>) {
                logger.info(item.toString())

            }

            override fun itemRemoved(item: ItemEvent<String>) {
                logger.info(item.toString())

            }
        }
        val config = Config()
        val h = Hazelcast.newHazelcastInstance(config)
        val queue = h.getQueue<String>("my-distributed-queue")
        queue.addItemListener(listener, true)
        queue.offer("item")
        val item = queue.poll()

        logger.info(item)

        //Timed blocking Operations
        queue.offer("anotheritem", 500, TimeUnit.MILLISECONDS)
        val anotherItem = queue.poll(5, TimeUnit.SECONDS)

        logger.info(anotherItem)
        //Indefinitely blocking Operations
        queue.put("yetanotheritem")
        val yetanother = queue.take()
        logger.info(yetanother)

    }

}
