/*
 * Java-based distributed database like Mysql
 */
package io.jsql.sql.handler.adminstatement

import io.jsql.config.ErrorCode
import io.jsql.mysql.mysql.OkPacket
import io.jsql.sql.OConnection
import io.jsql.sql.handler.SqlStatementHander
import org.springframework.stereotype.Component

/**
 * @author jsql
 */
@Component
class KillHandler : SqlStatementHander() {

    override fun supportSQLstatement(): Class<out SQLStatement> {
        return MySqlKillStatement::class.java
    }

    @Throws(Exception::class)
    override fun handle0(sqlStatement: SQLStatement, c: OConnection): Any? {
        return null
    }

    companion object {

        fun handle(stmt: String, offset: Int, c: OConnection) {
            val id = stmt.substring(offset).trim { it <= ' ' }
            if (id.isNullOrEmpty()) {
                c.writeErrMessage(ErrorCode.ER_NO_SUCH_THREAD, "NULL connection id")
            } else {
                // get value
                var value: Long = 0
                try {
                    value = java.lang.Long.parseLong(id)
                } catch (e: NumberFormatException) {
                    c.writeErrMessage(ErrorCode.ER_NO_SUCH_THREAD, "Invalid connection id:" + id)
                }

                //            // kill myself
                //            if (value == c.getId()) {
                //                getOkPacket().write(c);
                //                c.write(c.allocate());
                //                return;
                //            }
                //
                //            // get connection and close it
                //            FrontendConnection fc = null;
                //            NIOProcessor[] processors = MycatServer.getInstance().getProcessors();
                //            for (NIOProcessor p : processors) {
                //                if ((fc = p.getFrontends().get(value)) != null) {
                //                    break;
                //                }
                //            }
                //            if (fc != null) {
                //                fc.close("killed");
                //                getOkPacket().write(c);
                //            } else {
                //                c.writeErrMessage(ErrorCode.ER_NO_SUCH_THREAD, "Unknown connection id:" + id);
                //            }
            }
        }

        private val okPacket: OkPacket
            get() {
                val packet = OkPacket()
                packet.packetId = 1
                packet.affectedRows = 0
                packet.serverStatus = 2
                return packet
            }

        fun handle(x: MySqlKillStatement, c: OConnection) {
            val id = x.threadId.toString()
            if (id.isNullOrEmpty()) {
                c.writeErrMessage(ErrorCode.ER_NO_SUCH_THREAD, "NULL connection id")
            } else {
                // get value
                var value: Long = 0
                try {
                    value = java.lang.Long.parseLong(id)
                } catch (e: NumberFormatException) {
                    c.writeErrMessage(ErrorCode.ER_NO_SUCH_THREAD, "Invalid connection id:" + id)
                }

                //            // kill myself
                //            if (value == c.getId()) {
                //                getOkPacket().write(c);
                //                c.write(c.allocate());
                //                return;
                //            }
                //
                //            // get connection and close it
                //            FrontendConnection fc = null;
                //            NIOProcessor[] processors = MycatServer.getInstance().getProcessors();
                //            for (NIOProcessor p : processors) {
                //                if ((fc = p.getFrontends().get(value)) != null) {
                //                    break;
                //                }
                //            }
                //            if (fc != null) {
                //                fc.close("killed");
                //                getOkPacket().write(c);
                //            } else {
                //                c.writeErrMessage(ErrorCode.ER_NO_SUCH_THREAD, "Unknown connection id:" + id);
                //            }
                //        }
            }
        }
    }
}