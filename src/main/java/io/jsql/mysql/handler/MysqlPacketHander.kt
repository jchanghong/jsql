/*
 * Java-based distributed database like Mysql
 */

package io.jsql.mysql.handler

import io.jsql.mysql.mysql.MySQLPacket
import io.jsql.sql.OConnection

/**
 * Created by 长宏 on 2017/4/30 0030.
 * 处理不同的mysql包
 */
interface MysqlPacketHander {
    fun hander(mySQLPacket: MySQLPacket, source: OConnection)
}
