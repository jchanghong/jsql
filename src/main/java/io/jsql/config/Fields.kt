/*
 * Java-based distributed database like Mysql
 */
package io.jsql.config

/**
 * 字段类型及标识定义

 * @author jsql
 */
interface Fields {
    companion object {
        /**
         * field data type
         */
        val FIELD_TYPE_DECIMAL = 0
        val FIELD_TYPE_TINY = 1
        val FIELD_TYPE_SHORT = 2
        val FIELD_TYPE_LONG = 3
        val FIELD_TYPE_FLOAT = 4
        val FIELD_TYPE_DOUBLE = 5
        val FIELD_TYPE_NULL = 6
        val FIELD_TYPE_TIMESTAMP = 7
        val FIELD_TYPE_LONGLONG = 8
        val FIELD_TYPE_INT24 = 9
        val FIELD_TYPE_DATE = 10
        val FIELD_TYPE_TIME = 11
        val FIELD_TYPE_DATETIME = 12
        val FIELD_TYPE_YEAR = 13
        val FIELD_TYPE_NEWDATE = 14
        val FIELD_TYPE_VARCHAR = 15
        val FIELD_TYPE_BIT = 16
        val FIELD_TYPE_NEW_DECIMAL = 246
        val FIELD_TYPE_ENUM = 247
        val FIELD_TYPE_SET = 248
        val FIELD_TYPE_TINY_BLOB = 249
        val FIELD_TYPE_MEDIUM_BLOB = 250
        val FIELD_TYPE_LONG_BLOB = 251
        val FIELD_TYPE_BLOB = 252
        val FIELD_TYPE_VAR_STRING = 253
        val FIELD_TYPE_STRING = 254
        val FIELD_TYPE_GEOMETRY = 255

        /**
         * field flag
         */
        val NOT_NULL_FLAG = 0x0001
        val PRI_KEY_FLAG = 0x0002
        val UNIQUE_KEY_FLAG = 0x0004
        val MULTIPLE_KEY_FLAG = 0x0008
        val BLOB_FLAG = 0x0010
        val UNSIGNED_FLAG = 0x0020
        val ZEROFILL_FLAG = 0x0040
        val BINARY_FLAG = 0x0080
        val ENUM_FLAG = 0x0100
        val AUTO_INCREMENT_FLAG = 0x0200
        val TIMESTAMP_FLAG = 0x0400
        val SET_FLAG = 0x0800
    }

}