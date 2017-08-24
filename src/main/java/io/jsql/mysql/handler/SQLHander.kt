/*
 * Java-based distributed database like Mysql
 */

package io.jsql.mysql.handler

import io.jsql.sql.OConnection

/**
 * Created by 长宏 on 2017/4/30 0030.
 * 处理不同的sql语句
 */
interface SQLHander {
    fun handle(sql: String, source: OConnection)
}
