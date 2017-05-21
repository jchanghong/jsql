/*
 * Copyright (c) 2013, OpenCloudDB/MyCAT and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software;Designed and Developed mainly by many Chinese 
 * opensource volunteers. you can redistribute it and/or modify it under the 
 * terms of the GNU General Public License version 2 only, as published by the
 * Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 * 
 * Any questions about this component can be directed to it's project Web address 
 * https://code.google.com/p/opencloudb/.
 *
 */
package io.jsql.sql.handler.adminstatement

import com.alibaba.druid.sql.ast.SQLStatement
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlKillStatement
import io.jsql.config.ErrorCode
import io.jsql.mysql.mysql.OkPacket
import io.jsql.sql.OConnection
import io.jsql.sql.handler.SqlStatementHander
import io.jsql.util.StringUtil
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