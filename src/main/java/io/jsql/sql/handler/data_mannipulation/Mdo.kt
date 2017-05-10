package io.jsql.sql.handler.data_mannipulation

import io.jsql.sql.OConnection

/**
 * Created by 长宏 on 2017/3/18 0018.
 * mysql> SELECT SLEEP(5);
 * +----------+
 * | SLEEP(5) |
 * +----------+
 * |        0 |
 * +----------+
 * 1 row in set (5.02 sec)
 * DO, on the other hand, pauses without producing a result set.:
 *
 *
 * mysql> DO SLEEP(5);
 * Query OK, 0 rows affected (4.99 sec)
 */
//@Component 不支持的解析语句，，，所有不用实现hander接口
object Mdo {
    fun isme(sql: String): Boolean {
        val sqll = sql.toUpperCase().trim { it <= ' ' }
        val list = sqll.split("\\s+".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        return list.size > 1 && list[0] == "DO"
    }

    fun handle(sql: String, c: OConnection) {
        c.writeok()
    }

}
