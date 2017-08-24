/*
 * Java-based distributed database like Mysql
 */

package io.jsql.test

import org.junit.After
import org.junit.Before
import org.junit.Test

import org.junit.Assert.assertEquals

/**
 * Created by 长宏 on 2017/5/3 0003.
 */
class OrientdbTest {
    @Before
    @Throws(Exception::class)
    fun setUp() {
        assertEquals(1, 1)
    }

    @Test
    @Throws(Exception::class)
    fun test1() {
        assertEquals(1, 1)
    }

    @Test
    @Throws(Exception::class)
    fun test2() {
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
    }

}