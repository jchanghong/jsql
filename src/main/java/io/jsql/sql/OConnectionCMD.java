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
package io.jsql.sql;

import com.google.common.base.MoreObjects;
import io.jsql.mysql.mysql.AuthPacket;
import io.jsql.mysql.mysql.CommandPacket;
import io.jsql.mysql.mysql.MySQLPacket;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.springframework.stereotype.Component;

/**
 * The type O connection.
 *
 * @author changhong
 * 只用来执行后台命令。
 */
@Component
public class OConnectionCMD extends OConnection{

    @Override
    void init() {
    }

    public OConnectionCMD() {

    }

    @Override
    public void writeNotSurrport() {
    }

    @Override
    public void writeErrMessage(int errno, String msg) {
    }

    @Override
    public void writeErrMessage(byte id, int errno, String msg) {
    }

    @Override
    public void close(String reason) {
    }

    @Override
    public String toString() {
       return MoreObjects.toStringHelper(this).toString();
    }

    @Override
    public void writeok() {
    }

    @Override
    public void writeErrMessage(String message) {
    }

    @Override
    public void register() {
    }

    @Override
    public ByteBuf allocate() {
        return Unpooled.buffer(12);
    }


    @Override
    public void handerAuth(AuthPacket authPacket) {
    }

    @Override
    public void handerCommand(CommandPacket commandPacket) {
    }

    @Override
    public void write(ByteBuf byteBuf) {
    }

    @Override
    public void writeResultSet(MySQLPacket header, MySQLPacket[] filed, MySQLPacket eof1, MySQLPacket[] rows, MySQLPacket eof2) {
    }
}
