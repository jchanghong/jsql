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
package io.jsql.util

/**
 * @author jsql
 */
object LongUtil {

    private val minValue = "-9223372036854775808".toByteArray()

    fun toBytes(i: Long): ByteArray {
        if (i == java.lang.Long.MIN_VALUE) {
            return minValue
        }
        val size = if (i < 0) stringSize(-i) + 1 else stringSize(i)
        val buf = ByteArray(size)
        getBytes(i, size, buf)
        return buf
    }

    internal fun stringSize(x: Long): Int {
        var p: Long = 10
        for (i in 1..18) {
            if (x < p) {
                return i
            }
            p = 10 * p
        }
        return 19
    }

    internal fun getBytes(i: Long, index: Int, buf: ByteArray) {
        var i = i
        var q: Long
        var r: Int
        var charPos = index
        var sign: Byte = 0

        if (i < 0) {
            sign = '-'.toByte()
            i = -i
        }

        // Get 2 digits/iteration using longs until quotient fits into an int
        while (i > Integer.MAX_VALUE) {
            q = i / 100
            // really: r = i - (q * 100);
            r = (i - ((q shl 6) + (q shl 5) + (q shl 2))).toInt()
            i = q
            buf[--charPos] = IntegerUtil.digitOnes[r].toByte()
            buf[--charPos] = IntegerUtil.digitTens[r].toByte()
        }

        // Get 2 digits/iteration using ints
        var q2: Int
        var i2 = i.toInt()
        while (i2 >= 65536) {
            q2 = i2 / 100
            // really: r = i2 - (q * 100);
            r = i2 - ((q2 shl 6) + (q2 shl 5) + (q2 shl 2))
            i2 = q2
            buf[--charPos] = IntegerUtil.digitOnes[r].toByte()
            buf[--charPos] = IntegerUtil.digitTens[r].toByte()
        }

        // Fall thru to fast mode for smaller numbers
        // assert(i2 <= 65536, i2);
        while (true) {
            q2 = (i2 * 52429).ushr(16 + 3)
            r = i2 - ((q2 shl 3) + (q2 shl 1)) // r = i2-(q2*10) ...
            buf[--charPos] = IntegerUtil.digits[r].toByte()
            i2 = q2
            if (i2 == 0) {
                break
            }
        }
        if (sign.toInt() != 0) {
            buf[--charPos] = sign
        }
    }

}