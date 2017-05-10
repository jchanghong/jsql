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
//import java.io.*
//import java.lang.reflect.*
//import java.util.Collections
//import java.util.HashMap
//
///**
// * @author jsql
// */
//class ReflectionProvider {
//
//    @Transient private var serializedDataCache: MutableMap<Class<*>, ByteArray> = Collections
//            .synchronizedMap(HashMap<Class<*>, ByteArray>())
//    @Transient private var fieldDictionary = FieldDictionary()
//
//    fun newInstance(type: Class<*>): Any {
//        try {
//            val c = type.declaredConstructors
//            for (i in c.indices) {
//                if (c[i].parameterTypes.size == 0) {
//                    if (!Modifier.isPublic(c[i].modifiers)) {
//                        c[i].isAccessible = true
//                    }
//                    return c[i].newInstance()
//                }
//            }
//            if (Serializable::class.java.isAssignableFrom(type)) {
//                return instantiateUsingSerialization(type)
//            } else {
//                throw ObjectAccessException("Cannot construct " + type.name
//                        + " as it does not have a no-args constructor")
//            }
//        } catch (e: InstantiationException) {
//            throw ObjectAccessException("Cannot construct " + type.name, e)
//        } catch (e: IllegalAccessException) {
//            throw ObjectAccessException("Cannot construct " + type.name, e)
//        } catch (e: InvocationTargetException) {
//            if (e.targetException is RuntimeException) {
//                throw e.targetException as RuntimeException
//            } else if (e.targetException is Error) {
//                throw e.targetException as Error
//            } else {
//                throw ObjectAccessException("Constructor for " + type.name + " threw an exception",
//                        e.targetException)
//            }
//        }
//
//    }
//
//    fun visitSerializableFields(`object`: Any, visitor: Visitor) {
//        val iterator = fieldDictionary.serializableFieldsFor(`object`.javaClass)
//        while (iterator.hasNext()) {
//            val field = iterator.next()
//            if (!fieldModifiersSupported(field)) {
//                continue
//            }
//            validateFieldAccess(field)
//            try {
//                val value = field.get(`object`)
//                visitor.visit(field.name, field.type, field.declaringClass, value)
//            } catch (e: IllegalArgumentException) {
//                throw ObjectAccessException("Could not get field " + field.javaClass + "." + field.name, e)
//            } catch (e: IllegalAccessException) {
//                throw ObjectAccessException("Could not get field " + field.javaClass + "." + field.name, e)
//            }
//
//        }
//    }
//
//    fun writeField(`object`: Any, fieldName: String, value: Any, definedIn: Class<*>) {
//        val field = fieldDictionary.field(`object`.javaClass, fieldName, definedIn)
//        validateFieldAccess(field)
//        try {
//            field.set(`object`, value)
//        } catch (e: IllegalArgumentException) {
//            throw ObjectAccessException("Could not set field " + field.name + "@" + `object`.javaClass, e)
//        } catch (e: IllegalAccessException) {
//            throw ObjectAccessException("Could not set field " + field.name + "@" + `object`.javaClass, e)
//        }
//
//    }
//
//    fun invokeMethod(`object`: Any, methodName: String, value: Any, definedIn: Class<*>) {
//        try {
//            val method = `object`.javaClass.getMethod(methodName, value.javaClass)
//            method.invoke(`object`, value)
//        } catch (e: Exception) {
//            throw ObjectAccessException("Could not invoke " + `object`.javaClass + "." + methodName, e)
//        }
//
//    }
//
//    fun getFieldType(`object`: Any, fieldName: String, definedIn: Class<*>): Class<*> {
//        return fieldDictionary.field(`object`.javaClass, fieldName, definedIn).type
//    }
//
//    fun fieldDefinedInClass(fieldName: String, type: Class<*>): Boolean {
//        try {
//            val field = fieldDictionary.field(type, fieldName, null)
//            return fieldModifiersSupported(field)
//        } catch (e: ObjectAccessException) {
//            return false
//        }
//
//    }
//
//    fun getField(definedIn: Class<*>, fieldName: String): Field {
//        return fieldDictionary.field(definedIn, fieldName, null)
//    }
//
//    private fun instantiateUsingSerialization(type: Class<*>): Any {
//        try {
//            val data: ByteArray
//            if (serializedDataCache.containsKey(type)) {
//                data = serializedDataCache!!!![type] as ByteArray
//            } else {
//                val bytes = ByteArrayOutputStream()
//                val stream = DataOutputStream(bytes)
//                stream.writeShort(ObjectStreamConstants.STREAM_MAGIC.toInt())
//                stream.writeShort(ObjectStreamConstants.STREAM_VERSION.toInt())
//                stream.writeByte(ObjectStreamConstants.TC_OBJECT.toInt())
//                stream.writeByte(ObjectStreamConstants.TC_CLASSDESC.toInt())
//                stream.writeUTF(type.name)
//                stream.writeLong(ObjectStreamClass.lookup(type).serialVersionUID)
//                stream.writeByte(2) // classDescFlags (2 = Serializable)
//                stream.writeShort(0) // field count
//                stream.writeByte(ObjectStreamConstants.TC_ENDBLOCKDATA.toInt())
//                stream.writeByte(ObjectStreamConstants.TC_NULL.toInt())
//                data = bytes.toByteArray()
//                serializedDataCache.put(type, data)
//            }
//
//            val `in` = ObjectInputStream(ByteArrayInputStream(data))
//            return `in`.readObject()
//        } catch (e: IOException) {
//            throw ObjectAccessException("Cannot create " + type.name + " by JDK serialization", e)
//        } catch (e: ClassNotFoundException) {
//            throw ObjectAccessException("Cannot find class " + e.message)
//        }
//
//    }
//
//    private fun fieldModifiersSupported(field: Field): Boolean {
//        return !(Modifier.isStatic(field.modifiers) || Modifier.isTransient(field.modifiers))
//    }
//
//    private fun validateFieldAccess(field: Field) {
//        if (Modifier.isFinal(field.modifiers)) {
//            if (JVMInfo.is15) {
//                field.isAccessible = true
//            } else {
//                throw ObjectAccessException("Invalid final field " + field.declaringClass.name + "."
//                        + field.name)
//            }
//        }
//    }
//
//    private fun readResolve(): Any {
//        serializedDataCache = Collections.synchronizedMap(HashMap<Class<*>, ByteArray>())
//        fieldDictionary = FieldDictionary()
//        return this
//    }
//
//}