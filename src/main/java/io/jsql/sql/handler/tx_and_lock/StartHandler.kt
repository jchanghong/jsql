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
package io.jsql.sql.handler.tx_and_lock

import com.alibaba.druid.sql.ast.SQLStatement
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlStartTransactionStatement
import io.jsql.sql.OConnection
import io.jsql.sql.handler.SqlStatementHander
import io.netty.buffer.Unpooled

/**
 * @author 完成
 */
object StartHandler: SqlStatementHander() {
    private val AC_OFF = byteArrayOf(7, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0)
    override fun supportSQLstatement(): Class<out SQLStatement> {
       return MySqlStartTransactionStatement::class.java
    }

    override fun handle0(sqlStatement: SQLStatement, c: OConnection): Any? {
        return null
    }

    fun handle(stmt: String, c: OConnection, offset: Int) {

        if (c.autocommit) {
            c.autocommit = false
            c.write(Unpooled.wrappedBuffer(AC_OFF))
        } else {
            //                    c.getSession2().commit();
            c.writeok()
        }

    }

    fun handle(x: MySqlStartTransactionStatement, c: OConnection) {
        c.writeok()
    }
}