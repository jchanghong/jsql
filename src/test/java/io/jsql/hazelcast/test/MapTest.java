package io.jsql.hazelcast.test;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.ConcurrentMap;

import static org.junit.Assert.*;

/**
 * Created by 长宏 on 2017/5/3 0003.
 */
public class MapTest {
    HazelcastInstance hazelcastInstance;
    @Before
    public void setUp() throws Exception {
        Config config = new Config();
        hazelcastInstance = Hazelcast.newHazelcastInstance(config);
    }

    @Test
    public void map() throws Exception {
        IMap<String, String> map = hazelcastInstance.getMap("map");
        map.put("n", "m");
        assertEquals(map.size(), 1);
        assertEquals(map.get("n"), "m");
        map.remove("n");
        assertEquals(map.size(), 0);
    }

    @After
    public void tearDown() throws Exception {

    }

}