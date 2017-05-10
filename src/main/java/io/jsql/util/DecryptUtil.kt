///*
// * Copyright (c) 2013, OpenCloudDB/MyCAT and/or its affiliates. All rights reserved.
// * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
// *
// * This code is free software;Designed and Developed mainly by many Chinese
// * opensource volunteers. you can redistribute it and/or modify it under the
// * terms of the GNU General Public License version 2 only, as published by the
// * Free Software Foundation.
// *
// * This code is distributed in the hope that it will be useful, but WITHOUT
// * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
// * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
// * version 2 for more details (a copy is included in the LICENSE file that
// * accompanied this code).
// *
// * You should have received a copy of the GNU General Public License version
// * 2 along with this work; if not, write to the Free Software Foundation,
// * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
// *
// * Any questions about this component can be directed to it's project Web address
// * https://code.google.com/p/opencloudb/.
// *
// */
//package io.jsql.util
//
//import io.jsql.config.util.ConfigException
//
//import javax.crypto.Cipher
//import java.security.*
//import java.security.interfaces.RSAPrivateKey
//import java.security.interfaces.RSAPublicKey
//import java.security.spec.PKCS8EncodedKeySpec
//import java.security.spec.RSAPrivateKeySpec
//import java.security.spec.RSAPublicKeySpec
//import java.security.spec.X509EncodedKeySpec
//
///**
// * @author songwie
// */
//object DecryptUtil {
//
//    val DEFAULT_PUBLIC_KEY_STRING = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAKHGwq7q2RmwuRgKxBypQHw0mYu4BQZ3eMsTrdK8E6igRcxsobUC7uT0SoxIjl1WveWniCASejoQtn/BY6hVKWsCAwEAAQ=="
//    private val DEFAULT_PRIVATE_KEY_STRING = "MIIBVAIBADANBgkqhkiG9w0BAQEFAASCAT4wggE6AgEAAkEAocbCrurZGbC5GArEHKlAfDSZi7gFBnd4yxOt0rwTqKBFzGyhtQLu5PRKjEiOXVa95aeIIBJ6OhC2f8FjqFUpawIDAQABAkAPejKaBYHrwUqUEEOe8lpnB6lBAsQIUFnQI/vXU4MV+MhIzW0BLVZCiarIQqUXeOhThVWXKFt8GxCykrrUsQ6BAiEA4vMVxEHBovz1di3aozzFvSMdsjTcYRRo82hS5Ru2/OECIQC2fAPoXixVTVY7bNMeuxCP4954ZkXp7fEPDINCjcQDywIgcc8XLkkPcs3Jxk7uYofaXaPbg39wuJpEmzPIxi3k0OECIGubmdpOnin3HuCP/bbjbJLNNoUdGiEmFL5hDI4UdwAdAiEAtcAwbm08bKN7pwwvyqaCBC//VnEWaq39DCzxr+Z2EIk="
//
//    @Throws(Exception::class)
//    @JvmStatic fun main(args: Array<String>) {
//        val password = args[0]
//        println(encrypt(password))
//    }
//
//    fun mycatDecrypt(usingDecrypt: String, user: String, passwrod: String): String {
//        if ("1" == usingDecrypt) {
//            //type:user:password
//            //0:test:test
//            var flag = false
//            try {
//                val passwrods = DecryptUtil.decrypt(passwrod).split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
//                if ("0" == passwrods[0] && user == passwrods[1]) {
//                    flag = true
//                    return passwrods[2]
//                }
//                if (!flag) {
//                    throw ConfigException("user $user passwrod need to decrype ,but decrype password is wrong !")
//                }
//            } catch (e2: Exception) {
//                throw ConfigException("user $user passwrod need to decrype ,but decrype password is wrong !", e2)
//            }
//
//        }
//        return passwrod
//    }
//
//    fun DBHostDecrypt(usingDecrypt: String, host: String, user: String, passwrod: String): String {
//        if ("1" == usingDecrypt) {
//            //type:host:user:password
//            //1:myhost1:test:test
//            val flag = false
//            try {
//                val passwrods = DecryptUtil.decrypt(passwrod).split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
//                if ("1" == passwrods[0] && host == passwrods[1] && user == passwrods[2]) {
//                    return passwrods[3]
//                }
//                if (!flag) {
//                    throw ConfigException("user $user passwrod need to decrype ,but decrype password is wrong !")
//                }
//            } catch (e2: Exception) {
//                throw ConfigException("host $host,user $user passwrod need to decrype ,but decrype password is wrong !", e2)
//            }
//
//        }
//        return passwrod
//    }
//
//
//    @Throws(Exception::class)
//    fun decrypt(cipherText: String) {
////        return decrypt(null as String?, cipherText)
//        "null"
//    }
//
//    @Throws(Exception::class)
//    fun decrypt(publicKeyText: String, cipherText: String): String {
//        val publicKey = getPublicKey(publicKeyText)
//
//        return decrypt(publicKey, cipherText)
//    }
//
//    fun getPublicKey(publicKeyText: String?): PublicKey {
//        var publicKeyText = publicKeyText
//        if (publicKeyText == null || publicKeyText.length == 0) {
//            publicKeyText = DecryptUtil.DEFAULT_PUBLIC_KEY_STRING
//        }
//
//        try {
//            val publicKeyBytes = Base64.base64ToByteArray(publicKeyText)
//            val x509KeySpec = X509EncodedKeySpec(
//                    publicKeyBytes)
//
//            val keyFactory = KeyFactory.getInstance("RSA")
//            return keyFactory.generatePublic(x509KeySpec)
//        } catch (e: Exception) {
//            throw IllegalArgumentException("Failed to get public key", e)
//        }
//
//    }
//
//
//    @Throws(Exception::class)
//    fun decrypt(publicKey: PublicKey, cipherText: String?): String {
//        var cipher = Cipher.getInstance("RSA")
//        try {
//            cipher.init(Cipher.DECRYPT_MODE, publicKey)
//        } catch (e: InvalidKeyException) {
//            // 因为 IBM JDK 不支持私钥加密, 公钥解密, 所以要反转公私钥
//            // 也就是说对于解密, 可以通过公钥的参数伪造一个私钥对象欺骗 IBM JDK
//            val rsaPublicKey = publicKey as RSAPublicKey
//            val spec = RSAPrivateKeySpec(rsaPublicKey.modulus, rsaPublicKey.publicExponent)
//            val fakePrivateKey = KeyFactory.getInstance("RSA").generatePrivate(spec)
//            cipher = Cipher.getInstance("RSA") //It is a stateful object. so we need to get new one.
//            cipher.init(Cipher.DECRYPT_MODE, fakePrivateKey)
//        }
//
//        if (cipherText == null || cipherText.length == 0) {
//            return cipherText!!
//        }
//
//        val cipherBytes = Base64.base64ToByteArray(cipherText)
//        val plainBytes = cipher.doFinal(cipherBytes)
//
//        return String(plainBytes)
//    }
//
//    @Throws(Exception::class)
//    fun encrypt(plainText: String): String {
//        return encrypt(null as String?, plainText)
//    }
//
//    @Throws(Exception::class)
//    fun encrypt(key: String?, plainText: String): String {
//        var key = key
//        if (key == null) {
//            key = DEFAULT_PRIVATE_KEY_STRING
//        }
//
//        val keyBytes = Base64.base64ToByteArray(key)
//        return encrypt(keyBytes, plainText)
//    }
//
//    @Throws(Exception::class)
//    fun encrypt(keyBytes: ByteArray, plainText: String): String {
//        val spec = PKCS8EncodedKeySpec(keyBytes)
//        val factory = KeyFactory.getInstance("RSA")
//        val privateKey = factory.generatePrivate(spec)
//        var cipher = Cipher.getInstance("RSA")
//        try {
//            cipher.init(Cipher.ENCRYPT_MODE, privateKey)
//        } catch (e: InvalidKeyException) {
//            //For IBM JDK, 原因请看解密方法中的说明
//            val rsaPrivateKey = privateKey as RSAPrivateKey
//            val publicKeySpec = RSAPublicKeySpec(rsaPrivateKey.modulus, rsaPrivateKey.privateExponent)
//            val fakePublicKey = KeyFactory.getInstance("RSA").generatePublic(publicKeySpec)
//            cipher = Cipher.getInstance("RSA")
//            cipher.init(Cipher.ENCRYPT_MODE, fakePublicKey)
//        }
//
//        val encryptedBytes = cipher.doFinal(plainText.toByteArray(charset("UTF-8")))
//
//        return Base64.byteArrayToBase64(encryptedBytes)
//    }
//
//    @Throws(NoSuchAlgorithmException::class)
//    fun genKeyPairBytes(keySize: Int): Array<ByteArray?> {
//        val keyPairBytes = arrayOfNulls<ByteArray>(2)
//
//        val gen = KeyPairGenerator.getInstance("RSA")
//        gen.initialize(keySize, SecureRandom())
//        val pair = gen.generateKeyPair()
//
//        keyPairBytes[0] = pair.private.encoded
//        keyPairBytes[1] = pair.public.encoded
//
//        return keyPairBytes
//    }
//
//    @Throws(NoSuchAlgorithmException::class)
//    fun genKeyPair(keySize: Int): Array<String> {
//        val keyPairBytes = genKeyPairBytes(keySize)
//        val keyPairs = arrayOfNulls<String>(2)
//
//        keyPairs[0] = Base64.byteArrayToBase64(keyPairBytes!![0]!!)
//        keyPairs[1] = Base64.byteArrayToBase64(keyPairBytes!![1]!!)
//
//        return keyPairs as Array<String>
//    }
//
//    internal object Base64 {
//
//        /**
//         * This array is a lookup table that translates 6-bit positive integer index values into their "Base64 Alphabet"
//         * equivalents as specified in Table 1 of RFC 2045.
//         */
//        private val intToBase64 = charArrayOf('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/')
//        /**
//         * This array is a lookup table that translates 6-bit positive integer index values into their
//         * "Alternate Base64 Alphabet" equivalents. This is NOT the real Base64 Alphabet as per in Table 1 of RFC 2045. This
//         * alternate alphabet does not use the capital letters. It is designed for use in environments where "case folding"
//         * occurs.
//         */
//        private val intToAltBase64 = charArrayOf('!', '"', '#', '$', '%', '&', '\'', '(', ')', ',', '-', '.', ':', ';', '<', '>', '@', '[', ']', '^', '`', '_', '{', '|', '}', '~', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '?')
//        /**
//         * This array is a lookup table that translates unicode characters drawn from the "Base64 Alphabet" (as specified in
//         * Table 1 of RFC 2045) into their 6-bit positive integer equivalents. Characters that are not in the Base64
//         * alphabet but fall within the bounds of the array are translated to -1.
//         */
//        private val base64ToInt = byteArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1, -1, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51)
//        /**
//         * This array is the analogue of base64ToInt, but for the nonstandard variant that avoids the use of uppercase
//         * alphabetic characters.
//         */
//        private val altBase64ToInt = byteArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, -1, 62, 9, 10, 11, -1, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 12, 13, 14, -1, 15, 63, 16, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 17, -1, 18, 19, 21, 20, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 22, 23, 24, 25)
//
//        /**
//         * Translates the specified byte array into a Base64 string as per Preferences.put(byte[]).
//         */
//        fun byteArrayToBase64(a: ByteArray): String {
//            return byteArrayToBase64(a, false)
//        }
//
//        /**
//         * Translates the specified byte array into an "alternate representation" Base64 string. This non-standard variant
//         * uses an alphabet that does not contain the uppercase alphabetic characters, which makes it suitable for use in
//         * situations where case-folding occurs.
//         */
//        fun byteArrayToAltBase64(a: ByteArray): String {
//            return byteArrayToBase64(a, true)
//        }
//
//        private fun byteArrayToBase64(a: ByteArray, alternate: Boolean): String {
//            val aLen = a.size
//            val numFullGroups = aLen / 3
//            val numBytesInPartialGroup = aLen - 3 * numFullGroups
//            val resultLen = 4 * ((aLen + 2) / 3)
//            val result = StringBuilder(resultLen)
//            val intToAlpha = if (alternate) intToAltBase64 else intToBase64
//
//            // Translate all full groups from byte array elements to Base64
//            var inCursor = 0
//            for (i in 0..numFullGroups - 1) {
//                val byte0 = a[inCursor++].toInt() and 0xff
//                val byte1 = a[inCursor++].toInt() and 0xff
//                val byte2 = a[inCursor++].toInt() and 0xff
//                result.append(intToAlpha[byte0 shr 2])
//                result.append(intToAlpha[byte0 shl 4 and 0x3f or (byte1 shr 4)])
//                result.append(intToAlpha[byte1 shl 2 and 0x3f or (byte2 shr 6)])
//                result.append(intToAlpha[byte2 and 0x3f])
//            }
//
//            // Translate partial group if present
//            if (numBytesInPartialGroup != 0) {
//                val byte0 = a[inCursor++].toInt() and 0xff
//                result.append(intToAlpha[byte0 shr 2])
//                if (numBytesInPartialGroup == 1) {
//                    result.append(intToAlpha[byte0 shl 4 and 0x3f])
//                    result.append("==")
//                } else {
//                    // assert numBytesInPartialGroup == 2;
//                    val byte1 = a[inCursor++].toInt() and 0xff
//                    result.append(intToAlpha[byte0 shl 4 and 0x3f or (byte1 shr 4)])
//                    result.append(intToAlpha[byte1 shl 2 and 0x3f])
//                    result.append('=')
//                }
//            }
//            // assert inCursor == a.length;
//            // assert result.length() == resultLen;
//            return result.toString()
//        }
//
//        /**
//         * Translates the specified Base64 string (as per Preferences.get(byte[])) into a byte array.
//
//         * @throw IllegalArgumentException if <tt>s</tt> is not a valid Base64 string.
//         */
//        fun base64ToByteArray(s: String): ByteArray {
//            return base64ToByteArray(s, false)
//        }
//
//        /**
//         * Translates the specified "alternate representation" Base64 string into a byte array.
//
//         * @throw IllegalArgumentException or ArrayOutOfBoundsException if <tt>s</tt> is not a valid alternate
//         * * representation Base64 string.
//         */
//        fun altBase64ToByteArray(s: String): ByteArray {
//            return base64ToByteArray(s, true)
//        }
//
//        private fun base64ToByteArray(s: String, alternate: Boolean): ByteArray {
//            val alphaToInt = if (alternate) altBase64ToInt else base64ToInt
//            val sLen = s.length
//            val numGroups = sLen / 4
//            if (4 * numGroups != sLen) {
//                throw IllegalArgumentException("String length must be a multiple of four.")
//            }
//            var missingBytesInLastGroup = 0
//            var numFullGroups = numGroups
//            if (sLen != 0) {
//                if (s[sLen - 1] == '=') {
//                    missingBytesInLastGroup++
//                    numFullGroups--
//                }
//                if (s[sLen - 2] == '=') {
//                    missingBytesInLastGroup++
//                }
//            }
//            val result = ByteArray(3 * numGroups - missingBytesInLastGroup)
//
//            // Translate all full groups from base64 to byte array elements
//            var inCursor = 0
//            var outCursor = 0
//            for (i in 0..numFullGroups - 1) {
//                val ch0 = base64toInt(s[inCursor++], alphaToInt)
//                val ch1 = base64toInt(s[inCursor++], alphaToInt)
//                val ch2 = base64toInt(s[inCursor++], alphaToInt)
//                val ch3 = base64toInt(s[inCursor++], alphaToInt)
//                result[outCursor++] = (ch0 shl 2 or (ch1 shr 4)).toByte()
//                result[outCursor++] = (ch1 shl 4 or (ch2 shr 2)).toByte()
//                result[outCursor++] = (ch2 shl 6 or ch3).toByte()
//            }
//
//            // Translate partial group, if present
//            if (missingBytesInLastGroup != 0) {
//                val ch0 = base64toInt(s[inCursor++], alphaToInt)
//                val ch1 = base64toInt(s[inCursor++], alphaToInt)
//                result[outCursor++] = (ch0 shl 2 or (ch1 shr 4)).toByte()
//
//                if (missingBytesInLastGroup == 1) {
//                    val ch2 = base64toInt(s[inCursor++], alphaToInt)
//                    result[outCursor++] = (ch1 shl 4 or (ch2 shr 2)).toByte()
//                }
//            }
//            // assert inCursor == s.length()-missingBytesInLastGroup;
//            // assert outCursor == result.length;
//            return result
//        }
//
//        /**
//         * Translates the specified character, which is assumed to be in the "Base 64 Alphabet" into its equivalent 6-bit
//         * positive integer.
//
//         * @throw IllegalArgumentException or ArrayOutOfBoundsException if c is not in the Base64 Alphabet.
//         */
//        private fun base64toInt(c: Char, alphaToInt: ByteArray): Int {
//            val result = alphaToInt[c.toInt()].toInt()
//            if (result < 0) {
//                throw IllegalArgumentException("Illegal character " + c)
//            }
//            return result
//        }
//
//    }
//
//}
