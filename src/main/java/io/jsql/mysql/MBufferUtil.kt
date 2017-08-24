/*
 * Java-based distributed database like Mysql
 */
package io.jsql.mysql


import io.netty.buffer.ByteBuf

object MBufferUtil {

    fun writeUB2(buffer: ByteBuf, i: Int) {
        buffer.writeByte((i and 0xff).toByte().toInt())
        buffer.writeByte(i.ushr(8).toByte().toInt())
    }

    fun writeUB3(buffer: ByteBuf, i: Int) {
        buffer.writeByte((i and 0xff).toByte().toInt())
        buffer.writeByte(i.ushr(8).toByte().toInt())
        buffer.writeByte(i.ushr(16).toByte().toInt())
    }

    fun writeInt(buffer: ByteBuf, i: Int) {
        buffer.writeByte((i and 0xff).toByte().toInt())
        buffer.writeByte(i.ushr(8).toByte().toInt())
        buffer.writeByte(i.ushr(16).toByte().toInt())
        buffer.writeByte(i.ushr(24).toByte().toInt())
    }

    fun writeFloat(buffer: ByteBuf, f: Float) {
        writeInt(buffer, java.lang.Float.floatToIntBits(f))
    }

    fun writeUB4(buffer: ByteBuf, l: Long) {
        buffer.writeByte((l and 0xff).toByte().toInt())
        buffer.writeByte(l.ushr(8).toByte().toInt())
        buffer.writeByte(l.ushr(16).toByte().toInt())
        buffer.writeByte(l.ushr(24).toByte().toInt())
    }

    fun writeLong(buffer: ByteBuf, l: Long) {
        buffer.writeByte((l and 0xff).toByte().toInt())
        buffer.writeByte(l.ushr(8).toByte().toInt())
        buffer.writeByte(l.ushr(16).toByte().toInt())
        buffer.writeByte(l.ushr(24).toByte().toInt())
        buffer.writeByte(l.ushr(32).toByte().toInt())
        buffer.writeByte(l.ushr(40).toByte().toInt())
        buffer.writeByte(l.ushr(48).toByte().toInt())
        buffer.writeByte(l.ushr(56).toByte().toInt())
    }

    fun writeDouble(buffer: ByteBuf, d: Double) {
        writeLong(buffer, java.lang.Double.doubleToLongBits(d))
    }

    fun writeLength(buffer: ByteBuf, l: Long) {
        if (l < 251) {
            buffer.writeByte(l.toByte().toInt())
        } else if (l < 0x10000L) {
            buffer.writeByte(252.toByte().toInt())
            writeUB2(buffer, l.toInt())
        } else if (l < 0x1000000L) {
            buffer.writeByte(253.toByte().toInt())
            writeUB3(buffer, l.toInt())
        } else {
            buffer.writeByte(254.toByte().toInt())
            writeLong(buffer, l)
        }
    }

    fun writeWithNull(buffer: ByteBuf, src: ByteArray) {
        buffer.writeBytes(src)
        buffer.writeByte(0.toByte().toInt())
    }

    fun writeWithLength(buffer: ByteBuf, src: ByteArray) {
        val length = src.size
        if (length < 251) {
            buffer.writeByte(length.toByte().toInt())
        } else if (length < 0x10000L) {
            buffer.writeByte(252.toByte().toInt())
            writeUB2(buffer, length)
        } else if (length < 0x1000000L) {
            buffer.writeByte(253.toByte().toInt())
            writeUB3(buffer, length)
        } else {
            buffer.writeByte(254.toByte().toInt())
            writeLong(buffer, length.toLong())
        }
        buffer.writeBytes(src)
    }

    fun writeWithLength(buffer: ByteBuf, src: ByteArray?, nullValue: Byte) {
        if (src == null) {
            buffer.writeByte(nullValue.toInt())
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