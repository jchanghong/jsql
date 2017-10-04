/*
 * Java-based distributed database like Mysql
 */

package io.jsql.hazelcast.test

import io.jsql.hazelcast.MyHazelcast

/**
 * Created by 长宏 on 2017/5/5 0005.
 */
object Mhazelcasttest {
    @JvmStatic
    fun main(args: Array<String>) {
        val mhazelcast = MyHazelcast()
        mhazelcast.exeSql("create database db3", "d")
    }
}
