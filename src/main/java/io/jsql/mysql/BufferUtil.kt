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

import java.nio.ByteBuffer

/**
 * @author jsql
 */
object BufferUtil {

    fun writeUB2(buffer: ByteBuffer, i: Int) {
        buffer.put((i and 0xff).toByte())
        buffer.put(i.ushr(8).toByte())
    }

    fun writeUB3(buffer: ByteBuffer, i: Int) {
        buffer.put((i and 0xff).toByte())
        buffer.put(i.ushr(8).toByte())
        buffer.put(i.ushr(16).toByte())
    }

    fun writeInt(buffer: ByteBuffer, i: Int) {
        buffer.put((i and 0xff).toByte())
        buffer.put(i.ushr(8).toByte())
        buffer.put(i.ushr(16).toByte())
        buffer.put(i.ushr(24).toByte())
    }

    fun writeFloat(buffer: ByteBuffer, f: Float) {
        writeInt(buffer, java.lang.Float.floatToIntBits(f))
    }

    fun writeUB4(buffer: ByteBuffer, l: Long) {
        buffer.put((l and 0xff).toByte())
        buffer.put(l.ushr(8).toByte())
        buffer.put(l.ushr(16).toByte())
        buffer.put(l.ushr(24).toByte())
    }

    fun writeLong(buffer: ByteBuffer, l: Long) {
        buffer.put((l and 0xff).toByte())
        buffer.put(l.ushr(8).toByte())
        buffer.put(l.ushr(16).toByte())
        buffer.put(l.ushr(24).toByte())
        buffer.put(l.ushr(32).toByte())
        buffer.put(l.ushr(40).toByte())
        buffer.put(l.ushr(48).toByte())
        buffer.put(l.ushr(56).toByte())
    }

    fun writeDouble(buffer: ByteBuffer, d: Double) {
        writeLong(buffer, java.lang.Double.doubleToLongBits(d))
    }

    fun writeLength(buffer: ByteBuffer, l: Long) {
        if (l < 251) {
            buffer.put(l.toByte())
        } else if (l < 0x10000L) {
            buffer.put(252.toByte())
            writeUB2(buffer, l.toInt())
        } else if (l < 0x1000000L) {
            buffer.put(253.toByte())
            writeUB3(buffer, l.toInt())
        } else {
            buffer.put(254.toByte())
            writeLong(buffer, l)
        }
    }

    fun writeWithNull(buffer: ByteBuffer, src: ByteArray) {
        buffer.put(src)
        buffer.put(0.toByte())
    }

    fun writeWithLength(buffer: ByteBuffer, src: ByteArray) {
        val length = src.size
        if (length < 251) {
            buffer.put(length.toByte())
        } else if (length < 0x10000L) {
            buffer.put(252.toByte())
            writeUB2(buffer, length)
        } else if (length < 0x1000000L) {
            buffer.put(253.toByte())
            writeUB3(buffer, length)
        } else {
            buffer.put(254.toByte())
            writeLong(buffer, length.toLong())
        }
        buffer.put(src)
    }

    fun writeWithLength(buffer: ByteBuffer, src: ByteArray?, nullValue: Byte) {
        if (src == null) {
            buffer.put(nullValue)
        } else {
            writeWithLength(buffer, src)
        }
    }

    fun getLength(length: Long): Int {
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

    fun getLength(src: ByteArray): Int {
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

}