/*
 * Java-based distributed database like Mysql
 */

package io.jsql.my_config

import java.io.FileInputStream
import java.io.IOException
import java.util.*

/**
 * Created by 长宏 on 2017/5/1 0001.
 */
object MyProperties {
    private val filename = "./config/config.properties"
    private val v = HashMap<String, String>()
    private var properties: Properties? = null

    init {

        properties = Properties()
        try {
            properties!!.load(FileInputStream(filename))
            val enum1 = properties!!.propertyNames()//得到配置文件的名字
            while (enum1.hasMoreElements()) {
                val strKey = enum1.nextElement() as String
                val strValue = properties!!.getProperty(strKey)
                //                         System.out.println(strKey + "=" + strValue);
                v.put(strKey, strValue)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    operator fun get(key: String): String? {
        return v[key]
    }

    fun getInt(key: String): Int {
        return Integer.parseInt(get(key).toString())
    }

    fun keys(): Set<String> {
        return v.keys
    }
}
