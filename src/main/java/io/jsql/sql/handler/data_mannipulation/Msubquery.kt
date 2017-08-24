/*
 * Java-based distributed database like Mysql
 */

package io.jsql.sql.handler.data_mannipulation

import io.jsql.sql.OConnection

/**
 * Created by 长宏 on 2017/3/18 0018.
 * Here is an example of a subquery:
 *
 *
 * SELECT * FROM t1 WHERE column1 = (SELECT column1 FROM t2);
 */
object Msubquery {
    fun isme(sql: String): Boolean {
        //        String sqll = sql.toUpperCase().trim();
        //        String list[] = sqll.split("\\s+");
        //        if (list.length > 2 && list[0].equals("ALTER") && list[1].equals("EVENT")) {
        //            return true;
        //        }
        return false
    }

    fun handle(sql: String, c: OConnection) {
        c.writeok()
    }
}
