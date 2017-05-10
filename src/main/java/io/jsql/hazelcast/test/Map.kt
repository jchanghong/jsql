package io.jsql.hazelcast.test

import com.hazelcast.config.Config
import com.hazelcast.core.Hazelcast
import com.hazelcast.core.HazelcastInstance
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import java.util.concurrent.ConcurrentMap

/**
 * Created by 长宏 on 2017/5/3 0003.
 */
object Map {
    internal var logger = LoggerFactory.getLogger("me")
    @JvmStatic fun main(args: Array<String>) {
        val config = Config()
        val h = Hazelcast.newHazelcastInstance(config)
        val map = h.getMap<String, String>("mymap")
        map.put("key", "value")
        map["key"]

        logger.info(map["key"])
        //Concurrent Map methods
        (map as java.util.Map<String, String>).putIfAbsent("somekey", "somevalue")
        map.replace("key", "value", "newvalue")
    }

}
