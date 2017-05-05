package io.jsql.mysql.handler;

import io.jsql.sql.OConnection;

/**
 * Created by 长宏 on 2017/4/30 0030.
 * 处理不同的sql语句
 */
public interface SQLHander {
    void handle(String sql);

    void setConnection(OConnection connection);
}
