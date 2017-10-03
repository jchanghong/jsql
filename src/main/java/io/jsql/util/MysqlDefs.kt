/*
 * Java-based distributed database like Mysql
 */
/*
 * 	This program is free software; you can redistribute it and/or modify it under the terms of 
 * the GNU AFFERO GENERAL PUBLIC LICENSE as published by the Free Software Foundation; either version 3 of the License, 
 * or (at your option) any later version. 
 * 
 * 	This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  
 * See the GNU AFFERO GENERAL PUBLIC LICENSE for more details. 
 * 	You should have received a copy of the GNU AFFERO GENERAL PUBLIC LICENSE along with this program; 
 * if not, write to the Free Software Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package io.jsql.util

import java.sql.Types
import java.util.*

/**
 * copy from mysql-connector-j MysqlDefs contains many values that are needed
 * for communication with the MySQL server.

 * @author Mark Matthews
 * *
 * @version $Id: MysqlDefs.java 4724 2005-12-20 23:27:01Z mmatthews $
 */
object MysqlDefs {
    // ~ Static fields/initializers
    // ---------------------------------------------

    val COM_BINLOG_DUMP = 18

    val COM_CHANGE_USER = 17

    val COM_CLOSE_STATEMENT = 25

    val COM_CONNECT_OUT = 20

    val COM_END = 29

    val COM_EXECUTE = 23

    val COM_FETCH = 28

    val COM_LONG_DATA = 24

    val COM_PREPARE = 22

    val COM_REGISTER_SLAVE = 21

    val COM_RESET_STMT = 26

    val COM_SET_OPTION = 27

    val COM_TABLE_DUMP = 19

    val CONNECT = 11

    val CREATE_DB = 5

    val DEBUG = 13

    val DELAYED_INSERT = 16

    val DROP_DB = 6

    val FIELD_LIST = 4

    val FIELD_TYPE_BIT = 16

    val FIELD_TYPE_BLOB = 252

    val FIELD_TYPE_DATE = 10

    val FIELD_TYPE_DATETIME = 12

    // Data Types
    val FIELD_TYPE_DECIMAL = 0

    val FIELD_TYPE_DOUBLE = 5

    val FIELD_TYPE_ENUM = 247

    val FIELD_TYPE_FLOAT = 4

    val FIELD_TYPE_GEOMETRY = 255

    val FIELD_TYPE_INT24 = 9

    val FIELD_TYPE_LONG = 3

    val FIELD_TYPE_LONG_BLOB = 251

    val FIELD_TYPE_LONGLONG = 8

    val FIELD_TYPE_MEDIUM_BLOB = 250

    val FIELD_TYPE_NEW_DECIMAL = 246

    val FIELD_TYPE_NEWDATE = 14

    val FIELD_TYPE_NULL = 6

    val FIELD_TYPE_SET = 248

    val FIELD_TYPE_SHORT = 2

    val FIELD_TYPE_STRING = 254

    val FIELD_TYPE_TIME = 11

    val FIELD_TYPE_TIMESTAMP = 7

    val FIELD_TYPE_TINY = 1

    // Older data types
    val FIELD_TYPE_TINY_BLOB = 249

    val FIELD_TYPE_VAR_STRING = 253

    val FIELD_TYPE_VARCHAR = 15

    // Newer data types
    val FIELD_TYPE_YEAR = 13

    val INIT_DB = 2

    val LENGTH_BLOB: Long = 65535

    val LENGTH_LONGBLOB = 4294967295L

    val LENGTH_MEDIUMBLOB: Long = 16777215

    val LENGTH_TINYBLOB: Long = 255

    // Limitations
    val MAX_ROWS = 50000000 // From the MySQL FAQ

    /**
     * Used to indicate that the server sent no field-level character set
     * information, so the driver should use the connection-level character
     * encoding instead.
     */
    val NO_CHARSET_INFO = -1

    val OPEN_CURSOR_FLAG: Byte = 1

    val PING = 14

    val PROCESS_INFO = 10

    val PROCESS_KILL = 12

    val QUERY = 3

    val QUIT = 1

    // ~ Methods
    // ----------------------------------------------------------------

    val RELOAD = 7

    val SHUTDOWN = 8

    //
    // Constants defined from mysql
    //
    // DB Operations
    val SLEEP = 0

    val STATISTICS = 9

    val TIME = 15
    val SQL_STATE_BASE_TABLE_NOT_FOUND = "S0002" //$NON-NLS-1$
    val SQL_STATE_BASE_TABLE_OR_VIEW_ALREADY_EXISTS = "S0001" //$NON-NLS-1$
    val SQL_STATE_BASE_TABLE_OR_VIEW_NOT_FOUND = "42S02" //$NON-NLS-1$
    val SQL_STATE_COLUMN_ALREADY_EXISTS = "S0021" //$NON-NLS-1$
    val SQL_STATE_COLUMN_NOT_FOUND = "S0022" //$NON-NLS-1$
    val SQL_STATE_COMMUNICATION_LINK_FAILURE = "08S01" //$NON-NLS-1$
    val SQL_STATE_CONNECTION_FAIL_DURING_TX = "08007" //$NON-NLS-1$
    val SQL_STATE_CONNECTION_IN_USE = "08002" //$NON-NLS-1$
    val SQL_STATE_CONNECTION_NOT_OPEN = "08003" //$NON-NLS-1$
    val SQL_STATE_CONNECTION_REJECTED = "08004" //$NON-NLS-1$
    val SQL_STATE_DATE_TRUNCATED = "01004" //$NON-NLS-1$
    val SQL_STATE_DATETIME_FIELD_OVERFLOW = "22008" //$NON-NLS-1$
    val SQL_STATE_DEADLOCK = "41000" //$NON-NLS-1$
    val SQL_STATE_DISCONNECT_ERROR = "01002" //$NON-NLS-1$
    val SQL_STATE_DIVISION_BY_ZERO = "22012" //$NON-NLS-1$
    val SQL_STATE_DRIVER_NOT_CAPABLE = "S1C00" //$NON-NLS-1$
    val SQL_STATE_ERROR_IN_ROW = "01S01" //$NON-NLS-1$
    val SQL_STATE_GENERAL_ERROR = "S1000" //$NON-NLS-1$
    val SQL_STATE_ILLEGAL_ARGUMENT = "S1009" //$NON-NLS-1$
    val SQL_STATE_INDEX_ALREADY_EXISTS = "S0011" //$NON-NLS-1$
    val SQL_STATE_INDEX_NOT_FOUND = "S0012" //$NON-NLS-1$
    val SQL_STATE_INSERT_VALUE_LIST_NO_MATCH_COL_LIST = "21S01" //$NON-NLS-1$
    val SQL_STATE_INVALID_AUTH_SPEC = "28000" //$NON-NLS-1$
    val SQL_STATE_INVALID_CHARACTER_VALUE_FOR_CAST = "22018" // $NON_NLS
    val SQL_STATE_INVALID_COLUMN_NUMBER = "S1002" //$NON-NLS-1$
    val SQL_STATE_INVALID_CONNECTION_ATTRIBUTE = "01S00" //$NON-NLS-1$
    val SQL_STATE_MEMORY_ALLOCATION_FAILURE = "S1001" //$NON-NLS-1$
    val SQL_STATE_MORE_THAN_ONE_ROW_UPDATED_OR_DELETED = "01S04" //$NON-NLS-1$
    val SQL_STATE_NO_DEFAULT_FOR_COLUMN = "S0023" //$NON-NLS-1$
    val SQL_STATE_NO_ROWS_UPDATED_OR_DELETED = "01S03" //$NON-NLS-1$
    val SQL_STATE_NUMERIC_VALUE_OUT_OF_RANGE = "22003" //$NON-NLS-1$
    val SQL_STATE_PRIVILEGE_NOT_REVOKED = "01006" //$NON-NLS-1$
    // -
    // 1
    // $
    val SQL_STATE_SYNTAX_ERROR = "42000" //$NON-NLS-1$
    val SQL_STATE_TIMEOUT_EXPIRED = "S1T00" //$NON-NLS-1$
    val SQL_STATE_TRANSACTION_RESOLUTION_UNKNOWN = "08007" // $NON_NLS
    val SQL_STATE_UNABLE_TO_CONNECT_TO_DATASOURCE = "08001" //$NON-NLS-1$
    val SQL_STATE_WRONG_NO_OF_PARAMETERS = "07001" //$NON-NLS-1$
    val SQL_STATE_INVALID_TRANSACTION_TERMINATION = "2D000" // $NON_NLS
    private val mysqlToJdbcTypesMap = HashMap<String, Int>()

    init {
        mysqlToJdbcTypesMap.put("BIT", mysqlToJavaType(FIELD_TYPE_BIT))

        mysqlToJdbcTypesMap.put("TINYINT", mysqlToJavaType(FIELD_TYPE_TINY))
        mysqlToJdbcTypesMap.put("SMALLINT", mysqlToJavaType(FIELD_TYPE_SHORT))
        mysqlToJdbcTypesMap.put("MEDIUMINT", mysqlToJavaType(FIELD_TYPE_INT24))
        mysqlToJdbcTypesMap.put("INT", mysqlToJavaType(FIELD_TYPE_LONG))
        mysqlToJdbcTypesMap.put("INTEGER", mysqlToJavaType(FIELD_TYPE_LONG))
        mysqlToJdbcTypesMap.put("BIGINT", mysqlToJavaType(FIELD_TYPE_LONGLONG))
        mysqlToJdbcTypesMap.put("INT24", mysqlToJavaType(FIELD_TYPE_INT24))
        mysqlToJdbcTypesMap.put("REAL", mysqlToJavaType(FIELD_TYPE_DOUBLE))
        mysqlToJdbcTypesMap.put("FLOAT", mysqlToJavaType(FIELD_TYPE_FLOAT))
        mysqlToJdbcTypesMap.put("DECIMAL", mysqlToJavaType(FIELD_TYPE_DECIMAL))
        mysqlToJdbcTypesMap.put("NUMERIC", mysqlToJavaType(FIELD_TYPE_DECIMAL))
        mysqlToJdbcTypesMap.put("DOUBLE", mysqlToJavaType(FIELD_TYPE_DOUBLE))
        mysqlToJdbcTypesMap.put("CHAR", mysqlToJavaType(FIELD_TYPE_STRING))
        mysqlToJdbcTypesMap.put("VARCHAR", mysqlToJavaType(FIELD_TYPE_VAR_STRING))
        mysqlToJdbcTypesMap.put("DATE", mysqlToJavaType(FIELD_TYPE_DATE))
        mysqlToJdbcTypesMap.put("TIME", mysqlToJavaType(FIELD_TYPE_TIME))
        mysqlToJdbcTypesMap.put("YEAR", mysqlToJavaType(FIELD_TYPE_YEAR))
        mysqlToJdbcTypesMap.put("TIMESTAMP", mysqlToJavaType(FIELD_TYPE_TIMESTAMP))
        mysqlToJdbcTypesMap.put("DATETIME", mysqlToJavaType(FIELD_TYPE_DATETIME))
        mysqlToJdbcTypesMap.put("TINYBLOB", Types.BINARY)
        mysqlToJdbcTypesMap.put("BLOB", Types.LONGVARBINARY)
        mysqlToJdbcTypesMap.put("MEDIUMBLOB", Types.LONGVARBINARY)
        mysqlToJdbcTypesMap.put("LONGBLOB", Types.LONGVARBINARY)
        mysqlToJdbcTypesMap
                .put("TINYTEXT", Types.VARCHAR)
        mysqlToJdbcTypesMap
                .put("TEXT", Types.LONGVARCHAR)
        mysqlToJdbcTypesMap.put("MEDIUMTEXT", Types.LONGVARCHAR)
        mysqlToJdbcTypesMap.put("LONGTEXT", Types.LONGVARCHAR)
        mysqlToJdbcTypesMap.put("ENUM", mysqlToJavaType(FIELD_TYPE_ENUM))
        mysqlToJdbcTypesMap.put("SET", mysqlToJavaType(FIELD_TYPE_SET))
        mysqlToJdbcTypesMap.put("GEOMETRY", mysqlToJavaType(FIELD_TYPE_GEOMETRY))
    }

    /**
     * Maps the given MySQL type to the correct MJDBCtest type.
     */
    fun mysqlToJavaType(mysqlType: Int): Int {
        val jdbcType: Int

        when (mysqlType) {
            MysqlDefs.FIELD_TYPE_NEW_DECIMAL, MysqlDefs.FIELD_TYPE_DECIMAL -> jdbcType = Types.DECIMAL

            MysqlDefs.FIELD_TYPE_TINY -> jdbcType = Types.TINYINT

            MysqlDefs.FIELD_TYPE_SHORT -> jdbcType = Types.SMALLINT

            MysqlDefs.FIELD_TYPE_LONG -> jdbcType = Types.INTEGER

            MysqlDefs.FIELD_TYPE_FLOAT -> jdbcType = Types.REAL

            MysqlDefs.FIELD_TYPE_DOUBLE -> jdbcType = Types.DOUBLE

            MysqlDefs.FIELD_TYPE_NULL -> jdbcType = Types.NULL

            MysqlDefs.FIELD_TYPE_TIMESTAMP -> jdbcType = Types.TIMESTAMP

            MysqlDefs.FIELD_TYPE_LONGLONG -> jdbcType = Types.BIGINT

            MysqlDefs.FIELD_TYPE_INT24 -> jdbcType = Types.INTEGER

            MysqlDefs.FIELD_TYPE_DATE -> jdbcType = Types.DATE

            MysqlDefs.FIELD_TYPE_TIME -> jdbcType = Types.TIME

            MysqlDefs.FIELD_TYPE_DATETIME -> jdbcType = Types.TIMESTAMP

            MysqlDefs.FIELD_TYPE_YEAR -> jdbcType = Types.DATE

            MysqlDefs.FIELD_TYPE_NEWDATE -> jdbcType = Types.DATE

            MysqlDefs.FIELD_TYPE_ENUM -> jdbcType = Types.CHAR

            MysqlDefs.FIELD_TYPE_SET -> jdbcType = Types.CHAR

            MysqlDefs.FIELD_TYPE_TINY_BLOB -> jdbcType = Types.VARBINARY

            MysqlDefs.FIELD_TYPE_MEDIUM_BLOB -> jdbcType = Types.LONGVARBINARY

            MysqlDefs.FIELD_TYPE_LONG_BLOB -> jdbcType = Types.LONGVARBINARY

            MysqlDefs.FIELD_TYPE_BLOB -> jdbcType = Types.LONGVARBINARY

            MysqlDefs.FIELD_TYPE_VAR_STRING, MysqlDefs.FIELD_TYPE_VARCHAR -> jdbcType = Types.VARCHAR

            MysqlDefs.FIELD_TYPE_STRING -> jdbcType = Types.CHAR
            MysqlDefs.FIELD_TYPE_GEOMETRY -> jdbcType = Types.BINARY
            MysqlDefs.FIELD_TYPE_BIT -> jdbcType = Types.BIT
            else -> jdbcType = Types.VARCHAR
        }

        return jdbcType
    }

    fun javaTypeDetect(javaType: Int, scale: Int): Int {
        when (javaType) {
            Types.NUMERIC -> {
                run {
                    if (scale > 0) {
                        return Types.DECIMAL
                    } else {
                        return javaType
                    }
                }
                run { return javaType }
            }
            else -> {
                return javaType
            }
        }

    }

    fun javaTypeMysql(javaType: Int): Int {

        when (javaType) {
            Types.NUMERIC -> return MysqlDefs.FIELD_TYPE_DECIMAL

            Types.DECIMAL -> return MysqlDefs.FIELD_TYPE_NEW_DECIMAL

            Types.TINYINT -> return MysqlDefs.FIELD_TYPE_TINY

            Types.SMALLINT -> return MysqlDefs.FIELD_TYPE_SHORT

            Types.INTEGER -> return MysqlDefs.FIELD_TYPE_LONG

            Types.REAL -> return MysqlDefs.FIELD_TYPE_FLOAT

            Types.DOUBLE -> return MysqlDefs.FIELD_TYPE_DOUBLE

            Types.NULL -> return MysqlDefs.FIELD_TYPE_NULL

            Types.TIMESTAMP -> return MysqlDefs.FIELD_TYPE_TIMESTAMP

            Types.BIGINT -> return MysqlDefs.FIELD_TYPE_LONGLONG

            Types.DATE -> return MysqlDefs.FIELD_TYPE_DATE

            Types.TIME -> return MysqlDefs.FIELD_TYPE_TIME

            Types.VARBINARY -> return MysqlDefs.FIELD_TYPE_TINY_BLOB

            Types.LONGVARBINARY -> return MysqlDefs.FIELD_TYPE_BLOB
        //对应sqlserver的image类型
            27 -> return MysqlDefs.FIELD_TYPE_BLOB

            Types.VARCHAR -> return MysqlDefs.FIELD_TYPE_VAR_STRING

            Types.CHAR -> return MysqlDefs.FIELD_TYPE_STRING

            Types.BINARY -> return MysqlDefs.FIELD_TYPE_GEOMETRY

            Types.BIT -> return MysqlDefs.FIELD_TYPE_BIT
            Types.CLOB -> return MysqlDefs.FIELD_TYPE_VAR_STRING
            Types.BLOB -> return MysqlDefs.FIELD_TYPE_BLOB

        //修改by     magicdoom@gmail.com
        // 当jdbc连接非mysql的数据库时，需要把对应类型映射为mysql的类型，否则应用端会出错
            Types.NVARCHAR -> return MysqlDefs.FIELD_TYPE_VAR_STRING
            Types.NCHAR -> return MysqlDefs.FIELD_TYPE_STRING
            Types.NCLOB -> return MysqlDefs.FIELD_TYPE_VAR_STRING
            Types.LONGNVARCHAR -> return MysqlDefs.FIELD_TYPE_VAR_STRING

            else -> return MysqlDefs.FIELD_TYPE_VAR_STRING   //其他未知类型返回字符类型
        }//	return Types.VARCHAR;

    }
    // -
    // 1
    // $

    /**
     * Maps the given MySQL type to the correct MJDBCtest type.
     */
    internal fun mysqlToJavaType(mysqlType: String): Int {
        if (mysqlType.equals("BIT", ignoreCase = true)) {
            return mysqlToJavaType(FIELD_TYPE_BIT)
        } else if (mysqlType.equals("TINYINT", ignoreCase = true)) { //$NON-NLS-1$
            return mysqlToJavaType(FIELD_TYPE_TINY)
        } else if (mysqlType.equals("SMALLINT", ignoreCase = true)) { //$NON-NLS-1$
            return mysqlToJavaType(FIELD_TYPE_SHORT)
        } else if (mysqlType.equals("MEDIUMINT", ignoreCase = true)) { //$NON-NLS-1$
            return mysqlToJavaType(FIELD_TYPE_INT24)
        } else if (mysqlType.equals("INT", ignoreCase = true) || mysqlType.equals("INTEGER", ignoreCase = true)) { //$NON-NLS-1$ //$NON-NLS-2$
            return mysqlToJavaType(FIELD_TYPE_LONG)
        } else if (mysqlType.equals("BIGINT", ignoreCase = true)) { //$NON-NLS-1$
            return mysqlToJavaType(FIELD_TYPE_LONGLONG)
        } else if (mysqlType.equals("INT24", ignoreCase = true)) { //$NON-NLS-1$
            return mysqlToJavaType(FIELD_TYPE_INT24)
        } else if (mysqlType.equals("REAL", ignoreCase = true)) { //$NON-NLS-1$
            return mysqlToJavaType(FIELD_TYPE_DOUBLE)
        } else if (mysqlType.equals("FLOAT", ignoreCase = true)) { //$NON-NLS-1$
            return mysqlToJavaType(FIELD_TYPE_FLOAT)
        } else if (mysqlType.equals("DECIMAL", ignoreCase = true)) { //$NON-NLS-1$
            return mysqlToJavaType(FIELD_TYPE_DECIMAL)
        } else if (mysqlType.equals("NUMERIC", ignoreCase = true)) { //$NON-NLS-1$
            return mysqlToJavaType(FIELD_TYPE_DECIMAL)
        } else if (mysqlType.equals("DOUBLE", ignoreCase = true)) { //$NON-NLS-1$
            return mysqlToJavaType(FIELD_TYPE_DOUBLE)
        } else if (mysqlType.equals("CHAR", ignoreCase = true)) { //$NON-NLS-1$
            return mysqlToJavaType(FIELD_TYPE_STRING)
        } else if (mysqlType.equals("VARCHAR", ignoreCase = true)) { //$NON-NLS-1$
            return mysqlToJavaType(FIELD_TYPE_VAR_STRING)
        } else if (mysqlType.equals("DATE", ignoreCase = true)) { //$NON-NLS-1$
            return mysqlToJavaType(FIELD_TYPE_DATE)
        } else if (mysqlType.equals("TIME", ignoreCase = true)) { //$NON-NLS-1$
            return mysqlToJavaType(FIELD_TYPE_TIME)
        } else if (mysqlType.equals("YEAR", ignoreCase = true)) { //$NON-NLS-1$
            return mysqlToJavaType(FIELD_TYPE_YEAR)
        } else if (mysqlType.equals("TIMESTAMP", ignoreCase = true)) { //$NON-NLS-1$
            return mysqlToJavaType(FIELD_TYPE_TIMESTAMP)
        } else if (mysqlType.equals("DATETIME", ignoreCase = true)) { //$NON-NLS-1$
            return mysqlToJavaType(FIELD_TYPE_DATETIME)
        } else if (mysqlType.equals("TINYBLOB", ignoreCase = true)) { //$NON-NLS-1$
            return java.sql.Types.BINARY
        } else if (mysqlType.equals("BLOB", ignoreCase = true)) { //$NON-NLS-1$
            return java.sql.Types.LONGVARBINARY
        } else if (mysqlType.equals("MEDIUMBLOB", ignoreCase = true)) { //$NON-NLS-1$
            return java.sql.Types.LONGVARBINARY
        } else if (mysqlType.equals("LONGBLOB", ignoreCase = true)) { //$NON-NLS-1$
            return java.sql.Types.LONGVARBINARY
        } else if (mysqlType.equals("TINYTEXT", ignoreCase = true)) { //$NON-NLS-1$
            return java.sql.Types.VARCHAR
        } else if (mysqlType.equals("TEXT", ignoreCase = true)) { //$NON-NLS-1$
            return java.sql.Types.LONGVARCHAR
        } else if (mysqlType.equals("MEDIUMTEXT", ignoreCase = true)) { //$NON-NLS-1$
            return java.sql.Types.LONGVARCHAR
        } else if (mysqlType.equals("LONGTEXT", ignoreCase = true)) { //$NON-NLS-1$
            return java.sql.Types.LONGVARCHAR
        } else if (mysqlType.equals("ENUM", ignoreCase = true)) { //$NON-NLS-1$
            return mysqlToJavaType(FIELD_TYPE_ENUM)
        } else if (mysqlType.equals("SET", ignoreCase = true)) { //$NON-NLS-1$
            return mysqlToJavaType(FIELD_TYPE_SET)
        } else if (mysqlType.equals("GEOMETRY", ignoreCase = true)) {
            return mysqlToJavaType(FIELD_TYPE_GEOMETRY)
        } else if (mysqlType.equals("BINARY", ignoreCase = true)) {
            return Types.BINARY // no concrete type on the wire
        } else if (mysqlType.equals("VARBINARY", ignoreCase = true)) {
            return Types.VARBINARY // no concrete type on the wire
        }

        // Punt
        return java.sql.Types.OTHER
    }

    /**
     * @param mysqlType
     * *
     * @return
     */
    fun typeToName(mysqlType: Int): String {
        when (mysqlType) {
            MysqlDefs.FIELD_TYPE_DECIMAL -> return "FIELD_TYPE_DECIMAL"

            MysqlDefs.FIELD_TYPE_TINY -> return "FIELD_TYPE_TINY"

            MysqlDefs.FIELD_TYPE_SHORT -> return "FIELD_TYPE_SHORT"

            MysqlDefs.FIELD_TYPE_LONG -> return "FIELD_TYPE_LONG"

            MysqlDefs.FIELD_TYPE_FLOAT -> return "FIELD_TYPE_FLOAT"

            MysqlDefs.FIELD_TYPE_DOUBLE -> return "FIELD_TYPE_DOUBLE"

            MysqlDefs.FIELD_TYPE_NULL -> return "FIELD_TYPE_NULL"

            MysqlDefs.FIELD_TYPE_TIMESTAMP -> return "FIELD_TYPE_TIMESTAMP"

            MysqlDefs.FIELD_TYPE_LONGLONG -> return "FIELD_TYPE_LONGLONG"

            MysqlDefs.FIELD_TYPE_INT24 -> return "FIELD_TYPE_INT24"

            MysqlDefs.FIELD_TYPE_DATE -> return "FIELD_TYPE_DATE"

            MysqlDefs.FIELD_TYPE_TIME -> return "FIELD_TYPE_TIME"

            MysqlDefs.FIELD_TYPE_DATETIME -> return "FIELD_TYPE_DATETIME"

            MysqlDefs.FIELD_TYPE_YEAR -> return "FIELD_TYPE_YEAR"

            MysqlDefs.FIELD_TYPE_NEWDATE -> return "FIELD_TYPE_NEWDATE"

            MysqlDefs.FIELD_TYPE_ENUM -> return "FIELD_TYPE_ENUM"

            MysqlDefs.FIELD_TYPE_SET -> return "FIELD_TYPE_SET"

            MysqlDefs.FIELD_TYPE_TINY_BLOB -> return "FIELD_TYPE_TINY_BLOB"

            MysqlDefs.FIELD_TYPE_MEDIUM_BLOB -> return "FIELD_TYPE_MEDIUM_BLOB"

            MysqlDefs.FIELD_TYPE_LONG_BLOB -> return "FIELD_TYPE_LONG_BLOB"

            MysqlDefs.FIELD_TYPE_BLOB -> return "FIELD_TYPE_BLOB"

            MysqlDefs.FIELD_TYPE_VAR_STRING -> return "FIELD_TYPE_VAR_STRING"

            MysqlDefs.FIELD_TYPE_STRING -> return "FIELD_TYPE_STRING"

            MysqlDefs.FIELD_TYPE_VARCHAR -> return "FIELD_TYPE_VARCHAR"

            MysqlDefs.FIELD_TYPE_GEOMETRY -> return "FIELD_TYPE_GEOMETRY"

            else -> return " Unknown MySQL Type # " + mysqlType
        }
    }

    internal fun appendJdbcTypeMappingQuery(buf: StringBuffer,
                                            mysqlTypeColumnName: String) {

        buf.append("CASE ")
        val typesMap = HashMap<String, Int>()
        typesMap.putAll(mysqlToJdbcTypesMap)
        typesMap.put("BINARY", Types.BINARY)
        typesMap.put("VARBINARY", Types.VARBINARY)

        for (mysqlTypeName in typesMap.keys) {
            buf.append(" WHEN ")
            buf.append(mysqlTypeColumnName)
            buf.append("='")
            buf.append(mysqlTypeName)
            buf.append("' THEN ")
            buf.append(typesMap[mysqlTypeName])

            if (mysqlTypeName.equals("DOUBLE", ignoreCase = true)
                    || mysqlTypeName.equals("FLOAT", ignoreCase = true)
                    || mysqlTypeName.equals("DECIMAL", ignoreCase = true)
                    || mysqlTypeName.equals("NUMERIC", ignoreCase = true)) {
                buf.append(" WHEN ")
                buf.append(mysqlTypeColumnName)
                buf.append("='")
                buf.append(mysqlTypeName)
                buf.append(" unsigned' THEN ")
                buf.append(typesMap[mysqlTypeName])
            }
        }

        buf.append(" ELSE ")
        buf.append(Types.OTHER)
        buf.append(" END ")

    }
    // -
    // 1
    // $
}
