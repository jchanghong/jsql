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
//import io.jsql.util.ObjectUtil
//
//import java.lang.reflect.InvocationTargetException
//import java.util.HashMap
//
///**
// * @author jsql
// */
//class BeanConfig : Cloneable {
//
//    var name: String? = null
//    var className: String? = null
//    var params: Map<String, Any> = HashMap()
//
//    @Throws(IllegalAccessException::class, InvocationTargetException::class)
//    fun create(initEarly: Boolean): Any {
//        var obj: Any? = null
//        try {
////            obj = refProvider.newInstance(Class.forName(className))
//        } catch (e: ClassNotFoundException) {
//            throw ConfigException(e)
//        }
//
////        ParameterMapping.mapping(obj, params)
//        if (initEarly && obj is Initializable) {
//            obj.init()
//        }
//        return obj!!
//    }
//
//    public override fun clone(): Any {
//        try {
//            super.clone()
//        } catch (e: CloneNotSupportedException) {
//            throw ConfigException(e)
//        }
//
//        var bc: BeanConfig? = null
//        try {
//            bc = javaClass.newInstance()
//        } catch (e: InstantiationException) {
//            throw ConfigException(e)
//        } catch (e: IllegalAccessException) {
//            throw ConfigException(e)
//        }
//
//        //        if (bc == null) {
//        //            return null;
//        //        }
//        bc!!.className = className
//        bc.name = name
//        //        Map<String, Object> params = new HashMap<String, Object>();
//        //        params.putAll(params);
//        return bc
//    }
//
//    override fun hashCode(): Int {
//        var hashcode = 37
//        hashcode += if (name == null) 0 else name!!.hashCode()
//        hashcode += if (className == null) 0 else className!!.hashCode()
//        hashcode += if (params == null) 0 else params!!.hashCode()
//        return hashcode
//    }
//
//    override fun equals(`object`: Any?): Boolean {
//        if (`object` is BeanConfig) {
//            val entity = `object`
//            var isEquals = equals(name, entity.name)
//            isEquals = isEquals && equals(className, entity.className)
//            isEquals = isEquals && ObjectUtil.equals(params, entity.params)
//            return isEquals
//        }
//        return false
//    }
//
//    companion object {
////        private val refProvider = ReflectionProvider()
//
//        private fun equals(str1: String?, str2: String?): Boolean {
//            if (str1 == null) {
//                return str2 == null
//            }
//            return str1 == str2
//        }
//    }
//
//}