/*
 * Java-based distributed database like Mysql
 */

package io.jsql.sql

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

import java.util.HashSet

/**
 * Created by 长宏 on 2017/5/2 0002.
 */
@Component
class OconnectionPool {
    @Value("\${connection.number.max}")
    private val max: Long = 100
    private val oConnections = HashSet<OConnection>()

    fun checkMax(): Boolean {
        return oConnections.size <= max
    }

    fun add(connection: OConnection) {
        oConnections.add(connection)
    }

    fun remove(connection: OConnection) {
        oConnections.remove(connection)
    }
}
