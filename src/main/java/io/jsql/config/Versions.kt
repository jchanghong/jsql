/*
 * Java-based distributed database like Mysql
 */
package io.jsql.config

/**
 * @author jsql
 */
object Versions {

    /**
     * 协议版本
     */
    val PROTOCOL_VERSION: Byte = 10

    /**
     * 服务器版本
     */
    var SERVER_VERSION = "jsql 1.0".toByteArray()

    fun setServerVersion(version: String) {
        val mysqlVersionPart = version.toByteArray()
        var startIndex: Int = 0
        while (startIndex < SERVER_VERSION.size) {
            if (SERVER_VERSION[startIndex] == '_'.toByte())
                break
            startIndex++
        }

        // 重新拼接mycat version字节数组
        val newMycatVersion = ByteArray(mysqlVersionPart.size + SERVER_VERSION.size - startIndex)
        System.arraycopy(mysqlVersionPart, 0, newMycatVersion, 0, mysqlVersionPart.size)
        System.arraycopy(SERVER_VERSION, startIndex, newMycatVersion, mysqlVersionPart.size,
                SERVER_VERSION.size - startIndex)
        SERVER_VERSION = newMycatVersion
    }
}
