/*
 * Java-based distributed database like Mysql
 */
package io.jsql.mysql

import java.io.UnsupportedEncodingException
import java.math.BigDecimal
import java.sql.Time
import java.sql.Timestamp
import java.util.Arrays
import java.util.Calendar

/**
 * @author jsql
 */
class MySQLMessage(private val data: ByteArray) {
    private val length: Int
    private var position: Int = 0

    init {
        this.length = data.size
        this.position = 0
    }

    fun length(): Int {
        return length
    }

    fun position(): Int {
        return position
    }

    fun bytes(): ByteArray {
        return data
    }

    fun move(i: Int) {
        position += i
    }

    fun position(i: Int) {
        this.position = i
    }

    fun hasRemaining(): Boolean {
        return length > position
    }

    fun read(i: Int): Byte {
        return data[i]
    }

    fun read(): Byte {
        return data[position++]
    }

    fun readUB2(): Int {
        val b = this.data
        var i = b[position++].toInt() and 0xff
        i = i or (b[position++].toInt() and 0xff shl 8)
        return i
    }

    fun readUB3(): Int {
        val b = this.data
        var i = b[position++].toInt() and 0xff
        i = i or (b[position++].toInt() and 0xff shl 8)
        i = i or (b[position++].toInt() and 0xff shl 16)
        return i
    }

    fun readUB4(): Long {
        val b = this.data
        var l = (b[position++].toInt() and 0xff).toLong()
        l = l or ((b[position++].toInt() and 0xff).toLong() shl 8)
        l = l or ((b[position++].toInt() and 0xff).toLong() shl 16)
        l = l or ((b[position++].toInt() and 0xff).toLong() shl 24)
        return l
    }

    fun readInt(): Int {
        val b = this.data
        var i = b[position++].toInt() and 0xff
        i = i or (b[position++].toInt() and 0xff shl 8)
        i = i or (b[position++].toInt() and 0xff shl 16)
        i = i or (b[position++].toInt() and 0xff shl 24)
        return i
    }

    fun readFloat(): Float {
        return java.lang.Float.intBitsToFloat(readInt())
    }

    fun readLong(): Long {
        val b = this.data
        var l = (b[position++].toInt() and 0xff).toLong()
        l = l or ((b[position++].toInt() and 0xff).toLong() shl 8)
        l = l or ((b[position++].toInt() and 0xff).toLong() shl 16)
        l = l or ((b[position++].toInt() and 0xff).toLong() shl 24)
        l = l or ((b[position++].toInt() and 0xff).toLong() shl 32)
        l = l or ((b[position++].toInt() and 0xff).toLong() shl 40)
        l = l or ((b[position++].toInt() and 0xff).toLong() shl 48)
        l = l or ((b[position++].toInt() and 0xff).toLong() shl 56)
        return l
    }

    fun readDouble(): Double {
        return java.lang.Double.longBitsToDouble(readLong())
    }

    fun readLength(): Long {
        val length = data[position++].toInt() and 0xff
        when (length) {
            251 -> return NULL_LENGTH
            252 -> return readUB2().toLong()
            253 -> return readUB3().toLong()
            254 -> return readLong()
            else -> return length.toLong()
        }
    }

    fun readBytes(): ByteArray {
        if (position >= length) {
            return EMPTY_BYTES
        }
        val ab = ByteArray(length - position)
        System.arraycopy(data, position, ab, 0, ab.size)
        position = length
        return ab
    }

    fun readBytes(length: Int): ByteArray {
        val ab = ByteArray(length)
        System.arraycopy(data, position, ab, 0, length)
        position += length
        return ab
    }

    fun readBytesWithNull(): ByteArray {
        val b = this.data
        if (position >= length) {
            return EMPTY_BYTES
        }
        var offset = -1
        for (i in position..length - 1) {
            if (b[i].toInt() == 0) {
                offset = i
                break
            }
        }
        when (offset) {
            -1 -> {
                val ab1 = ByteArray(length - position)
                System.arraycopy(b, position, ab1, 0, ab1.size)
                position = length
                return ab1
            }
            0 -> {
                position++
                return EMPTY_BYTES
            }
            else -> {
                val ab2 = ByteArray(offset - position)
                System.arraycopy(b, position, ab2, 0, ab2.size)
                position = offset + 1
                return ab2
            }
        }
    }

    fun readBytesWithLength(): ByteArray? {
        val length = readLength().toInt()
        if (length.toLong() == NULL_LENGTH) {
            return null
        }
        if (length <= 0) {
            return EMPTY_BYTES
        }

        val ab = ByteArray(length)
        System.arraycopy(data, position, ab, 0, ab.size)
        position += length
        return ab
    }

    fun readString(): String? {
        if (position >= length) {
            return null
        }
        val s = String(data, position, length - position)
        position = length
        return s
    }

    @Throws(UnsupportedEncodingException::class)
    fun readString(charset: String): String? {
        if (position >= length) {
            return null
        }

        val s = String(data, position, length - position, kotlin.text.charset(charset))
        position = length
        return s
    }

    fun readStringWithNull(): String? {
        val b = this.data
        if (position >= length) {
            return null
        }
        var offset = -1
        for (i in position..length - 1) {
            if (b[i].toInt() == 0) {
                offset = i
                break
            }
        }
        if (offset == -1) {
            val s = String(b, position, length - position)
            position = length
            return s
        }
        if (offset > position) {
            val s = String(b, position, offset - position)
            position = offset + 1
            return s
        } else {
            position++
            return null
        }
    }

    @Throws(UnsupportedEncodingException::class)
    fun readStringWithNull(charset: String): String? {
        val b = this.data
        if (position >= length) {
            return null
        }
        var offset = -1
        for (i in position..length - 1) {
            if (b[i].toInt() == 0) {
                offset = i
                break
            }
        }
        when (offset) {
            -1 -> {
                val s1 = String(b, position, length - position, kotlin.text.charset(charset))
                position = length
                return s1
            }
            0 -> {
                position++
                return null
            }
            else -> {
                val s2 = String(b, position, offset - position, kotlin.text.charset(charset))
                position = offset + 1
                return s2
            }
        }
    }

    fun readStringWithLength(): String? {
        val length = readLength().toInt()
        if (length <= 0) {
            return null
        }
        val s = String(data, position, length)
        position += length
        return s
    }

    @Throws(UnsupportedEncodingException::class)
    fun readStringWithLength(charset: String): String? {
        val length = readLength().toInt()
        if (length <= 0) {
            return null
        }
        val s = String(data, position, length, kotlin.text.charset(charset))
        position += length
        return s
    }

    fun readTime(): java.sql.Time {
        move(6)
        val hour = read().toInt()
        val minute = read().toInt()
        val second = read().toInt()
        val cal = getLocalCalendar()
        cal.set(0, 0, 0, hour, minute, second)
        return Time(cal.timeInMillis)
    }

    fun readDate(): java.util.Date {
        val length = read()
        val year = readUB2()
        var month = read()
        val date = read()
        val hour = read().toInt()
        val minute = read().toInt()
        val second = read().toInt()
        if (length.toInt() == 11) {
            val nanos = readUB4()
            val cal = getLocalCalendar()
            cal.set(year, (--month).toInt(), date.toInt(), hour, minute, second)
            val time = Timestamp(cal.timeInMillis)
            time.nanos = nanos.toInt()
            return time
        } else {
            val cal = getLocalCalendar()
            cal.set(year, (--month).toInt(), date.toInt(), hour, minute, second)
            return java.sql.Date(cal.timeInMillis)
        }
    }

    fun readBigDecimal(): BigDecimal? {
        val src = readStringWithLength()
        return if (src == null) null else BigDecimal(src)
    }

    override fun toString(): String {
        return Arrays.toString(data)
    }

    companion object {
        val NULL_LENGTH: Long = -1
        private val EMPTY_BYTES = ByteArray(0)
        private val localCalendar = ThreadLocal<Calendar>()

        private fun getLocalCalendar(): Calendar {
            var cal: Calendar? = localCalendar.get()
            if (cal == null) {
                cal = Calendar.getInstance()
                localCalendar.set(cal)
            }
            return cal!!
        }
    }

}