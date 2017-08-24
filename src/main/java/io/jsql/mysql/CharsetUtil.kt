/*
 * Java-based distributed database like Mysql
 */
package io.jsql.mysql

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import java.util.HashMap

/**
 * @author jsql
 */
object CharsetUtil {
    val logger = LoggerFactory
            .getLogger(CharsetUtil::class.java)
    private val INDEX_TO_CHARSET = HashMap<Int, String>()
    private val CHARSET_TO_INDEX = HashMap<String, Int>()

    init {

        // index_to_charset.properties
        INDEX_TO_CHARSET.put(1, "big5")
        INDEX_TO_CHARSET.put(8, "latin1")
        INDEX_TO_CHARSET.put(9, "latin2")
        INDEX_TO_CHARSET.put(14, "cp1251")
        INDEX_TO_CHARSET.put(28, "gbk")
        INDEX_TO_CHARSET.put(24, "gb2312")
        INDEX_TO_CHARSET.put(33, "utf8")
        INDEX_TO_CHARSET.put(45, "utf8mb4")

        //        String filePath = Thread.currentThread().getContextClassLoader()
        //                .getResource("").getPath().replaceAll("%20", " ")
        //                + "index_to_charset.properties";
        //        Properties prop = new Properties();
        //        try {
        //            prop.load(new FileInputStream(filePath));
        //            for (Object index : prop.keySet()){
        //               INDEX_TO_CHARSET.put(Integer.parseInt((String) index), prop.getProperty((String) index));
        //            }
        //        } catch (Exception e) {
        //            logger.error("error:",e);
        //        }
        //
        // charset --> index
        for (key in INDEX_TO_CHARSET.keys) {
            val charset = INDEX_TO_CHARSET[key]
            if (charset != null && CHARSET_TO_INDEX[charset] == null) {
                CHARSET_TO_INDEX.put(charset, key)
            }
        }

        CHARSET_TO_INDEX.put("iso-8859-1", 14)
        CHARSET_TO_INDEX.put("iso_8859_1", 14)
        CHARSET_TO_INDEX.put("utf-8", 33)
    }

    fun getCharset(index: Int): String {
        return INDEX_TO_CHARSET!![index]!!
    }

    fun getIndex(charset: String?): Int {
        if (charset == null || charset.length == 0) {
            return 0
        } else {
            val i = CHARSET_TO_INDEX[charset.toLowerCase()]
            return i ?: 0
        }
    }


}
