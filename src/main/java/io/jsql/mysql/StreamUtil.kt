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

import java.io.EOFException
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

/**
 * @author jsql
 */
object StreamUtil {
    private val NULL_LENGTH: Long = -1
    private val EMPTY_BYTES = ByteArray(0)

    @Throws(IOException::class)
    fun read(`in`: InputStream, b: ByteArray, offset: Int, length: Int) {
        var offset = offset
        var length = length
        var got = 0
        while (length > 0) {
            got = `in`.read(b, offset, length)
            if (got < 0) {
                throw EOFException()
            }
            offset += got
            length -= got
        }
    }

    @Throws(IOException::class)
    fun read(`in`: InputStream): Byte {
        val got = `in`.read()
        if (got < 0) {
            throw EOFException()
        }
        return (got and 0xff).toByte()
    }

    @Throws(IOException::class)
    fun readUB2(`in`: InputStream): Int {
        val b = ByteArray(2)
        read(`in`, b, 0, b.size)
        var i = b[0].toInt() and 0xff
        i = i or (b[1].toInt() and 0xff shl 8)
        return i
    }

    @Throws(IOException::class)
    fun readUB3(`in`: InputStream): Int {
        val b = ByteArray(3)
        read(`in`, b, 0, b.size)
        var i = b[0].toInt() and 0xff
        i = i or (b[1].toInt() and 0xff shl 8)
        i = i or (b[2].toInt() and 0xff shl 16)
        return i
    }

    @Throws(IOException::class)
    fun readInt(`in`: InputStream): Int {
        val b = ByteArray(4)
        read(`in`, b, 0, b.size)
        var i = b[0].toInt() and 0xff
        i = i or (b[1].toInt() and 0xff shl 8)
        i = i or (b[2].toInt() and 0xff shl 16)
        i = i or (b[3].toInt() and 0xff shl 24)
        return i
    }

    @Throws(IOException::class)
    fun readFloat(`in`: InputStream): Float {
        return java.lang.Float.intBitsToFloat(readInt(`in`))
    }

    @Throws(IOException::class)
    fun readUB4(`in`: InputStream): Long {
        val b = ByteArray(4)
        read(`in`, b, 0, b.size)
        var l = (b[0].toInt() and 0xff).toLong()
        l = l or ((b[1].toInt() and 0xff).toLong() shl 8)
        l = l or ((b[2].toInt() and 0xff).toLong() shl 16)
        l = l or ((b[3].toInt() and 0xff).toLong() shl 24)
        return l
    }

    @Throws(IOException::class)
    fun readLong(`in`: InputStream): Long {
        val b = ByteArray(8)
        read(`in`, b, 0, b.size)
        var l = (b[0].toInt() and 0xff).toLong()
        l = l or ((b[1].toInt() and 0xff).toLong() shl 8)
        l = l or ((b[2].toInt() and 0xff).toLong() shl 16)
        l = l or ((b[3].toInt() and 0xff).toLong() shl 24)
        l = l or ((b[4].toInt() and 0xff).toLong() shl 32)
        l = l or ((b[5].toInt() and 0xff).toLong() shl 40)
        l = l or ((b[6].toInt() and 0xff).toLong() shl 48)
        l = l or ((b[7].toInt() and 0xff).toLong() shl 56)
        return l
    }

    @Throws(IOException::class)
    fun readDouble(`in`: InputStream): Double {
        return java.lang.Double.longBitsToDouble(readLong(`in`))
    }

    @Throws(IOException::class)
    fun readWithLength(`in`: InputStream): ByteArray {
        val length = readLength(`in`).toInt()
        if (length <= 0) {
            return EMPTY_BYTES
        }
        val b = ByteArray(length)
        read(`in`, b, 0, b.size)
        return b
    }

    @Throws(IOException::class)
    fun write(out: OutputStream, b: Byte) {
        out.write(b.toInt() and 0xff)
    }

    @Throws(IOException::class)
    fun writeUB2(out: OutputStream, i: Int) {
        val b = ByteArray(2)
        b[0] = (i and 0xff).toByte()
        b[1] = i.ushr(8).toByte()
        out.write(b)
    }

    @Throws(IOException::class)
    fun writeUB3(out: OutputStream, i: Int) {
        val b = ByteArray(3)
        b[0] = (i and 0xff).toByte()
        b[1] = i.ushr(8).toByte()
        b[2] = i.ushr(16).toByte()
        out.write(b)
    }

    @Throws(IOException::class)
    fun writeInt(out: OutputStream, i: Int) {
        val b = ByteArray(4)
        b[0] = (i and 0xff).toByte()
        b[1] = i.ushr(8).toByte()
        b[2] = i.ushr(16).toByte()
        b[3] = i.ushr(24).toByte()
        out.write(b)
    }

    @Throws(IOException::class)
    fun writeFloat(out: OutputStream, f: Float) {
        writeInt(out, java.lang.Float.floatToIntBits(f))
    }

    @Throws(IOException::class)
    fun writeUB4(out: OutputStream, l: Long) {
        val b = ByteArray(4)
        b[0] = (l and 0xff).toByte()
        b[1] = l.ushr(8).toByte()
        b[2] = l.ushr(16).toByte()
        b[3] = l.ushr(24).toByte()
        out.write(b)
    }

    @Throws(IOException::class)
    fun writeLong(out: OutputStream, l: Long) {
        val b = ByteArray(8)
        b[0] = (l and 0xff).toByte()
        b[1] = l.ushr(8).toByte()
        b[2] = l.ushr(16).toByte()
        b[3] = l.ushr(24).toByte()
        b[4] = l.ushr(32).toByte()
        b[5] = l.ushr(40).toByte()
        b[6] = l.ushr(48).toByte()
        b[7] = l.ushr(56).toByte()
        out.write(b)
    }

    @Throws(IOException::class)
    fun writeDouble(out: OutputStream, d: Double) {
        writeLong(out, java.lang.Double.doubleToLongBits(d))
    }

    @Throws(IOException::class)
    fun readLength(`in`: InputStream): Long {
        val length = `in`.read()
        if (length < 0) {
            throw EOFException()
        }
        when (length) {
            251 -> return NULL_LENGTH
            252 -> return readUB2(`in`).toLong()
            253 -> return readUB3(`in`).toLong()
            254 -> return readLong(`in`)
            else -> return length.toLong()
        }
    }

    @Throws(IOException::class)
    fun writeLength(out: OutputStream, length: Long) {
        if (length < 251) {
            out.write(length.toByte().toInt())
        } else if (length < 0x10000L) {
            out.write(252.toByte().toInt())
            writeUB2(out, length.toInt())
        } else if (length < 0x1000000L) {
            out.write(253.toByte().toInt())
            writeUB3(out, length.toInt())
        } else {
            out.write(254.toByte().toInt())
            writeLong(out, length)
        }
    }

    @Throws(IOException::class)
    fun writeWithNull(out: OutputStream, src: ByteArray) {
        out.write(src)
        out.write(0.toByte().toInt())
    }

    @Throws(IOException::class)
    fun writeWithLength(out: OutputStream, src: ByteArray) {
        val length = src.size
        if (length < 251) {
            out.write(length.toByte().toInt())
        } else if (length < 0x10000L) {
            out.write(252.toByte().toInt())
            writeUB2(out, length)
        } else if (length < 0x1000000L) {
            out.write(253.toByte().toInt())
            writeUB3(out, length)
        } else {
            out.write(254.toByte().toInt())
            writeLong(out, length.toLong())
        }
        out.write(src)
    }

}