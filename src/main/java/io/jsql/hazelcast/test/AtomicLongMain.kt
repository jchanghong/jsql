/*
 * Java-based distributed database like Mysql
 */

package io.jsql.hazelcast.test

import com.hazelcast.core.Hazelcast
import com.hazelcast.core.IFunction
import org.slf4j.LoggerFactory

/**
 * Created by 长宏 on 2017/5/3 0003.
 */
object AtomicLongMain {
    internal var logger = LoggerFactory.getLogger("me")
    @JvmStatic
    fun main(args: Array<String>) {
        val hz = Hazelcast.newHazelcastInstance()
        val counter = hz.getAtomicLong("counter")
        counter.addAndGet(3) // value is now 3
        counter.alter(MultiplyByTwo())//value is now 6
        val idGenerator = hz.getIdGenerator("myid")

        logger.info(idGenerator.newId().toString() + "")
        logger.info(idGenerator.newId().toString() + "")
        logger.info(idGenerator.newId().toString() + "")
        logger.info(idGenerator.newId().toString() + "")
        logger.info(idGenerator.newId().toString() + "")
        logger.info(idGenerator.newId().toString() + "")
        logger.info(idGenerator.newId().toString() + "")
        logger.info(idGenerator.newId().toString() + "")
        logger.info(idGenerator.newId().toString() + "")
        println("counter: " + counter.get())
    }

    class MultiplyByTwo : IFunction<Long, Long> {
        override fun apply(input: Long?): Long? {
            return input!! * 2
        }
    }

}
