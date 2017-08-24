/*
 * Java-based distributed database like Mysql
 */
package io.jsql.mysql.mysql

/**
 * @author jsql
 * *
 * @author changhong
 */
class PingPacket : MySQLPacket() {

    override fun read(data: ByteArray) {

    }

    override fun calcPacketSize(): Int {
        return 1
    }

    protected override val packetInfo: String
        get() = "MySQL Ping Packet"

    companion object {
        val PING = byteArrayOf(1, 0, 0, 0, 14)
    }

}