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
object IntegerUtil {

    internal val minValue = "-2147483648".toByteArray()
    internal val sizeTable = intArrayOf(9, 99, 999, 9999, 99999, 999999, 9999999, 99999999, 999999999, Integer.MAX_VALUE)
    internal val digitTens = charArrayOf('0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '3', '3', '3', '3', '3', '3', '3', '3', '3', '3', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '6', '6', '6', '6', '6', '6', '6', '6', '6', '6', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '8', '8', '8', '8', '8', '8', '8', '8', '8', '8', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9')
    internal val digitOnes = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9')
    internal val digits = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z')

    fun toBytes(i: Int): ByteArray {
        if (i == Integer.MIN_VALUE) {
            return minValue
        }
        val size = if (i < 0) stringSize(-i) + 1 else stringSize(i)
        val buf = ByteArray(size)
        getBytes(i, size, buf)
        return buf
    }

    internal fun stringSize(x: Int): Int {
        var i = 0
        while (true) {
            if (x <= sizeTable[i]) {
                return i + 1
            }
            i++
        }
    }

    internal fun getBytes(i: Int, index: Int, buf: ByteArray) {
        var i = i
        var q: Int
        var r: Int
        var charPos = index
        var sign: Byte = 0

        if (i < 0) {
            sign = '-'.toByte()
            i = -i
        }

        // Generate two digits per iteration
        while (i >= 65536) {
            q = i / 100
            // really: r = i - (q * 100);
            r = i - ((q shl 6) + (q shl 5) + (q shl 2))
            i = q
            buf[--charPos] = digitOnes[r].toByte()
            buf[--charPos] = digitTens[r].toByte()
        }

        // Fall thru to fast mode for smaller numbers
        // assert(i <= 65536, i);
        while (true) {
            q = (i * 52429).ushr(16 + 3)
            r = i - ((q shl 3) + (q shl 1)) // r = i-(q*10) ...
            buf[--charPos] = digits[r].toByte()
            i = q
            if (i == 0) {
                break
            }
        }
        if (sign.toInt() != 0) {
            buf[--charPos] = sign
        }
    }

}