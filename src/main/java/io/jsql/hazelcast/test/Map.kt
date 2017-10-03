/*
 * Java-based distributed database like Mysql
 */

package io.jsql.hazelcast.test

import com.hazelcast.config.Config
import com.hazelcast.core.Hazelcast
import com.hazelcast.core.IMap
import org.slf4j.LoggerFactory

/**
 * Created by 长宏 on 2017/5/3 0003.
 */
object Map {
    internal var logger = LoggerFactory.getLogger("me")
    @JvmStatic
    fun main(args: Array<String>) {
        val config = Config()
        val h = Hazelcast.newHazelcastInstance(config)
        val map: IMap<String, String> = h.getMap<String, String>("mymap")
        map.put("key", "value")
        map["key"]
        logger.info(map["key"])
        //Concurrent Map methods
        map.putIfAbsent("somekey", "somevalue")
        map.replace("key", "value", "newvalue")
    }

}
