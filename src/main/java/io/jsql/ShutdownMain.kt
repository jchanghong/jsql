/*
 * Java-based distributed database like Mysql
 */

package io.jsql

import io.jsql.shutdown.Shutdown

/**
 * Created by 长宏 on 2017/5/4 0004.
 * 关闭本机服务器
 */
object ShutdownMain {
    @JvmStatic fun main(args: Array<String>) {
        try {
            Shutdown.main1(args)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}
