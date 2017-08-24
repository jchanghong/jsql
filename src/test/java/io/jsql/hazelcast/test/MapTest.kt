/*
 * Java-based distributed database like Mysql
 */

package io.jsql.hazelcast.test

import com.hazelcast.config.Config
import com.hazelcast.core.Hazelcast
import com.hazelcast.core.HazelcastInstance
import com.hazelcast.core.IMap
import org.junit.After
import org.junit.Before
import org.junit.Test

import java.util.concurrent.ConcurrentMap

import org.junit.Assert.*

/**
 * Created by 长宏 on 2017/5/3 0003.
 */

class MapTest {
    internal var hazelcastInstance: HazelcastInstance?=null
    //    @Before
    @Throws(Exception::class)
    fun setUp() {
        val config = Config()
        hazelcastInstance = Hazelcast.newHazelcastInstance(config)
    }

    //    @Test
    @Throws(Exception::class)
    fun map() {
        val map = hazelcastInstance!!.getMap<String, String>("map")
        map.put("n", "m")
        assertEquals(map.size.toLong(), 1)
        assertEquals(map["n"], "m")
        map.remove("n")
        assertEquals(map.size.toLong(), 0)
    }

    //    @After
    @Throws(Exception::class)
    fun tearDown() {

    }

}