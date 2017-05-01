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
package io.jsql.orientserver;

import io.jsql.config.Capabilities;
import io.jsql.config.ErrorCode;
import io.jsql.config.Versions;
import io.jsql.mysql.handler.*;
import io.jsql.mysql.mysql.*;
import io.jsql.util.RandomUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;

/**
 * The type O connection.
 *
 * @author changhong.基于orientdb的服务器 ，9999端口连接
 */
public class OConnection {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(OConnection.class);
    public final boolean txInterrupted;
    public final MysqlPacketHander authhander;
    public final MysqlPacketHander comhander;
    public final SQLHander sqlHander;
    public volatile int charsetIndex;
    public volatile int txIsolation;
    public volatile boolean autocommit;
    public volatile String txInterrputMsg = "";
    public long lastInsertId;
    /**
     * 标志是否执行了lock tables语句，并处于lock状态
     */
    public volatile boolean isLocked = false;
    public String charset = "utf8";
    public String schema;
    public ChannelHandlerContext channelHandlerContext;
    public byte[] seed;
    public boolean authenticated;
    public String user;

    public OConnection() {
        this.txInterrupted = false;
        this.autocommit = true;
        authhander = new MysqlAuthHander(this);
        comhander = new MysqlCommandHandler(this);
        sqlHander = new MysqlSQLhander(this);
    }

    //    public boolean setCharset(String charset) {
//
//        // 修复PHP字符集设置错误, 如： set names 'utf8'
//        if (charset != null) {
//            charset = charset.replace("'", "");
//        }
//
//        int ci = CharsetUtil.getIndex(charset);
//        if (ci > 0) {
//            this.charset = charset.equalsIgnoreCase("utf8mb4") ? "utf8" : charset;
//            this.charsetIndex = ci;
//            return true;
//        } else {
//            return false;
//        }
//    }
    private static byte[] encodeString(String src, String charset) {
        if (src == null) {
            return null;
        }
        if (charset == null) {
            return src.getBytes();
        }
        try {
            return src.getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            return src.getBytes();
        }
    }

    public void writeNotSurrport() {
        writeErrMessage(ErrorCode.ER_NOT_SUPPORTED_YET, "暂时不支持！！！");
    }

    public void writeErrMessage(int errno, String msg) {
        writeErrMessage((byte) 1, errno, msg);
    }

//    public boolean isIdleTimeout() {
//        if (isAuthenticated) {
//            return super.isIdleTimeout();
//        } else {
//            return TimeUtil.currentTimeMillis() > Math.max(lastWriteTime,
//                    lastReadTime) + AUTH_TIMEOUT;
//        }
//    }

//    public int getTxIsolation() {
//        return txIsolation;
//    }

//    public void setTxIsolation(int txIsolation) {
//        this.txIsolation = txIsolation;
//    }

    public void writeErrMessage(byte id, int errno, String msg) {
        ErrorPacket err = new ErrorPacket();
        err.packetId = id;
        err.errno = errno;
        err.message = encodeString(msg, charset);
        err.write(channelHandlerContext.channel());
    }

//    /**
//     * 提交事务
//     */
//    public void commit() {
//        if (txInterrupted) {
//            writeErrMessage(ErrorCode.ER_YES,
//                    "Transaction error, need to rollback.");
//        } else {
//        }
//    }

//    /**
//     * 回滚事务
//     */
//    public void rollback() {
//        // 状态检查
//        if (txInterrupted) {
//            txInterrupted = false;
//        }
//
//        // 执行回滚
////        session.rollback();
//    }

//    /**
//     * 执行lock tables语句方法
//     *
//     * @param sql
//     */
//    public void lockTable(String sql) {
//        // 事务中不允许执行lock table语句
//        if (!autocommit) {
//            writeErrMessage(ErrorCode.ER_YES, "can't lock table in transaction!");
//            return;
//        }
//        // 已经执行了lock table且未执行unlock table之前的连接不能再次执行lock table命令
//        if (isLocked) {
//            writeErrMessage(ErrorCode.ER_YES, "can't lock multi-table");
//            return;
//        }
////        RouteResultset rrs = routeSQL(sql, ServerParse.LOCK);
////        if (rrs != null) {
////            session.lockTable(rrs);
////        }
//    }

//    /**
//     * 执行unlock tables语句方法
//     *
//     * @param sql
//     */
//    public void unLockTable(String sql) {
//        sql = sql.replaceAll("\n", " ").replaceAll("\t", " ");
//        String[] words = SplitUtil.split(sql, ' ', true);
//        if (words.length == 2 && ("table".equalsIgnoreCase(words[1]) || "tables".equalsIgnoreCase(words[1]))) {
//            isLocked = false;
////            session.unLockTable(sql);
//        } else {
//            writeErrMessage(ErrorCode.ER_UNKNOWN_COM_ERROR, "Unknown command");
//        }
//
//    }

    public void close(String reason) {
//        super.close(reason);
//        if (getLoadDataInfileHandler() != null) {
//            getLoadDataInfileHandler().clear();
//        }
        if (channelHandlerContext.channel().isActive()) {
            write(Unpooled.wrappedBuffer(reason.getBytes()));

        }
        channelHandlerContext.close();
    }

    @Override
    public String toString() {
//        return "OConnection [id=" + id + ", schema=" + schema + ", host="
//                + host + ", user=" + user + ",txIsolation=" + txIsolation
//                + ", autocommit=" + autocommit + ", schema=" + schema + "]";
        return super.toString();
    }

    public void writeok() {
        write(Unpooled.wrappedBuffer(OkPacket.OK));
    }

    public void writeErrMessage(String message) {
        writeErrMessage(ErrorCode.ER_BAD_DB_ERROR, message);
    }

    public void register() {
        if (channelHandlerContext != null) {
            // 生成认证数据
            byte[] rand1 = RandomUtil.randomBytes(8);
            byte[] rand2 = RandomUtil.randomBytes(12);

            // 保存认证数据
            byte[] seed = new byte[rand1.length + rand2.length];
            System.arraycopy(rand1, 0, seed, 0, rand1.length);
            System.arraycopy(rand2, 0, seed, rand1.length, rand2.length);
            this.seed = seed;

            // 发送握手数据包
            boolean useHandshakeV10 = true;
            if (useHandshakeV10) {
                HandshakeV10Packet hs = new HandshakeV10Packet();
                hs.packetId = 0;
                hs.protocolVersion = Versions.PROTOCOL_VERSION;
                hs.serverVersion = Versions.SERVER_VERSION;
                hs.threadId = 0;
                hs.seed = rand1;
                hs.serverCapabilities = getServerCapabilities();
                hs.serverCharsetIndex = (byte) (charsetIndex & 0xff);
                hs.serverStatus = 2;
                hs.restOfScrambleBuff = rand2;
                hs.write(channelHandlerContext.channel());
            } else {
                HandshakePacket hs = new HandshakePacket();
                hs.packetId = 0;
                hs.protocolVersion = Versions.PROTOCOL_VERSION;
                hs.serverVersion = Versions.SERVER_VERSION;
                hs.threadId = 0;
                hs.seed = rand1;
                hs.serverCapabilities = getServerCapabilities();
                hs.serverCharsetIndex = (byte) (charsetIndex & 0xff);
                hs.serverStatus = 2;
                hs.restOfScrambleBuff = rand2;
                hs.write(channelHandlerContext.channel());
            }
        }
    }

    public ByteBuf allocate() {
        return Unpooled.buffer(256);
    }

    public void removebuff(ByteBuf buf) {
        buf.release();
        buf = null;
    }

    private int getServerCapabilities() {
        int flag = 0;
        flag |= Capabilities.CLIENT_LONG_PASSWORD;
        flag |= Capabilities.CLIENT_FOUND_ROWS;
        flag |= Capabilities.CLIENT_LONG_FLAG;
        flag |= Capabilities.CLIENT_CONNECT_WITH_DB;
        // flag |= Capabilities.CLIENT_NO_SCHEMA;
        boolean usingCompress = false;
        if (usingCompress) {
            flag |= Capabilities.CLIENT_COMPRESS;
        }

        flag |= Capabilities.CLIENT_ODBC;
        flag |= Capabilities.CLIENT_LOCAL_FILES;
        flag |= Capabilities.CLIENT_IGNORE_SPACE;
        flag |= Capabilities.CLIENT_PROTOCOL_41;
        flag |= Capabilities.CLIENT_INTERACTIVE;
        // flag |= Capabilities.CLIENT_SSL;
        flag |= Capabilities.CLIENT_IGNORE_SIGPIPE;
        flag |= Capabilities.CLIENT_TRANSACTIONS;
        // flag |= ServerDefs.CLIENT_RESERVED;
        flag |= Capabilities.CLIENT_SECURE_CONNECTION;
        flag |= Capabilities.CLIENT_MULTI_STATEMENTS;
        flag |= Capabilities.CLIENT_MULTI_RESULTS;
        boolean useHandshakeV10 = true;
        if (useHandshakeV10) {
            flag |= Capabilities.CLIENT_PLUGIN_AUTH;
        }
        return flag;
    }

    public void handerAuth(AuthPacket authPacket) {
        authhander.hander(authPacket);
    }

    public void handerCommand(CommandPacket commandPacket) {
        comhander.hander(commandPacket);
    }

    public void write(ByteBuf byteBuf) {
        channelHandlerContext.writeAndFlush(byteBuf);
    }

    public void writeResultSet(MySQLPacket header, MySQLPacket[] filed, MySQLPacket eof1, MySQLPacket[] rows, MySQLPacket eof2) {

        ByteBuf buf = allocate();
        header.write(buf);
        for (MySQLPacket packet : filed) {
            packet.write(buf);
        }
        eof1.write(buf);
        for (MySQLPacket row : rows) {
            row.write(buf);
        }
        eof2.write(buf);
//        byte[] bytes = new byte[buf.readableBytes()];
//        buf.readBytes(bytes);
        channelHandlerContext.channel().writeAndFlush(buf);
    }
}
