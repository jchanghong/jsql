package io.jsql.mysql.mysql

import io.jsql.mysql.MySQLMessage

/**
 * <pre>

 * COM_STMT_SEND_LONG_DATA sends the data for a column. Repeating to send it, appends the data to the parameter.
 * No response is sent back to the client.

 * COM_STMT_SEND_LONG_DATA:
 * COM_STMT_SEND_LONG_DATA
 * direction: client -> server
 * response: none

 * payload:
 * 1              [18] COM_STMT_SEND_LONG_DATA
 * 4              statement-id
 * 2              param-id
 * n              data

</pre> *

 * @author CrazyPig
 * *
 * @author changhong
 * *
 * @see https://dev.mysql.com/doc/internals/en/com-stmt-send-long-data.html

 * @since 2016-09-08
 */
class LongDataPacket : MySQLPacket() {
    var pstmtId: Long = 0
        private set
    var paramId: Long = 0
        private set
    var longData = ByteArray(0)
        private set

    override fun read(data: ByteArray) {
        val mm = MySQLMessage(data)
        packetLength = mm.readUB3()
        packetId = mm.read()
        val code = mm.read()
        assert(code == PACKET_FALG)
        pstmtId = mm.readUB4()
        paramId = mm.readUB2().toLong()
        this.longData = mm.readBytes(packetLength - (1 + 4 + 2))
    }

    override fun calcPacketSize(): Int {
        return 1 + 4 + 2 + this.longData.size
    }

    protected override val packetInfo: String
        get() = "MySQL Long Data Packet"

    companion object {

        private val PACKET_FALG = 24.toByte()
    }


}
