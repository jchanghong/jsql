/*
 * Java-based distributed database like Mysql
 */

package io.jsql.sql.handler.data_define

import io.jsql.sql.OConnection

/**
 * Created by 长宏 on 2017/3/18 0018.
 * ALTER SERVER  server_name
 * OPTIONS (option [, option] ...)
 * Alters the server information for server_name,
 * adjusting any of the options permitted in the CREATE SERVER statement.
 * The corresponding fields in the mysql.servers table are updated accordingly.
 * This statement requires the SUPER privilege.
 *
 *
 * For example, to update the USER option:
 *
 *
 * ALTER SERVER s OPTIONS (USER 'sally');
 * ALTER SERVER does not cause an automatic commit.
 */
object AlterServer {
    fun isme(sql: String): Boolean {
        val sqll = sql.toUpperCase().trim { it <= ' ' }
        val list = sqll.split("\\s+".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        return list.size > 2 && list[0] == "ALTER" && list[1] == "SERVER"
    }

    fun handle(sql: String, c: OConnection) {
        c.writeok()
    }
}
