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
package io.jsql.mysql

import io.jsql.config.ErrorCode
import io.jsql.mysql.mysql.BinaryPacket
import io.jsql.mysql.mysql.ErrorPacket
import io.jsql.mysql.mysql.FieldPacket
import io.jsql.mysql.mysql.ResultSetHeaderPacket

import java.io.FilePermission
import java.io.UnsupportedEncodingException

/**
 * @author jsql
 */
object PacketUtil {
    private val CODE_PAGE_1252 = "Cp1252"

    fun getHeader(fieldCount: Int): ResultSetHeaderPacket {
        val packet = ResultSetHeaderPacket()
        packet.packetId = 1
        packet.fieldCount = fieldCount
        return packet
    }

    fun encode(src: String?, charset: String): ByteArray? {
        if (src == null) {
            return null
        }
        try {
            return src.toByteArray(charset(charset))
        } catch (e: UnsupportedEncodingException) {
            return src.toByteArray()
        }

    }

    fun getField(name: String, orgName: String, type: Int): FieldPacket {
        val packet = FieldPacket()
        packet.charsetIndex = CharsetUtil.getIndex(CODE_PAGE_1252)
        packet.name = encode(name, CODE_PAGE_1252)
        packet.orgName = encode(orgName, CODE_PAGE_1252)
        packet.type = type.toByte().toInt()
        return packet
    }

    fun getField(name: String, type: Int): FieldPacket {
        val packet = FieldPacket()
        packet.charsetIndex = CharsetUtil.getIndex(CODE_PAGE_1252)
        packet.name = encode(name, CODE_PAGE_1252)
        packet.type = type.toByte().toInt()
        return packet
    }

    fun setFieldName(fieldName: FieldPacket, newname: String) {
        fieldName.name = encode(newname, CODE_PAGE_1252)
    }

    val shutdown: ErrorPacket
        get() {
            val error = ErrorPacket()
            error.packetId = 1
            error.errno = ErrorCode.ER_SERVER_SHUTDOWN
            error.message = "The server has been shutdown".toByteArray()
            return error
        }

    fun getField(src: BinaryPacket, fieldName: String): FieldPacket {
        val field = FieldPacket()
        field.read(src)
        field.name = encode(fieldName, CODE_PAGE_1252)
        field.packetLength = field.calcPacketSize()
        return field
    }

}