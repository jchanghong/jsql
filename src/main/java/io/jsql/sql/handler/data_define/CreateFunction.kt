package io.jsql.sql.handler.data_define

import io.jsql.sql.OConnection

/**
 * Created by 长宏 on 2017/3/18 0018.
 */
object CreateFunction {
    fun isme(sql: String): Boolean {
        val sqll = sql.toUpperCase().trim { it <= ' ' }
        val list = sqll.split("\\s+".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        return list.size > 2 && list[0] == "CREATE" && list[1] == "FUNCTION"
    }

    fun handle(sql: String, c: OConnection) {
        c.writeok()
    }
}
