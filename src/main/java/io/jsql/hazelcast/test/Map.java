package io.jsql.hazelcast.test;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentMap;

/**
 * Created by 长宏 on 2017/5/3 0003.
 */
public class Map {
    static Logger logger = LoggerFactory.getLogger("me");
    public static void main(String[] args) {
        Config config = new Config();
        HazelcastInstance h = Hazelcast.newHazelcastInstance(config);
        ConcurrentMap<String, String> map = h.getMap("mymap");
        map.put("key", "value");
        map.get("key");

        logger.info(map.get("key"));
        //Concurrent Map methods
        map.putIfAbsent("somekey", "somevalue");
        map.replace("key", "value", "newvalue");
    }

}
