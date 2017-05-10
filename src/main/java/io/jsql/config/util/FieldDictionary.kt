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
//package io.jsql.config.util
//
//import java.lang.reflect.Field
//import java.util.Collections
//import java.util.HashMap
//
///**
// * @author jsql
// */
//class FieldDictionary {
//
//    private val nameCache = Collections
//            .synchronizedMap(HashMap<String, Map<String, Field>>())
//    private val keyCache = Collections
//            .synchronizedMap(HashMap<String, Map<FieldKey, Field>>())
//
//    /**
//     * Returns an iterator for all serializable fields for some class
//
//     * @param cls the class you are interested on
//     * *
//     * @return an iterator for its serializable fields
//     */
//    fun serializableFieldsFor(cls: Class<*>): Iterator<Field> {
//        return buildMap(cls, true)!!.values.iterator()
//    }
//
//    /**
//     * Returns an specific field of some class. If definedIn is null, it searchs
//     * for the field named 'name' inside the class cls. If definedIn is
//     * different than null, tries to find the specified field name in the
//     * specified class cls which should be defined in class definedIn (either
//     * equals cls or a one of it's superclasses)
//
//     * @param cls       the class where the field is to be searched
//     * *
//     * @param name      the field name
//     * *
//     * @param definedIn the superclass (or the class itself) of cls where the field
//     * *                  was defined
//     * *
//     * @return the field itself
//     */
//    fun field(cls: Class<*>, name: String, definedIn: Class<*>?): Field {
//        val fields = buildMap(cls, definedIn != null)
//        val field = fields!![if (definedIn != null) FieldKey(name, definedIn, 0) else name]
//        if (field == null) {
//            throw ObjectAccessException("No such field " + cls.name + "." + name)
//        } else {
//            return field
//        }
//    }
//
//    private fun buildMap(cls: Class<*>, tupleKeyed: Boolean): Map<FieldKey, Field>? {
//        var cls = cls
//        val clsName = cls.name
//        if (!nameCache.containsKey(clsName)) {
//            synchronized(keyCache) {
//                if (!nameCache.containsKey(clsName)) { // double check
//                    val keyedByFieldName = HashMap<String, Field>()
//                    val keyedByFieldKey = OrderRetainingMap<FieldKey, Field>()
//                    while (Any::class.java != cls) {
//                        val fields = cls.declaredFields
//                        if (JVMInfo.reverseFieldDefinition()) {
//                            var i = fields.size shr 1
//                            while (i-- > 0) {
//                                val idx = fields.size - i - 1
//                                val field = fields[i]
//                                fields[i] = fields[idx]
//                                fields[idx] = field
//                            }
//                        }
//                        for (i in fields.indices) {
//                            val field = fields[i]
//                            field.isAccessible = true
//                            if (!keyedByFieldName.containsKey(field.name)) {
//                                keyedByFieldName.put(field.name, field)
//                            }
//                            keyedByFieldKey.put(FieldKey(field.name, field.declaringClass, i), field)
//                        }
//                        cls = cls.superclass
//                    }
//                    nameCache.put(clsName, keyedByFieldName)
//                    keyCache.put(clsName, keyedByFieldKey)
//                }
//            }
//        }
////        return if (tupleKeyed) keyCache!![clsName] else nameCache[clsName]
//        return null
//
//    }
//
//    /**
//     * @author jsql
//     */
//    private class FieldKey(private val fieldName: String?, private val declaringClass: Class<*>?, private val order: Int) {
//        private val depth: Int?
//
//        init {
//            var c = declaringClass
//            var i = 0
//            while (c?.getSuperclass() != null) {
//                i++
//                c = c.getSuperclass()
//            }
//            //不用构造器创建Integer，用静态方法节省时间和空间，因为depth是不可变变量
//            depth = i
//        }
//
//        override fun equals(o: Any?): Boolean {
//            if (this === o) {
//                return true
//            }
//            if (o !is FieldKey) {
//                return false
//            }
//
//            val fieldKey = o
//
//            if (if (declaringClass != null)
//                declaringClass != fieldKey.declaringClass
//            else
//                fieldKey.declaringClass != null) {
//                return false
//            }
//
//            return if (fieldName != null) fieldName == fieldKey.fieldName else fieldKey.fieldName == null
//        }
//
//        override fun hashCode(): Int {
//            var result: Int
//            result = fieldName?.hashCode() ?: 0
//            result = 29 * result + (declaringClass?.hashCode() ?: 0)
//            return result
//        }
//
//        override fun toString(): String {
//            return "FieldKey{order=$order, writer=$depth, declaringClass=$declaringClass, fieldName='$fieldName'}"
//        }
//
//    }
//
//}