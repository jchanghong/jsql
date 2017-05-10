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

import io.jsql.config.Fields

import java.io.UnsupportedEncodingException

/**
 * @author jsql
 */
object BindValueUtil {

    @Throws(UnsupportedEncodingException::class)
    fun read(mm: MySQLMessage, bv: BindValue, charset: String) {
        when (bv.type and 0xff) {
            Fields.FIELD_TYPE_BIT -> bv.value = mm.readBytesWithLength()
            Fields.FIELD_TYPE_TINY -> bv.byteBinding = mm.read()
            Fields.FIELD_TYPE_SHORT -> bv.shortBinding = mm.readUB2().toShort()
            Fields.FIELD_TYPE_LONG -> bv.intBinding = mm.readInt()
            Fields.FIELD_TYPE_LONGLONG -> bv.longBinding = mm.readLong()
            Fields.FIELD_TYPE_FLOAT -> bv.floatBinding = mm.readFloat()
            Fields.FIELD_TYPE_DOUBLE -> bv.doubleBinding = mm.readDouble()
            Fields.FIELD_TYPE_TIME -> bv.value = mm.readTime()
            Fields.FIELD_TYPE_DATE, Fields.FIELD_TYPE_DATETIME, Fields.FIELD_TYPE_TIMESTAMP -> bv.value = mm.readDate()
            Fields.FIELD_TYPE_VAR_STRING, Fields.FIELD_TYPE_STRING, Fields.FIELD_TYPE_VARCHAR -> {
                bv.value = mm.readStringWithLength(charset)
                if (bv.value == null) {
                    bv.isNull = true
                }
            }
            Fields.FIELD_TYPE_DECIMAL, Fields.FIELD_TYPE_NEW_DECIMAL -> {
                bv.value = mm.readBigDecimal()
                if (bv.value == null) {
                    bv.isNull = true
                }
            }
            Fields.FIELD_TYPE_BLOB -> bv.isLongData = true
            else -> throw IllegalArgumentException("bindValue error,unsupported type:" + bv.type)
        }
        bv.isSet = true
    }

}