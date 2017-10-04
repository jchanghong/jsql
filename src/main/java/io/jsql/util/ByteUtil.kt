/*
 * Java-based distributed database like Mysql
 */
package io.jsql.util

import java.nio.charset.Charset
import java.util.Date

object ByteUtil {

    /**
     * compare to number or dicamal ascii byte array, for number :123456 ,store
     * to array [1,2,3,4,5,6]

     * @param b1
     * *
     * @param b2
     * *
     * @return -1 means b1 < b2, or 0 means b1=b2 else return 1
     */
    fun compareNumberByte(b1: ByteArray?, b2: ByteArray?): Int {
        if (b1 == null || b1.size == 0) {
            return -1
        } else if (b2 == null || b2.size == 0) {
            return 1
        }
        val isNegetive = b1[0].toInt() == 45 || b2[0].toInt() == 45
        if (!isNegetive && b1.size != b2.size) {
            return b1.size - b2.size
        }
        val len = if (b1.size > b2.size) b2.size else b1.size
        var result = 0
        var index = -1
        for (i in 0..len - 1) {
            val b1val = b1[i].toInt()
            val b2val = b2[i].toInt()
            if (b1val > b2val) {
                result = 1
                index = i
                break
            } else if (b1val < b2val) {
                index = i
                result = -1
                break
            }
        }
        if (index == 0) {
            // first byte compare
            return result
        } else {
            if (b1.size != b2.size) {

                val lenDelta = b1.size - b2.size
                return if (isNegetive) 0 - lenDelta else lenDelta

            } else {
                return if (isNegetive) 0 - result else result
            }
        }
    }

    fun compareNumberArray2(b1: ByteArray, b2: ByteArray, order: Int): ByteArray {
        if (b1.size <= 0 && b2.size > 0) {
            return b2
        }
        if (b1.size > 0 && b2.size <= 0) {
            return b1
        }
        val len = if (b1.size > b2.size) b1.size else b2.size
        for (i in 0..len - 1) {
            if (b1[i] != b2[i]) {
                if (order == 1) {
                    return if ((b1[i].toInt() and 0xff) - (b2[i].toInt() and 0xff) > 0) b1 else b2
                } else {
                    return if ((b1[i].toInt() and 0xff) - (b2[i].toInt() and 0xff) > 0) b2 else b1
                }
            }
        }

        return b1
    }

    fun getBytes(data: Short): ByteArray {
        val bytes = ByteArray(2)
        bytes[0] = (data.toInt() and 0xff).toByte()
        bytes[1] = (data.toInt() and 0xff00 shr 8).toByte()
        return bytes
    }

    fun getBytes(data: Char): ByteArray {
        val bytes = ByteArray(2)
        bytes[0] = data.toByte()
        bytes[1] = (data.toInt() shr 8).toByte()
        return bytes
    }

    fun getBytes(data: Int): ByteArray {
        val bytes = ByteArray(4)
        bytes[0] = (data and 0xff).toByte()
        bytes[1] = (data and 0xff00 shr 8).toByte()
        bytes[2] = (data and 0xff0000 shr 16).toByte()
        bytes[3] = (data and 0xff000000.toInt() shr 24).toByte()
        return bytes
    }

    fun getBytes(data: Long): ByteArray {
        val bytes = ByteArray(8)
        bytes[0] = (data and 0xff).toByte()
        bytes[1] = (data shr 8 and 0xff).toByte()
        bytes[2] = (data shr 16 and 0xff).toByte()
        bytes[3] = (data shr 24 and 0xff).toByte()
        bytes[4] = (data shr 32 and 0xff).toByte()
        bytes[5] = (data shr 40 and 0xff).toByte()
        bytes[6] = (data shr 48 and 0xff).toByte()
        bytes[7] = (data shr 56 and 0xff).toByte()
        return bytes
    }

    fun getBytes(data: Float): ByteArray {
        val intBits = java.lang.Float.floatToIntBits(data)
        return getBytes(intBits)
    }

    fun getBytes(data: Double): ByteArray {
        val intBits = java.lang.Double.doubleToLongBits(data)
        return getBytes(intBits)
    }

    @JvmOverloads fun getBytes(data: String, charsetName: String = "GBK"): ByteArray {
        val charset = Charset.forName(charsetName)
        return data.toByteArray(charset)
    }

    fun getShort(bytes: ByteArray): Short {
        return (0xff and bytes[0].toInt() or (0xff00 and (bytes[1].toInt() shl 8))).toShort()
    }

    fun getChar(bytes: ByteArray): Char {
        return (0xff and bytes[0].toInt() or (0xff00 and (bytes[1].toInt() shl 8))).toChar()
    }

    fun getInt(bytes: ByteArray): Int {
        return Integer.parseInt(String(bytes))
        // return (0xff & bytes[0]) | (0xff00 & (bytes[1] << 8)) | (0xff0000 &
        // (bytes[2] << 16)) | (0xff000000 & (bytes[3] << 24));
    }

    fun getLong(bytes: ByteArray): Long {
        return java.lang.Long.parseLong(String(bytes))
        // return(0xffL & (long)bytes[0]) | (0xff00L & ((long)bytes[1] << 8)) |
        // (0xff0000L & ((long)bytes[2] << 16)) | (0xff000000L & ((long)bytes[3]
        // << 24))
        // | (0xff00000000L & ((long)bytes[4] << 32)) | (0xff0000000000L &
        // ((long)bytes[5] << 40)) | (0xff000000000000L & ((long)bytes[6] <<
        // 48)) | (0xff00000000000000L & ((long)bytes[7] << 56));
    }

    fun getDouble(bytes: ByteArray): Double {
        return java.lang.Double.parseDouble(String(bytes))
    }

    fun getFloat(bytes: ByteArray): Float {
        return java.lang.Float.parseFloat(String(bytes))
    }

    @JvmOverloads fun getString(bytes: ByteArray, charsetName: String = "UTF-8"): String {
        return String(bytes, Charset.forName(charsetName))
    }

    fun getDate(bytes: ByteArray): String {
        return String(bytes)
    }

    fun getTime(bytes: ByteArray): String {
        return String(bytes)
    }

    fun getTimestmap(bytes: ByteArray): String {
        return String(bytes)
    }

    fun getBytes(date: Date, isTime: Boolean): ByteArray {
        if (isTime) {
            return getBytesFromTime(date)
        } else {
            return getBytesFromDate(date)
        }
    }

    private fun getBytesFromTime(date: Date): ByteArray {
        val day = 0
        val hour = DateUtil.getHour(date)
        val minute = DateUtil.getMinute(date)
        val second = DateUtil.getSecond(date)
        val microSecond = DateUtil.getMicroSecond(date)
        var bytes: ByteArray? = null
        var tmp: ByteArray? = null
        if (day == 0 && hour == 0 && minute == 0
                && second == 0 && microSecond == 0) {
            bytes = ByteArray(1)
            bytes[0] = 0.toByte()
        } else if (microSecond == 0) {
            bytes = ByteArray(1 + 8)
            bytes[0] = 8.toByte()
            bytes[1] = 0.toByte() // is_negative (1) -- (1 if minus, 0 for plus)
            tmp = getBytes(day)
            bytes[2] = tmp[0]
            bytes[3] = tmp[1]
            bytes[4] = tmp[2]
            bytes[5] = tmp[3]
            bytes[6] = hour.toByte()
            bytes[7] = minute.toByte()
            bytes[8] = second.toByte()
        } else {
            bytes = ByteArray(1 + 12)
            bytes[0] = 12.toByte()
            bytes[1] = 0.toByte() // is_negative (1) -- (1 if minus, 0 for plus)
            tmp = getBytes(day)
            bytes[2] = tmp[0]
            bytes[3] = tmp[1]
            bytes[4] = tmp[2]
            bytes[5] = tmp[3]
            bytes[6] = hour.toByte()
            bytes[7] = minute.toByte()
            bytes[8] = second.toByte()
            tmp = getBytes(microSecond)
            bytes[9] = tmp[0]
            bytes[10] = tmp[1]
            bytes[11] = tmp[2]
            bytes[12] = tmp[3]
        }
        return bytes
    }

    private fun getBytesFromDate(date: Date): ByteArray {
        val year = DateUtil.getYear(date)
        val month = DateUtil.getMonth(date)
        val day = DateUtil.getDay(date)
        val hour = DateUtil.getHour(date)
        val minute = DateUtil.getMinute(date)
        val second = DateUtil.getSecond(date)
        val microSecond = DateUtil.getMicroSecond(date)
        var bytes: ByteArray? = null
        var tmp: ByteArray? = null
        if (year == 0 && month == 0 && day == 0
                && hour == 0 && minute == 0 && second == 0
                && microSecond == 0) {
            bytes = ByteArray(1)
            bytes[0] = 0.toByte()
        } else if (hour == 0 && minute == 0 && second == 0
                && microSecond == 0) {
            bytes = ByteArray(1 + 4)
            bytes[0] = 4.toByte()
            tmp = getBytes(year.toShort())
            bytes[1] = tmp[0]
            bytes[2] = tmp[1]
            bytes[3] = month.toByte()
            bytes[4] = day.toByte()
        } else if (microSecond == 0) {
            bytes = ByteArray(1 + 7)
            bytes[0] = 7.toByte()
            tmp = getBytes(year.toShort())
            bytes[1] = tmp[0]
            bytes[2] = tmp[1]
            bytes[3] = month.toByte()
            bytes[4] = day.toByte()
            bytes[5] = hour.toByte()
            bytes[6] = minute.toByte()
            bytes[7] = second.toByte()
        } else {
            bytes = ByteArray(1 + 11)
            bytes[0] = 11.toByte()
            tmp = getBytes(year.toShort())
            bytes[1] = tmp[0]
            bytes[2] = tmp[1]
            bytes[3] = month.toByte()
            bytes[4] = day.toByte()
            bytes[5] = hour.toByte()
            bytes[6] = minute.toByte()
            bytes[7] = second.toByte()
            tmp = getBytes(microSecond)
            bytes[8] = tmp[0]
            bytes[9] = tmp[1]
            bytes[10] = tmp[2]
            bytes[11] = tmp[3]
        }
        return bytes
    }

    // 支持 byte dump
    //---------------------------------------------------------------------
    fun dump(data: ByteArray, offset: Int, length: Int): String {

        val sb = StringBuilder()
        sb.append(" byte dump log ")
        sb.append(System.lineSeparator())
        sb.append(" offset ").append(offset)
        sb.append(" length ").append(length)
        sb.append(System.lineSeparator())
        val lines = (length - 1) / 16 + 1
        var i = 0
        var pos = 0
        while (i < lines) {
            sb.append(String.format("0x%04X ", i * 16))
            run {
                var j = 0
                var pos1 = pos
                while (j < 16) {
                    sb.append(if (pos1 < length) String.format("%02X ", data[offset + pos1]) else "   ")
                    j++
                    pos1++
                }
            }
            sb.append(" ")
            var j = 0
            var pos1 = pos
            while (j < 16) {
                sb.append(if (pos1 < length) print(data[offset + pos1]) else '.')
                j++
                pos1++
            }
            sb.append(System.lineSeparator())
            i++
            pos += 16
        }
        sb.append(length).append(" bytes").append(System.lineSeparator())
        return sb.toString()
    }

    fun print(b: Byte): Char {
        return if (b < 32 || b > 127) '.' else b.toChar()
    }

}