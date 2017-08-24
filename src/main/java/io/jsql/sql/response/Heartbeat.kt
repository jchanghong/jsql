/*
 * Java-based distributed database like Mysql
 */
package io.jsql.sql.response

import io.jsql.sql.OConnection
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * @author jsql
 */
object Heartbeat {

    private val HEARTBEAT = LoggerFactory.getLogger("heartbeat")

    fun response(c: OConnection, data: ByteArray) {
        //        HeartbeatPacket hp = new HeartbeatPacket();
        //        hp.read(data);
        //        if (MycatServer.getInstance().isOnline()) {
        //            OkPacket ok = new OkPacket();
        //            ok.packetId = 1;
        //            ok.affectedRows = hp.id;
        //            ok.serverStatus = 2;
        //            ok.write(c);
        //            if (HEARTBEAT.isInfoEnabled()) {
        //                HEARTBEAT.info(responseMessage("OK", c, hp.id));
        //            }
        //        } else {
        //            ErrorPacket error = new ErrorPacket();
        //            error.packetId = 1;
        //            error.errno = ErrorCode.ER_SERVER_SHUTDOWN;
        //            error.message = String.valueOf(hp.id).getBytes();
        //            error.write(c);
        //            if (HEARTBEAT.isInfoEnabled()) {
        //                HEARTBEAT.info(responseMessage("ERROR", c, hp.id));
        //            }
        //        }
    }

    private fun responseMessage(action: String, c: OConnection, id: Long): String? {
        //        return new StringBuilder("RESPONSE:").append(action).append(", id=").append(id).append(", host=")
        //                .append(c.getHost()).append(", port=").append(c.getPort()).append(", time=")
        //                .append(TimeUtil.currentTimeMillis()).toString();
        return null
    }

}