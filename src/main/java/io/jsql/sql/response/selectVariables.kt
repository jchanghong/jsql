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
package io.jsql.sql.response

import com.google.common.base.Splitter
import io.jsql.config.Fields
import io.jsql.mysql.PacketUtil
import io.jsql.mysql.mysql.*
import io.jsql.sql.OConnection
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import java.util.ArrayList
import java.util.HashMap

/**
 * @author changhong
 */
object selectVariables {
    private val LOGGER = LoggerFactory.getLogger(selectVariables::class.java)
    private val variables = HashMap<String, String>()

    init {
        variables.put("@@character_set_client", "utf8")
        variables.put("@@character_set_connection", "utf8")
        variables.put("@@character_set_results", "utf8")
        variables.put("@@character_set_server", "utf8")
        variables.put("@@init_connect", "")
        variables.put("@@interactive_timeout", "172800")
        variables.put("@@license", "GPL")
        variables.put("@@lower_case_table_names", "1")
        variables.put("@@max_allowed_packet", "16777216")
        variables.put("@@net_buffer_length", "16384")
        variables.put("@@net_write_timeout", "60")
        variables.put("@@query_cache_size", "0")
        variables.put("@@query_cache_type", "OFF")
        variables.put("@@sql_mode", "STRICT_TRANS_TABLES")
        variables.put("@@system_time_zone", "CST")
        variables.put("@@time_zone", "SYSTEM")
        variables.put("@@tx_isolation", "REPEATABLE-READ")
        variables.put("@@wait_timeout", "172800")
        variables.put("@@session.auto_increment_increment", "1")

        variables.put("character_set_client", "utf8")
        variables.put("character_set_connection", "utf8")
        variables.put("character_set_results", "utf8")
        variables.put("character_set_server", "utf8")
        variables.put("init_connect", "")
        variables.put("interactive_timeout", "172800")
        variables.put("license", "GPL")
        variables.put("lower_case_table_names", "1")
        variables.put("max_allowed_packet", "16777216")
        variables.put("net_buffer_length", "16384")
        variables.put("net_write_timeout", "60")
        variables.put("query_cache_size", "0")
        variables.put("query_cache_type", "OFF")
        variables.put("sql_mode", "STRICT_TRANS_TABLES")
        variables.put("system_time_zone", "CST")
        variables.put("time_zone", "SYSTEM")
        variables.put("tx_isolation", "REPEATABLE-READ")
        variables.put("wait_timeout", "172800")
        variables.put("auto_increment_increment", "1")
    }

    fun execute(c: OConnection, sql: String) {

        val subSql = sql.substring(sql.indexOf("SELECT") + 6)
        var splitVar = Splitter.on(",").omitEmptyStrings().trimResults().splitToList(subSql)
        splitVar = convert(splitVar)
        val FIELD_COUNT = splitVar.size
        val header = PacketUtil.getHeader(FIELD_COUNT)
        val fields = arrayOfNulls<FieldPacket>(FIELD_COUNT)
        var i = 0
        var packetId: Byte = 0
        header.packetId = ++packetId
        run {
            var i1 = 0
            val splitVarSize = splitVar.size
            while (i1 < splitVarSize) {
                val s = splitVar[i1]
                fields[i] = PacketUtil.getField(s, Fields.FIELD_TYPE_VAR_STRING)
                fields[i++]!!.packetId = ++packetId
                i1++
            }
        }

        val eof = EOFPacket()
        eof.packetId = ++packetId

        // write rows
        //        byte packetId = eof.packetId;

        val row = RowDataPacket(FIELD_COUNT)
        var i1 = 0
        val splitVarSize = splitVar.size
        while (i1 < splitVarSize) {
            val s = splitVar[i1]
            val value = if (variables[s] == null) "" else variables[s]
            row.add(value!!.toByteArray())
            i1++

        }

        row.packetId = ++packetId

        // write lastEof
        val lastEof = EOFPacket()
        lastEof.packetId = ++packetId
        c.writeResultSet(header, fields as Array<MySQLPacket>, eof, arrayOf(row), lastEof)
    }

    private fun convert(`in`: List<String>): List<String> {
        val out = ArrayList<String>()
        for (s in `in`) {
            val asIndex = s.toUpperCase().indexOf(" AS ")
            if (asIndex != -1) {
                out.add(s.substring(asIndex + 4))
            }
        }
        if (out.isEmpty()) {
            return `in`
        } else {
            return out
        }


    }


}