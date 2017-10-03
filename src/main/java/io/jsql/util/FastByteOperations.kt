/*
 * Java-based distributed database like Mysql
 */
package io.jsql.util

import com.google.common.primitives.Longs
import com.google.common.primitives.UnsignedBytes
import com.google.common.primitives.UnsignedLongs
import sun.misc.Unsafe
import java.nio.Buffer
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.security.AccessController
import java.security.PrivilegedAction

/**
 * Utility code to do optimized byte-array comparison.
 * This is borrowed and slightly modified from Guava's [UnsignedBytes]
 * class to be able to compare arrays that start at non-zero offsets.
 */
object FastByteOperations {

    /**
     * Lexicographically compare two byte arrays.
     */
    fun compareUnsigned(b1: ByteArray, s1: Int, l1: Int, b2: ByteArray, s2: Int, l2: Int): Int {
        return BestHolder.BEST.compare(b1, s1, l1, b2, s2, l2)
    }

    fun compareUnsigned(b1: ByteBuffer, b2: ByteArray, s2: Int, l2: Int): Int {
        return BestHolder.BEST.compare(b1, b2, s2, l2)
    }

    fun compareUnsigned(b1: ByteArray, s1: Int, l1: Int, b2: ByteBuffer): Int {
        return -BestHolder.BEST.compare(b2, b1, s1, l1)
    }

    fun compareUnsigned(b1: ByteBuffer, b2: ByteBuffer): Int {
        return BestHolder.BEST.compare(b1, b2)
    }

    fun copy(src: ByteBuffer, srcPosition: Int, trg: ByteArray, trgPosition: Int, length: Int) {
        BestHolder.BEST.copy(src, srcPosition, trg, trgPosition, length)
    }

    fun copy(src: ByteBuffer, srcPosition: Int, trg: ByteBuffer, trgPosition: Int, length: Int) {
        BestHolder.BEST.copy(src, srcPosition, trg, trgPosition, length)
    }

    interface ByteOperations {
        fun compare(buffer1: ByteArray, offset1: Int, length1: Int,
                    buffer2: ByteArray, offset2: Int, length2: Int): Int

        fun compare(buffer1: ByteBuffer, buffer2: ByteArray, offset2: Int, length2: Int): Int

        fun compare(buffer1: ByteBuffer, buffer2: ByteBuffer): Int

        fun copy(src: ByteBuffer, srcPosition: Int, trg: ByteArray, trgPosition: Int, length: Int)

        fun copy(src: ByteBuffer, srcPosition: Int, trg: ByteBuffer, trgPosition: Int, length: Int)
    }

    /**
     * Provides a lexicographical comparer implementation; either a Java
     * implementation or a faster implementation based on [Unsafe].
     *
     *
     *
     * Uses reflection to gracefully fall back to the Java implementation if
     * `Unsafe` isn't available.
     */
    private object BestHolder {
        internal val UNSAFE_COMPARER_NAME = FastByteOperations::class.java.name + "\$UnsafeOperations"
        internal val BEST = best

        /**
         * Returns the Unsafe-using Comparer, or falls back to the pure-Java
         * implementation if unable to do so.
         */
        internal // yes, UnsafeComparer does implement Comparer<byte[]>
                //JVMStabilityInspector.inspectThrowable(t);
                // ensure we really catch *everything*
        val best: ByteOperations
            get() {
                val arch = System.getProperty("os.arch")
                val unaligned = arch == "i386" || arch == "x86"
                        || arch == "amd64" || arch == "x86_64"
                if (!unaligned) {
                    return PureJavaOperations()
                }
                try {
                    val theClass = Class.forName(UNSAFE_COMPARER_NAME)
                    val comparer = theClass.getConstructor().newInstance() as ByteOperations
                    return comparer
                } catch (t: Throwable) {
                    return PureJavaOperations()
                }

            }

    }

    // used via reflection
    class UnsafeOperations : ByteOperations {

        override fun compare(buffer1: ByteArray, offset1: Int, length1: Int, buffer2: ByteArray, offset2: Int, length2: Int): Int {
            return compareTo(buffer1, BYTE_ARRAY_BASE_OFFSET + offset1, length1,
                    buffer2, BYTE_ARRAY_BASE_OFFSET + offset2, length2)
        }

        override fun compare(buffer1: ByteBuffer, buffer2: ByteArray, offset2: Int, length2: Int): Int {
            val obj1: Any?
            var offset1: Long
            if (buffer1.hasArray()) {
                obj1 = buffer1.array()
                offset1 = BYTE_ARRAY_BASE_OFFSET + buffer1.arrayOffset()
            } else {
                obj1 = null
                offset1 = theUnsafe.getLong(buffer1, DIRECT_BUFFER_ADDRESS_OFFSET)
            }
            var length1: Int
            run {
                val position = buffer1.position()
                val limit = buffer1.limit()
                length1 = limit - position
                offset1 += position.toLong()
            }
//            return compareTo(obj1, offset1, length1, buffer2, BYTE_ARRAY_BASE_OFFSET + offset2, length2)
            return 0
        }

        override fun compare(buffer1: ByteBuffer, buffer2: ByteBuffer): Int {
            return compareTo(buffer1, buffer2)
        }

        override fun copy(src: ByteBuffer, srcPosition: Int, trg: ByteArray, trgPosition: Int, length: Int) {
            if (src.hasArray()) {
                System.arraycopy(src.array(), src.arrayOffset() + srcPosition, trg, trgPosition, length)
            } else {
                copy(null, srcPosition + theUnsafe.getLong(src, DIRECT_BUFFER_ADDRESS_OFFSET), trg, trgPosition, length)
            }
        }

        override fun copy(srcBuf: ByteBuffer, srcPosition: Int, trgBuf: ByteBuffer, trgPosition: Int, length: Int) {
            val src: Any?
            val srcOffset: Long
            if (srcBuf.hasArray()) {
                src = srcBuf.array()
                srcOffset = BYTE_ARRAY_BASE_OFFSET + srcBuf.arrayOffset()
            } else {
                src = null
                srcOffset = theUnsafe.getLong(srcBuf, DIRECT_BUFFER_ADDRESS_OFFSET)
            }
//            copy(src, srcOffset + srcPosition, trgBuf, trgPosition, length)
        }

        companion object {
            internal val theUnsafe: Unsafe
            /**
             * The offset to the first element in a byte array.
             */
            internal val BYTE_ARRAY_BASE_OFFSET: Long
            internal val DIRECT_BUFFER_ADDRESS_OFFSET: Long
            internal val BIG_ENDIAN = ByteOrder.nativeOrder() == ByteOrder.BIG_ENDIAN
            // 1M, copied from java.nio.Bits (unfortunately a package-private class)
            private val UNSAFE_COPY_THRESHOLD = (1 shl 20).toLong()
            private val MIN_COPY_THRESHOLD: Long = 6

            init {
                theUnsafe = AccessController.doPrivileged(
                        {
                            try {
                                val f = Unsafe::class.java.getDeclaredField("theUnsafe")
                                f.isAccessible = true
//                                return @AccessController.doPrivileged f . get null

                            } catch (e: NoSuchFieldException) {
                                // It doesn't matter what we throw;
                                // it's swallowed in getBest().
                                throw Error()
                            } catch (e: IllegalAccessException) {
                                throw Error()
                            }
                        } as PrivilegedAction<*>) as Unsafe

                try {
                    BYTE_ARRAY_BASE_OFFSET = theUnsafe.arrayBaseOffset(ByteArray::class.java).toLong()
                    DIRECT_BUFFER_ADDRESS_OFFSET = theUnsafe.objectFieldOffset(Buffer::class.java.getDeclaredField("address"))
                } catch (e: Exception) {
                    throw AssertionError(e)
                }

                // sanity check - this should never fail
                if (theUnsafe.arrayIndexScale(ByteArray::class.java) != 1) {
                    throw AssertionError()
                }
            }

            fun copy(src: Any, srcOffset: Long, trgBuf: ByteBuffer, trgPosition: Int, length: Int) {
                if (trgBuf.hasArray()) {
                    copy(src, srcOffset, trgBuf.array(), trgBuf.arrayOffset() + trgPosition, length)
                } else {
                    copy(src, srcOffset, null, trgPosition + theUnsafe.getLong(trgBuf, DIRECT_BUFFER_ADDRESS_OFFSET), length.toLong())
                }
            }

            fun copy(src: Any?, srcOffset: Long, trg: ByteArray, trgPosition: Int, length: Int) {
                if (length <= MIN_COPY_THRESHOLD) {
                    for (i in 0..length - 1) {
                        trg[trgPosition + i] = theUnsafe.getByte(src, srcOffset + i)
                    }
                } else {
//                    copy(src, srcOffset, trg, BYTE_ARRAY_BASE_OFFSET + trgPosition, length.toLong())
                }
            }

            fun copy(src: Any, srcOffset: Long, dst: Any?, dstOffset: Long, length: Long) {
                var srcOffset = srcOffset
                var dstOffset = dstOffset
                var length = length
                while (length > 0) {
                    val size = if (length > UNSAFE_COPY_THRESHOLD) UNSAFE_COPY_THRESHOLD else length
                    // if src or dst are null, the offsets are absolute base addresses:
                    theUnsafe.copyMemory(src, srcOffset, dst, dstOffset, size)
                    length -= size
                    srcOffset += size
                    dstOffset += size
                }
            }

            fun compareTo(buffer1: ByteBuffer, buffer2: ByteBuffer): Int {
                val obj1: Any?
                var offset1: Long
                val length1: Int
                if (buffer1.hasArray()) {
                    obj1 = buffer1.array()
                    offset1 = BYTE_ARRAY_BASE_OFFSET + buffer1.arrayOffset()
                } else {
                    obj1 = null
                    offset1 = theUnsafe.getLong(buffer1, DIRECT_BUFFER_ADDRESS_OFFSET)
                }
                offset1 += buffer1.position().toLong()
                length1 = buffer1.remaining()
                return compareTo(obj1!!, offset1, length1, buffer2)
            }

            fun compareTo(buffer1: Any, offset1: Long, length1: Int, buffer: ByteBuffer): Int {
                val obj2: Any?
                var offset2: Long

                val position = buffer.position()
                val limit = buffer.limit()
                if (buffer.hasArray()) {
                    obj2 = buffer.array()
                    offset2 = BYTE_ARRAY_BASE_OFFSET + buffer.arrayOffset()
                } else {
                    obj2 = null
                    offset2 = theUnsafe.getLong(buffer, DIRECT_BUFFER_ADDRESS_OFFSET)
                }
                val length2 = limit - position
                offset2 += position.toLong()

                return compareTo(buffer1, offset1, length1, obj2!!, offset2, length2)
            }

            /**
             * Lexicographically compare two arrays.

             * @param buffer1       left operand: a byte[] or null
             * *
             * @param buffer2       right operand: a byte[] or null
             * *
             * @param memoryOffset1 Where to start comparing in the left buffer (pure memory address if buffer1 is null, or relative otherwise)
             * *
             * @param memoryOffset2 Where to start comparing in the right buffer (pure memory address if buffer1 is null, or relative otherwise)
             * *
             * @param length1       How much to compare from the left buffer
             * *
             * @param length2       How much to compare from the right buffer
             * *
             * @return 0 if equal, < 0 if left is less than right, etc.
             */

            fun compareTo(buffer1: Any, memoryOffset1: Long, length1: Int,
                          buffer2: Any, memoryOffset2: Long, length2: Int): Int {
                val minLength = Math.min(length1, length2)

                /*
             * Compare 8 bytes at a time. Benchmarking shows comparing 8 bytes at a
             * time is no slower than comparing 4 bytes at a time even on 32-bit.
             * On the other hand, it is substantially faster on 64-bit.
             */
                val wordComparisons = minLength and 7.inv()
                run {
                    var i = 0
                    while (i < wordComparisons) {
                        val lw = theUnsafe.getLong(buffer1, memoryOffset1 + i.toLong())
                        val rw = theUnsafe.getLong(buffer2, memoryOffset2 + i.toLong())

                        if (lw != rw) {
                            if (BIG_ENDIAN) {
                                return UnsignedLongs.compare(lw, rw)
                            }

                            return UnsignedLongs.compare(java.lang.Long.reverseBytes(lw), java.lang.Long.reverseBytes(rw))
                        }
                        i += Longs.BYTES
                    }
                }

                for (i in wordComparisons..minLength - 1) {
                    val b1 = theUnsafe.getByte(buffer1, memoryOffset1 + i).toInt() and 0xFF
                    val b2 = theUnsafe.getByte(buffer2, memoryOffset2 + i).toInt() and 0xFF
                    if (b1 != b2) {
                        return b1 - b2
                    }
                }

                return length1 - length2
            }
        }

    }

    class PureJavaOperations : ByteOperations {
        override fun compare(buffer1: ByteArray, offset1: Int, length1: Int,
                             buffer2: ByteArray, offset2: Int, length2: Int): Int {
            // Short circuit equal case
            if (buffer1 == buffer2 && offset1 == offset2 && length1 == length2) {
                return 0
            }

            val end1 = offset1 + length1
            val end2 = offset2 + length2
            var i = offset1
            var j = offset2
            while (i < end1 && j < end2) {
                val a = buffer1[i].toInt() and 0xff
                val b = buffer2[j].toInt() and 0xff
                if (a != b) {
                    return a - b
                }
                i++
                j++
            }
            return length1 - length2
        }

        override fun compare(buffer1: ByteBuffer, buffer2: ByteArray, offset2: Int, length2: Int): Int {
            if (buffer1.hasArray()) {
                return compare(buffer1.array(), buffer1.arrayOffset() + buffer1.position(), buffer1.remaining(),
                        buffer2, offset2, length2)
            }
            return compare(buffer1, ByteBuffer.wrap(buffer2, offset2, length2))
        }

        override fun compare(buffer1: ByteBuffer, buffer2: ByteBuffer): Int {
            val end1 = buffer1.limit()
            val end2 = buffer2.limit()
            var i = buffer1.position()
            var j = buffer2.position()
            while (i < end1 && j < end2) {
                val a = buffer1.get(i).toInt() and 0xff
                val b = buffer2.get(j).toInt() and 0xff
                if (a != b) {
                    return a - b
                }
                i++
                j++
            }
            return buffer1.remaining() - buffer2.remaining()
        }

        override fun copy(src: ByteBuffer, srcPosition: Int, trg: ByteArray, trgPosition: Int, length: Int) {
            var src = src
            if (src.hasArray()) {
                System.arraycopy(src.array(), src.arrayOffset() + srcPosition, trg, trgPosition, length)
                return
            }
            src = src.duplicate()
            src.position(srcPosition)
            src.get(trg, trgPosition, length)
        }

        override fun copy(src: ByteBuffer, srcPosition: Int, trg: ByteBuffer, trgPosition: Int, length: Int) {
            var src = src
            var trg = trg
            if (src.hasArray() && trg.hasArray()) {
                System.arraycopy(src.array(), src.arrayOffset() + srcPosition, trg.array(), trg.arrayOffset() + trgPosition, length)
                return
            }
            src = src.duplicate()
            src.position(srcPosition).limit(srcPosition + length)
            trg = trg.duplicate()
            trg.position(trgPosition)
            trg.put(src)
        }
    }
}
