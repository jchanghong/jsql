/*
 * Java-based distributed database like Mysql
 */

package io.jsql.sql.handler.data_define

import io.jsql.sql.OConnection

/**
 * Created by 长宏 on 2017/3/18 0018.
 * ALTER INSTANCE ROTATE INNODB MASTER KEY
 * ALTER INSTANCE, introduced in MySQL 5.7.11, defines actions applicable to a MySQL server instance. Using ALTER INSTANCE requires the SUPER privilege.
 *
 *
 * The ALTER INSTANCE ROTATE INNODB MASTER KEY statement is used
 * to rotate the master encryption key used for InnoDB tablespace encryption.
 * A keyring plugin must be loaded to use this statement.
 * For information about keyring plugins,
 * see Section 7.5.4, “The MySQL Keyring”.
 */
object AlterInstall {
    fun isme(sql: String): Boolean {
        val sqll = sql.toUpperCase().trim { it <= ' ' }
        val list = sqll.split("\\s+".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        return list.size > 2 && list[0] == "ALTER" && list[1] == "INSTANCE"
    }

    fun handle(sql: String, c: OConnection) {
        c.writeok()
    }
}
