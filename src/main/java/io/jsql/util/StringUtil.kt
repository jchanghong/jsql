/*
 * Java-based distributed database like Mysql
 */
package io.jsql.util

import org.slf4j.LoggerFactory
import java.io.ByteArrayOutputStream
import java.util.*

/**
 * @author jsql
 */
object StringUtil {

    private val LOGGER = LoggerFactory.getLogger(StringUtil::class.java)
    private val EMPTY_BYTE_ARRAY = ByteArray(0)
    private val RANDOM = Random()
    private val CHARS = charArrayOf('1', '2', '3', '4', '5', '6', '7', '8', '9', '0', 'q', 'w', 'e', 'r', 't', 'y', 'u', 'i', 'o', 'p', 'a', 's', 'd', 'f', 'g', 'h', 'j', 'k', 'l', 'z', 'x', 'c', 'v', 'b', 'n', 'm', 'Q', 'W', 'E', 'R', 'T', 'Y', 'U', 'I', 'O', 'P', 'A', 'S', 'D', 'F', 'G', 'H', 'J', 'K', 'L', 'Z', 'X', 'C', 'V', 'B', 'N', 'M')

    fun encode(src: String?, charset: String): ByteArray? {
      return  try {
             src?.toByteArray(charset(charset))?:null
        } catch (e: Exception) {
             null
        }
    }


    fun hexString2Bytes(hexString: CharArray?, offset: Int,
                        length: Int): ByteArray? {
        if (hexString == null) {
            return null
        }
        if (length == 0) {
            return EMPTY_BYTE_ARRAY
        }
        val odd = length shl 31 == Integer.MIN_VALUE
        val bs = ByteArray(if (odd) length + 1 shr 1 else length shr 1)
        var i = offset
        val limit = offset + length
        while (i < limit) {
            val high: Char
            val low: Char
            if (i == offset && odd) {
                high = '0'
                low = hexString[i]
            } else {
                high = hexString[i]
                low = hexString[++i]
            }
            var b: Int
            when (high) {
                '0' -> b = 0
                '1' -> b = 0x10
                '2' -> b = 0x20
                '3' -> b = 0x30
                '4' -> b = 0x40
                '5' -> b = 0x50
                '6' -> b = 0x60
                '7' -> b = 0x70
                '8' -> b = 0x80
                '9' -> b = 0x90
                'a', 'A' -> b = 0xa0
                'b', 'B' -> b = 0xb0
                'c', 'C' -> b = 0xc0
                'd', 'D' -> b = 0xd0
                'e', 'E' -> b = 0xe0
                'f', 'F' -> b = 0xf0
                else -> throw IllegalArgumentException("illegal hex-string: " + String(hexString, offset, length))
            }
            when (low) {
                '0' -> {
                }
                '1' -> b += 1
                '2' -> b += 2
                '3' -> b += 3
                '4' -> b += 4
                '5' -> b += 5
                '6' -> b += 6
                '7' -> b += 7
                '8' -> b += 8
                '9' -> b += 9
                'a', 'A' -> b += 10
                'b', 'B' -> b += 11
                'c', 'C' -> b += 12
                'd', 'D' -> b += 13
                'e', 'E' -> b += 14
                'f', 'F' -> b += 15
                else -> throw IllegalArgumentException("illegal hex-string: " + String(hexString, offset, length))
            }
            bs[i - offset shr 1] = b.toByte()
            ++i
        }
        return bs
    }

    fun dumpAsHex(src: ByteArray, length: Int): String {
        val out = StringBuilder(length * 4)
        var p = 0
        val rows = length / 8
        run {
            var i = 0
            while (i < rows && p < length) {
                var ptemp = p
                for (j in 0..7) {
                    val hexVal = Integer.toHexString(src[ptemp].toInt() and 0xff)
                    if (hexVal.length == 1) {
                        out.append('0')
                    }
                    out.append(hexVal).append(' ')
                    ptemp++
                }
                out.append("    ")
                for (j in 0..7) {
                    val b = 0xff and src[p].toInt()
                    if (b in 33..126) {
                        out.append(b.toChar()).append(' ')
                    } else {
                        out.append(". ")
                    }
                    p++
                }
                out.append('\n')
                i++
            }
        }
        var n = 0
        for (i in p..length - 1) {
            val hexVal = Integer.toHexString(src[i].toInt() and 0xff)
            if (hexVal.length == 1) {
                out.append('0')
            }
            out.append(hexVal).append(' ')
            n++
        }
        for (i in n..7) {
            out.append("   ")
        }
        out.append("    ")
        for (i in p..length - 1) {
            val b = 0xff and src[i].toInt()
            if (b > 32 && b < 127) {
                out.append(b.toChar()).append(' ')
            } else {
                out.append(". ")
            }
        }
        out.append('\n')
        return out.toString()
    }

    fun escapeEasternUnicodeByteStream(src: ByteArray?,
                                       srcString: String, offset: Int, length: Int): ByteArray {
        if (src == null || src.size == 0) {
            return src!!
        }
        val bytesLen = src.size
        var bufIndex = 0
        var strIndex = 0
        val out = ByteArrayOutputStream(bytesLen)
        while (true) {
            if (srcString[strIndex] == '\\') {// write it out as-is
                out.write(src[bufIndex++].toInt())
            } else {// Grab the first byte
                var loByte = src[bufIndex].toInt()
                if (loByte < 0) {
                    loByte += 256 // adjust for signedness/wrap-around
                }
                out.write(loByte)// We always write the first byte
                if (loByte >= 0x80) {
                    if (bufIndex < bytesLen - 1) {
                        var hiByte = src[bufIndex + 1].toInt()
                        if (hiByte < 0) {
                            hiByte += 256 // adjust for signedness/wrap-around
                        }
                        out.write(hiByte)// write the high byte here, and
                        // increment the index for the high
                        // byte
                        bufIndex++
                        if (hiByte == 0x5C) {
                            out.write(hiByte)// escape 0x5c if necessary
                        }
                    }
                } else if (loByte == 0x5c && bufIndex < bytesLen - 1) {
                    var hiByte = src[bufIndex + 1].toInt()
                    if (hiByte < 0) {
                        hiByte += 256 // adjust for signedness/wrap-around
                    }
                    if (hiByte == 0x62) {// we need to escape the 0x5c
                        out.write(0x5c)
                        out.write(0x62)
                        bufIndex++
                    }
                }
                bufIndex++
            }
            if (bufIndex >= bytesLen) {
                break// we're done
            }
            strIndex++
        }
        return out.toByteArray()
    }

    fun toString(bytes: ByteArray?): String {
        if (bytes == null || bytes.size == 0) {
            return ""
        }
        val buffer = StringBuilder()
        for (byt in bytes) {
            buffer.append(byt.toChar())
        }
        return buffer.toString()
    }

    fun equalsIgnoreCase(str1: String?, str2: String?): Boolean {
        if (str1 == null) {
            return str2 == null
        }
        return str1.equals(str2!!, ignoreCase = true)
    }

    fun countChar(str: String?, c: Char): Int {
        if (str == null || str.isEmpty()) {
            return 0
        }
        val len = str.length
        var cnt = 0
        for (i in 0..len - 1) {
            if (c == str[i]) {
                ++cnt
            }
        }
        return cnt
    }

    /*
      insert into tablexxx

	  @param oriSql
	 * @return
	 */
    //	public static String getTableName(String oriSql) {
    //        //此处应该优化为去掉sql中的注释，或兼容注释
    //        String sql=null;
    //        if(oriSql.startsWith(LoadData.loadDataHint))
    //        {
    //           sql=oriSql.substring(LoadData.loadDataHint.length()) ;
    //        } else
    //        {
    //            sql=oriSql;
    //        }
    //		int pos = 0;
    //		boolean insertFound = false;
    //		boolean intoFound = false;
    //		int tableStartIndx = -1;
    //		int tableEndIndex = -1;
    //		while (pos < sql.length()) {
    //			char ch = sql.charAt(pos);
    //			// 忽略处理注释 /* */ BEN
    //			if(ch == '/' &&  pos+4 < sql.length() && sql.charAt(pos+1) == '*') {
    //				if(sql.substring(pos+2).indexOf("*/") != -1) {
    //					pos += sql.substring(pos+2).indexOf("*/")+4;
    //					continue;
    //				} else {
    //					// 不应该发生这类情况。
    //					throw new IllegalArgumentException("sql 注释 语法错误");
    //				}
    //			} else if (ch <= ' ' || ch == '(' || ch=='`') {//
    //				if (tableStartIndx > 0) {
    //					tableEndIndex = pos;
    //					break;
    //				} else {
    //					pos++;
    //					continue;
    //				}
    //			} else if (ch == 'i' || ch == 'I') {
    //				if (intoFound) {
    //					if (tableStartIndx == -1 && ch!='`') {
    //						tableStartIndx = pos;
    //					}
    //					pos++;
    //				} else if (insertFound) {// into start
    //					// 必须全部都为INTO才认为是into
    //					if(pos+5 < sql.length() && (sql.charAt(pos+1) == 'n' || sql.charAt(pos+1) == 'N') && (sql.charAt(pos+2) == 't' || sql.charAt(pos+2) == 'T') && (sql.charAt(pos+3) == 'o' || sql.charAt(pos+3) == 'O') && (sql.charAt(pos+4) <= ' ')) {
    //						pos = pos + 5;
    //						intoFound = true;
    //					} else {
    //						pos++;
    //					}
    //				} else {
    //					// 矫正必须全部都为 INSERT才认为是insert
    //					// insert start
    //					if(pos+7 < sql.length() && (sql.charAt(pos+1) == 'n' || sql.charAt(pos+1) == 'N') && (sql.charAt(pos+2) == 's' || sql.charAt(pos+2) == 'S')  && (sql.charAt(pos+3) == 'e' || sql.charAt(pos+3) == 'E') && (sql.charAt(pos+4) == 'r' || sql.charAt(pos+4) == 'R')  && (sql.charAt(pos+5) == 't' || sql.charAt(pos+5) == 'T') && (sql.charAt(pos+6) <= ' ')) {
    //						pos = pos + 7;
    //						insertFound = true;
    //					} else {
    //						pos++;
    //					}
    //				}
    //			} else {
    //				if (tableStartIndx == -1) {
    //					tableStartIndx = pos;
    //				}
    //				pos++;
    //			}
    //
    //		}
    //		return sql.substring(tableStartIndx, tableEndIndex);
    //	}

    /**
     * 移除`符号

     * @param str
     * *
     * @return
     */
    fun removeBackquote(str: String): String {
        //删除名字中的`tablename`和'value'
        if (str.length > 0) {
            val sb = StringBuilder(str)
            if (sb[0] == '`' || sb[0] == '\'') {
                sb.deleteCharAt(0)
            }
            if (sb[sb.length - 1] == '`' || sb[sb.length - 1] == '\'') {
                sb.deleteCharAt(sb.length - 1)
            }
            return sb.toString()
        }
        return ""
    }

}