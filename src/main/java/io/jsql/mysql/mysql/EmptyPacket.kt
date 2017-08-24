/*
 * Java-based distributed database like Mysql
 */
package io.jsql.mysql.mysql

/**
 * @author jsql
 * *
 * @author changhong
 * *         暂时只发现在load data infile时用到
 */
class EmptyPacket : MySQLPacket() {

    override fun read(data: ByteArray) {

    }

    override fun calcPacketSize(): Int {
        return 0
    }

    protected override val packetInfo: String
        get() = "MySQL Empty Packet"

    companion object {
        val EMPTY = byteArrayOf(0, 0, 0, 3)
    }

}