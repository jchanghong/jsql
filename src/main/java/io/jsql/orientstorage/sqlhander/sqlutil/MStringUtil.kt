/*
 * Java-based distributed database like Mysql
 */

package io.jsql.orientstorage.sqlhander.sqlutil

/**
 * Created by 长宏 on 2016/12/17 0017.
 */
object MStringUtil {
    /**
     * Gets .
     * 去掉2边的‘
     * 号’

     * @param filed the filed
     * *
     * @return the
     */
    fun getfiledname(filed: String): String {
        if (filed.startsWith("'") || filed.startsWith("`")) {
            return filed.substring(1, filed.length - 1)
        } else {
            return filed
        }
    }
}
