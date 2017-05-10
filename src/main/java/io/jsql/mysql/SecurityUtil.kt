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

import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

/**
 * 加密解密工具类

 * @author jsql
 */
object SecurityUtil {

    @Throws(NoSuchAlgorithmException::class)
    fun scramble411(pass: ByteArray, seed: ByteArray): ByteArray {
        val md = MessageDigest.getInstance("SHA-1")
        val pass1 = md.digest(pass)
        md.reset()
        val pass2 = md.digest(pass1)
        md.reset()
        md.update(seed)
        val pass3 = md.digest(pass2)
        for (i in pass3.indices) {
            pass3[i] = (pass3[i].toInt() xor pass1[i].toInt()).toByte()
        }
        return pass3
    }

    fun scramble323(pass: String?, seed: String): String {
        if (pass == null || pass.length == 0) {
            return pass!!
        }
        var b: Byte
        var d: Double
        val pw = hash(seed)
        val msg = hash(pass)
        val max = 0x3fffffffL
        var seed1 = (pw[0] xor msg[0]) % max
        var seed2 = (pw[1] xor msg[1]) % max
        val chars = CharArray(seed.length)
        for (i in 0..seed.length - 1) {
            seed1 = (seed1 * 3 + seed2) % max
            seed2 = (seed1 + seed2 + 33) % max
            d = seed1.toDouble() / max.toDouble()
            b = java.lang.Math.floor(d * 31 + 64).toByte()
            chars[i] = b.toChar()
        }
        seed1 = (seed1 * 3 + seed2) % max
        //        seed2 = (seed1 + seed2 + 33) % max;
        d = seed1.toDouble() / max.toDouble()
        b = java.lang.Math.floor(d * 31).toByte()
        for (i in 0..seed.length - 1) {
            chars[i] = (chars[i].toInt() xor b.toChar().toInt()).toChar()
        }
        return String(chars)
    }

    private fun hash(src: String): LongArray {
        var nr = 1345345333L
        var add: Long = 7
        var nr2 = 0x12345671L
        var tmp: Long
        loop@ for (i in 0..src.length - 1) {
            when (src[i]) {
                ' ', '\t' -> continue@loop
                else -> {
                    tmp = (0xff and src[i].toInt()).toLong()
                    nr = nr xor ((nr and 63) + add) * tmp + (nr shl 8)
                    nr2 += nr2 shl 8 xor nr
                    add += tmp
                }
            }
        }
        val result = LongArray(2)
        result[0] = nr and 0x7fffffffL
        result[1] = nr2 and 0x7fffffffL
        return result
    }

}