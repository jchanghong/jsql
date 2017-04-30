package io.jsql.mysql.handler;

import io.jsql.mysql.mysql.MySQLPacket;

/**
 * Created by 长宏 on 2017/4/30 0030.
 * 处理不同的mysql包
 */
public interface MysqlPacketHander {
    void hander(MySQLPacket mySQLPacket);
}
