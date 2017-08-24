/*
 * Java-based distributed database like Mysql
 */

package io.jsql.mysql.mysql

import io.jsql.mysql.MySQLMessage

/**
 * <pre>

 * COM_STMT_RESET resets the data of a prepared statement which was accumulated with COM_STMT_SEND_LONG_DATA commands and closes the cursor if it was opened with COM_STMT_EXECUTE

 * The server will send a OK_Packet if the statement could be reset, a ERR_Packet if not.

 * COM_STMT_RESET:
 * COM_STMT_RESET
 * direction: client -> server
 * response: OK or ERR

 * payload:
 * 1              [1a] COM_STMT_RESET
 * 4              statement-id

</pre> *

 * @author CrazyPig
 * *
 * @author changhong
 * *
 * @since 2016-09-08
 */
class ResetPacket : MySQLPacket() {
    var pstmtId: Long = 0
        private set

    override fun read(data: ByteArray) {
        val mm = MySQLMessage(data)
        packetLength = mm.readUB3()
        packetId = mm.read()
        val code = mm.read()
        assert(code == PACKET_FALG)
        pstmtId = mm.readUB4()
    }

    override fun calcPacketSize(): Int {
        return 1 + 4
    }

    protected override val packetInfo: String
        get() = "MySQL Reset Packet"

    companion object {

        private val PACKET_FALG = 26.toByte()
    }

}
