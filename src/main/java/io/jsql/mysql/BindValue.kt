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

/**
 * @author jsql
 */
class BindValue {

    var isNull: Boolean = false /* NULL indicator */
    var isLongData: Boolean = false /* long data indicator */
    var isSet: Boolean = false /* has this parameter been set */

    var length: Long = 0 /* Default length of data */
    var type: Int = 0 /* data type */
    var scale: Byte = 0

    /**
     * 数据值
     */
    var byteBinding: Byte = 0
    var shortBinding: Short = 0
    var intBinding: Int = 0
    var floatBinding: Float = 0.toFloat()
    var longBinding: Long = 0
    var doubleBinding: Double = 0.toDouble()
    var value: Any? = null /* Other value to store */

    fun reset() {
        this.isNull = false
        this.isLongData = false
        this.isSet = false

        this.length = 0
        this.type = 0
        this.scale = 0

        this.byteBinding = 0
        this.shortBinding = 0
        this.intBinding = 0
        this.floatBinding = 0f
        this.longBinding = 0L
        this.doubleBinding = 0.0
        this.value = null
    }

}