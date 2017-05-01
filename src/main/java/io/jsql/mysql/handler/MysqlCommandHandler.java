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
package io.jsql.mysql.handler;


import io.jsql.config.ErrorCode;
import io.jsql.databaseorient.adapter.MDBadapter;
import io.jsql.mysql.MySQLMessage;
import io.jsql.mysql.mysql.CommandPacket;
import io.jsql.mysql.mysql.MySQLPacket;
import io.jsql.orientserver.OConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;

/**
 * 前端命令处理器
 * 处理命令包
 */
public class MysqlCommandHandler implements MysqlPacketHander {
    public static Logger logger = LoggerFactory.getLogger(MysqlCommandHandler.class.getSimpleName());

    protected final OConnection source;

    public MysqlCommandHandler(OConnection connection) {
        this.source = connection;
    }

    @Override
    public void hander(MySQLPacket mySQLPacket) {
        handle((CommandPacket) mySQLPacket);
    }

    public void handle(CommandPacket data) {
        logger.debug(data.toString());
        logger.info("command info");

//        if(source.getLoadDataInfileHandler()!=null&&source.getLoadDataInfileHandler().isStartLoadData())
//        {
//            MySQLMessage mm = new MySQLMessage(data);
//            int  packetLength = mm.readUB3();
//            if(packetLength+4==data.length)
//            {
//                source.loadDataInfileData(data);
//            }
//            return;
//        }
        switch (data.command) {
            case MySQLPacket.COM_INIT_DB:
                initDB(data);
                break;
            case MySQLPacket.COM_QUERY:
                query(data);
                break;
            case MySQLPacket.COM_PING:
                ping();
                break;
            case MySQLPacket.COM_QUIT:
                close("quit cmd");
                break;
            case MySQLPacket.COM_PROCESS_KILL:
                kill(data);
                break;
            case MySQLPacket.COM_STMT_PREPARE:
                stmtPrepare(data);
                break;
            case MySQLPacket.COM_STMT_SEND_LONG_DATA:
                stmtSendLongData(data);
                break;
            case MySQLPacket.COM_STMT_RESET:
                stmtReset(data);
                break;
            case MySQLPacket.COM_STMT_EXECUTE:
                stmtExecute(data);
                break;
            case MySQLPacket.COM_STMT_CLOSE:
                stmtClose(data);
                break;
            case MySQLPacket.COM_HEARTBEAT:
                heartbeat(data);
                break;
            default:
                source.writeErrMessage(ErrorCode.ER_UNKNOWN_COM_ERROR,
                        "Unknown command");

        }
    }

    private void heartbeat(CommandPacket data) {
        source.writeok();
    }

    private void stmtClose(CommandPacket data) {
        source.writeok();
    }

    private void stmtExecute(CommandPacket data) {
        source.writeok();
    }

    private void stmtReset(CommandPacket data) {
        source.writeok();
    }

    private void stmtSendLongData(CommandPacket data) {
        source.writeok();
    }

    private void stmtPrepare(CommandPacket data) {
        source.writeok();
    }

    private void kill(CommandPacket data) {
        source.close(data.toString());

    }

    private void close(String s) {
        source.close(s);

    }

    private void ping() {
        source.writeok();
    }

    private void query(CommandPacket data) {
        MySQLMessage mm = new MySQLMessage(data.arg);
        mm.position(0);
        try {
            String sql = mm.readString(source.charset);
            source.sqlHander.handle(sql);
        } catch (UnsupportedEncodingException e) {
            source.writeErrMessage(ErrorCode.ER_UNKNOWN_CHARACTER_SET, "Unknown charset '" + source.charset + "'");
            e.printStackTrace();
        }
    }

    private void initDB(CommandPacket data) {
        MySQLMessage mm = new MySQLMessage(data.arg);
        mm.position(0);
        String db = mm.readString();
        // 检查schema的有效性
        if (!MDBadapter.dbset.contains(db)) {
            source.writeErrMessage(ErrorCode.ER_BAD_DB_ERROR, "Unknown database '" + db + "'");
            return;
        }
        source.schema = db;
        MDBadapter.currentDB = db;
        source.writeok();
    }


}