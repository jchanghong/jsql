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

import kotlin.experimental.and
import kotlin.experimental.or

/**
 * @author jsql
 */
object ByteUtil {

    fun readUB2(data: ByteArray, offset: Int): Int {
        var i1=offset
        var i = data[i1].toInt() and 0xff
        i = i or (data[++i1].toInt() and 0xff shl 8)
        return i
    }

    fun readUB3(data: ByteArray, offset: Int): Int {
        var offset = offset
        var i = data[offset].toInt() and 0xff
        i = i or (data[++offset].toInt() and 0xff shl 8)
        i = i or (data[++offset].toInt() and 0xff shl 16)
        return i
    }

    fun readUB4(data: ByteArray, offset: Int): Long {
        var offset = offset
        var l = (data[offset].toInt() and 0xff).toLong()
        l = l or (data[++offset].toInt() and 0xff shl 8).toLong()
        l = l or (data[++offset].toInt() and 0xff shl 16).toLong()
        l = l or (data[++offset].toInt() and 0xff shl 24).toLong()
        return l
    }

    fun readLong(data: ByteArray, offset: Int): Long {
        var offset = offset
        var l = (data[offset].toInt() and 0xff).toLong()
        l = l or ((data[++offset].toInt() and 0xff).toLong() shl 8)
        l = l or ((data[++offset].toInt() and 0xff).toLong() shl 16)
        l = l or ((data[++offset].toInt() and 0xff).toLong() shl 24)
        l = l or ((data[++offset].toInt() and 0xff).toLong() shl 32)
        l = l or ((data[++offset].toInt().toInt() and 0xff).toLong() shl 40)
        l = l or ((data[++offset].toInt().toInt() and 0xff).toLong() shl 48)
        l = l or ((data[++offset].toInt().toInt() and 0xff).toLong() shl 56)
        return l
    }

    fun readLength(data: ByteArray, offset: Int): Long {
        var offset = offset
        val length = data[offset++].toInt() and 0xff
        when (length) {
            251 -> return MySQLMessage.NULL_LENGTH
            252 -> return readUB2(data, offset).toLong()
            253 -> return readUB3(data, offset).toLong()
            254 -> return readLong(data, offset)
            else -> return length.toLong()
        }
    }

    fun lengthToZero(data: ByteArray, offset: Int): Int {
        for (i in offset..data.size - 1) {
            if (data[i].toInt() == 0) {
                return i - offset
            }
        }
        val remaining = data.size - offset
        return if (remaining > 0) remaining else 0
    }

    fun decodeLength(src: ByteArray): Int {
        val length = src.size
        if (length < 251) {
            return 1 + length
        } else if (length < 0x10000L) {
            return 3 + length
        } else if (length < 0x1000000L) {
            return 4 + length
        } else {
            return 9 + length
        }
    }

    fun decodeLength(length: Long): Int {
        if (length < 251) {
            return 1
        } else if (length < 0x10000L) {
            return 3
        } else if (length < 0x1000000L) {
            return 4
        } else {
            return 9
        }
    }

}