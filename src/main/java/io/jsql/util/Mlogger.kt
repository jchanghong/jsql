/*
 * Java-based distributed database like Mysql
 */

package io.jsql.util

import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Created by 长宏 on 2017/5/5 0005.
 */
interface Mlogger {
    fun getlogger(): Logger {
        return LoggerFactory.getLogger(this.javaClass.name)
    }

    companion object {
        val PRINT_WRITER = System.out
    }
}
