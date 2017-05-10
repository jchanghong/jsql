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
package io.jsql.mysql.mysql


import io.netty.buffer.ByteBuf
import io.netty.channel.Channel

/**
 * @author jsql
 * *
 * @author changhong
 */
abstract class MySQLPacket {

    //包体
    var packetLength: Int = 0
    var packetId: Byte = 0


    /**
     * 把数据包发送给客服端
     */
    open fun write(c: Channel) {
        throw UnsupportedOperationException()
    }

    /**
     * 把数据包发送给一个buff保存
     */
    open fun write(buffer: ByteBuf) {
        throw UnsupportedOperationException()
    }

    /**
     * 把字节数组转变成Packet
     */
    abstract fun read(data: ByteArray)

    /**
     * 计算数据包大小，不包含包头长度。
     */
    abstract fun calcPacketSize(): Int

    /**
     * 取得数据包信息
     */
    protected abstract val packetInfo: String

    override fun toString(): String {
        return packetInfo + "{length=" + packetLength + ",id=" +
                packetId + '}'
    }

    companion object {
        /**
         * none, this is an internal thread state
         */
        val COM_SLEEP: Byte = 0

        /**
         * mysql_close
         */
        val COM_QUIT: Byte = 1

        /**
         * mysql_select_db
         */
        val COM_INIT_DB: Byte = 2

        /**
         * mysql_real_query
         */
        val COM_QUERY: Byte = 3

        /**
         * mysql_list_fields
         */
        val COM_FIELD_LIST: Byte = 4

        /**
         * mysql_create_db (deprecated)
         */
        val COM_CREATE_DB: Byte = 5

        /**
         * mysql_drop_db (deprecated)
         */
        val COM_DROP_DB: Byte = 6

        /**
         * mysql_refresh
         */
        val COM_REFRESH: Byte = 7

        /**
         * mysql_shutdown
         */
        val COM_SHUTDOWN: Byte = 8

        /**
         * mysql_stat
         */
        val COM_STATISTICS: Byte = 9

        /**
         * mysql_list_processes
         */
        val COM_PROCESS_INFO: Byte = 10

        /**
         * none, this is an internal thread state
         */
        val COM_CONNECT: Byte = 11

        /**
         * mysql_kill
         */
        val COM_PROCESS_KILL: Byte = 12

        /**
         * mysql_dump_debug_info
         */
        val COM_DEBUG: Byte = 13

        /**
         * mysql_ping
         */
        val COM_PING: Byte = 14

        /**
         * none, this is an internal thread state
         */
        val COM_TIME: Byte = 15

        /**
         * none, this is an internal thread state
         */
        val COM_DELAYED_INSERT: Byte = 16

        /**
         * mysql_change_user
         */
        val COM_CHANGE_USER: Byte = 17

        /**
         * used by slave server mysqlbinlog
         */
        val COM_BINLOG_DUMP: Byte = 18

        /**
         * used by slave server to get master table
         */
        val COM_TABLE_DUMP: Byte = 19

        /**
         * used by slave to log connection to master
         */
        val COM_CONNECT_OUT: Byte = 20

        /**
         * used by slave to register to master
         */
        val COM_REGISTER_SLAVE: Byte = 21

        /**
         * mysql_stmt_prepare
         */
        val COM_STMT_PREPARE: Byte = 22

        /**
         * mysql_stmt_execute
         */
        val COM_STMT_EXECUTE: Byte = 23

        /**
         * mysql_stmt_send_long_data
         */
        val COM_STMT_SEND_LONG_DATA: Byte = 24

        /**
         * mysql_stmt_close
         */
        val COM_STMT_CLOSE: Byte = 25

        /**
         * mysql_stmt_reset
         */
        val COM_STMT_RESET: Byte = 26

        /**
         * mysql_set_server_option
         */
        val COM_SET_OPTION: Byte = 27

        /**
         * mysql_stmt_fetch
         */
        val COM_STMT_FETCH: Byte = 28

        /**
         * Mycat heartbeat
         */
        val COM_HEARTBEAT: Byte = 64

        val MAX_COMP_NUMBER = COM_STMT_FETCH
        val MIN_COMP_NUMBER = COM_SLEEP

        //包头大小
        val packetHeaderSize = 4
    }

}