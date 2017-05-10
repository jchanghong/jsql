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

import com.alibaba.druid.sql.ast.statement.SQLShowTablesStatement
import com.alibaba.druid.sql.dialect.mysql.ast.statement.*
import io.jsql.orientstorage.constant.MvariableTable
import io.jsql.sql.OConnection
import io.jsql.sql.handler.data_mannipulation.MselectVariables
import io.jsql.sql.response.MSelect1Response
import io.jsql.sql.response.MShowDatabases
import io.jsql.sql.response.MShowTables

import java.util.ArrayList

/**
 * @author changhong show开头的语句
 */
object ShowHandler {

    fun showcolumn(x: MySqlShowColumnsStatement, connection: OConnection) {
        MSelect1Response.response(connection, "no", ArrayList<String>())
    }

    fun showdatabase(x: MySqlShowDatabasesStatement, c: OConnection) {
        //        MShowDatabases.response(c);
    }

    fun showwarnings(x: MySqlShowWarningsStatement, connection: OConnection) {
        MSelect1Response.response(connection, "no", ArrayList<String>())

    }

    // SHOW SESSION STATUS LIKE 'Ssl_cipher';
    fun showstatus(x: MySqlShowStatusStatement, connection: OConnection) {
        val likes = x.like.toString()
        MSelect1Response.response(connection, likes, ArrayList<String>())

    }

    fun showauthors(x: MySqlShowAuthorsStatement, connection: OConnection) {
        MSelect1Response.response(connection, "no", ArrayList<String>())
    }

    fun showbinarylogs(x: MySqlShowBinaryLogsStatement, connection: OConnection) {
        MSelect1Response.response(connection, "no", ArrayList<String>())
    }

    fun showmasterlogs(x: MySqlShowMasterLogsStatement, connection: OConnection) {
        MSelect1Response.response(connection, "no", ArrayList<String>())
    }

    fun showcollation(x: MySqlShowCollationStatement, connection: OConnection) {
        MSelect1Response.response(connection, "no", ArrayList<String>())
    }

    fun showbinlogevent(x: MySqlShowBinLogEventsStatement, connection: OConnection) {
        MSelect1Response.response(connection, "no", ArrayList<String>())
    }

    fun showcharater(x: MySqlShowCharacterSetStatement, connection: OConnection) {
        MSelect1Response.response(connection, "no", ArrayList<String>())
    }

    fun showcontributors(x: MySqlShowContributorsStatement, connection: OConnection) {
        MSelect1Response.response(connection, "no", ArrayList<String>())
    }

    fun showcreatedatabase(x: MySqlShowCreateDatabaseStatement, connection: OConnection) {
        MSelect1Response.response(connection, "no", ArrayList<String>())
    }

    fun showcreateevent(x: MySqlShowCreateEventStatement, connection: OConnection) {
        MSelect1Response.response(connection, "no", ArrayList<String>())
    }

    fun showcreatefunction(x: MySqlShowCreateFunctionStatement, connection: OConnection) {
        MSelect1Response.response(connection, "no", ArrayList<String>())
    }

    fun showcreateproccedure(x: MySqlShowCreateProcedureStatement, connection: OConnection) {
        MSelect1Response.response(connection, "no", ArrayList<String>())
    }

    fun showcreatetable(x: MySqlShowCreateTableStatement, connection: OConnection) {
        MSelect1Response.response(connection, "no", ArrayList<String>())
    }

    fun showcreatetrigger(x: MySqlShowCreateTriggerStatement, connection: OConnection) {
        MSelect1Response.response(connection, "no", ArrayList<String>())
    }

    fun showcreateview(x: MySqlShowCreateViewStatement, connection: OConnection) {
        MSelect1Response.response(connection, "no", ArrayList<String>())
    }

    fun showengine(x: MySqlShowEngineStatement, connection: OConnection) {
        MSelect1Response.response(connection, "no", ArrayList<String>())
    }

    fun showengines(x: MySqlShowEnginesStatement, connection: OConnection) {
        MSelect1Response.response(connection, "no", ArrayList<String>())
    }

    fun showerrors(x: MySqlShowErrorsStatement, connection: OConnection) {
        MSelect1Response.response(connection, "no", ArrayList<String>())
    }

    fun showevents(x: MySqlShowEventsStatement, connection: OConnection) {
        MSelect1Response.response(connection, "no", ArrayList<String>())
    }

    fun showfunctioncode(x: MySqlShowFunctionCodeStatement, connection: OConnection) {
        MSelect1Response.response(connection, "no", ArrayList<String>())
    }

    fun showfunctionstatus(x: MySqlShowFunctionStatusStatement, connection: OConnection) {
        MSelect1Response.response(connection, "no", ArrayList<String>())
    }

    fun showgrants(x: MySqlShowGrantsStatement, connection: OConnection) {
        MSelect1Response.response(connection, "no", ArrayList<String>())
    }

    fun showindexs(x: MySqlShowIndexesStatement, connection: OConnection) {
        MSelect1Response.response(connection, "no", ArrayList<String>())
    }

    fun showkeys(x: MySqlShowKeysStatement, connection: OConnection) {
        MSelect1Response.response(connection, "no", ArrayList<String>())
    }

    fun showmasterstatus(x: MySqlShowMasterStatusStatement, connection: OConnection) {
        MSelect1Response.response(connection, "no", ArrayList<String>())
    }

    fun showopentable(x: MySqlShowOpenTablesStatement, connection: OConnection) {
        MSelect1Response.response(connection, "no", ArrayList<String>())
    }

    fun showplugins(x: MySqlShowPluginsStatement, connection: OConnection) {
        MSelect1Response.response(connection, "no", ArrayList<String>())
    }

    fun showprivileges(x: MySqlShowPrivilegesStatement, connection: OConnection) {
        MSelect1Response.response(connection, "no", ArrayList<String>())
    }

    fun showprovedureode(x: MySqlShowProcedureCodeStatement, connection: OConnection) {
        MSelect1Response.response(connection, "no", ArrayList<String>())
    }

    fun showproedurestatus(x: MySqlShowProcedureStatusStatement, connection: OConnection) {
        MSelect1Response.response(connection, "no", ArrayList<String>())
    }

    fun showprocesslist(x: MySqlShowProcessListStatement, connection: OConnection) {
        MSelect1Response.response(connection, "no", ArrayList<String>())
    }

    fun showprofile(x: MySqlShowProfileStatement, connection: OConnection) {
        MSelect1Response.response(connection, "no", ArrayList<String>())
    }

    fun showprofiles(x: MySqlShowProfilesStatement, connection: OConnection) {
        MSelect1Response.response(connection, "no", ArrayList<String>())
    }

    fun showrelaylog(x: MySqlShowRelayLogEventsStatement, connection: OConnection) {
        MSelect1Response.response(connection, "no", ArrayList<String>())
    }

    fun showslavehosts(x: MySqlShowSlaveHostsStatement, connection: OConnection) {
        MSelect1Response.response(connection, "no", ArrayList<String>())
    }

    fun showslavestatus(x: MySqlShowSlaveStatusStatement, connection: OConnection) {
        MSelect1Response.response(connection, "no", ArrayList<String>())
    }

    fun showtablestatus(x: MySqlShowTableStatusStatement, connection: OConnection) {
        MSelect1Response.response(connection, "no", ArrayList<String>())
    }

    fun showtriggers(x: MySqlShowTriggersStatement, connection: OConnection) {
        MSelect1Response.response(connection, "no", ArrayList<String>())
    }

    //SHOW VARIABLES WHERE Variable_name ='language' OR
    // Variable_name = 'net_write_timeout'
    // OR Variable_name = 'interactive_timeout'
    // OR Variable_name = 'wait_timeout'
    // OR Variable_name = 'character_set_client'
    // OR Variable_name = 'character_set_connection'
    // OR Variable_name = 'character_set'
    // OR Variable_name = 'character_set_server'
    // OR Variable_name = 'tx_isolation'
    // OR Variable_name = 'transaction_isolation'
    // OR Variable_name = 'character_set_results'
    // OR Variable_name = 'timezone'
    // OR Variable_name = 'time_zone'
    // OR Variable_name = 'system_time_zone'
    // OR Variable_name = 'lower_case_table_names'
    // OR Variable_name = 'max_allowed_packet'
    // OR Variable_name = 'net_buffer_length'
    // OR Variable_name = 'sql_mode'
    // OR Variable_name = 'query_cache_type'
    // OR Variable_name = 'query_cache_size'
    // OR Variable_name = 'license' OR Variable_name = 'init_connect'
    fun showvatiants(x: MySqlShowVariantsStatement, connection: OConnection) {
        var sql = x.toString()
        if (x.where == null) {
            sql = "select * from " + MvariableTable.tablename
        } else {
            val builder = StringBuilder(sql)
            val index = sql.toLowerCase().indexOf("where")
            sql = "select * from " + MvariableTable.tablename + " " + builder.substring(index)
        }
        MselectVariables.handle(connection, sql)
    }

    fun showtables(x: SQLShowTablesStatement, connection: OConnection) {
        //        MShowTables.response(connection, x, 0);
    }
}