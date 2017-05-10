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
//import io.jsql.util.StringUtil
//import org.slf4j.Logger
//import org.slf4j.LoggerFactory
//
//import java.beans.BeanInfo
//import java.beans.IntrospectionException
//import java.beans.Introspector
//import java.beans.PropertyDescriptor
//import java.lang.reflect.InvocationTargetException
//import java.lang.reflect.Method
//import java.util.ArrayList
//import java.util.HashMap
//
///**
// * @author jsql
// */
//object ParameterMapping {
//    private val LOGGER = LoggerFactory
//            .getLogger(ParameterMapping::class.java)
//    private val descriptors = HashMap<Class<*>, Array<PropertyDescriptor>>()
//
//    /**
//     * 将property键值对赋值组装到object中
//
//     * @param object    目标反射对象
//     * *
//     * @param parameter property的键值对
//     * *
//     * @throws IllegalAccessException
//     * *
//     * @throws InvocationTargetException
//     */
//    @Throws(IllegalAccessException::class, InvocationTargetException::class)
//    fun mapping(`object`: Any, parameter: Map<String, Any>) {
//        //获取用于导出clazz这个JavaBean的所有属性的PropertyDescriptor
//        val pds = getDescriptors(`object`.javaClass)
//        for (i in pds.indices) {
//            val pd = pds[i]
//            val obj = parameter[pd.name]
//            var value: Any? = obj
//            val cls = pd.propertyType
//            //类型转换
//            if (obj is String) {
//                var string = obj
//                if (!StringUtil.isEmpty(string)) {
//                    string = ConfigUtil.filter(string)
//                }
//                if (isPrimitiveType(cls)) {
//                    value = convert(cls, string)
//                }
//            } else if (obj is BeanConfig) {
//                value = createBean(obj)
//            } else if (obj is Array<*>) {
//                val list = ArrayList<Any>()
//                for (beanconfig in obj) {
//                    list.add(createBean(beanconfig as BeanConfig))
//                }
//                value = list.toTypedArray()
//            }
//            //赋值
//            if (cls != null && value != null) {
//                val method = pd.writeMethod
//                method?.invoke(`object`, value)
//            }
//        }
//    }
//
//    @Throws(IllegalAccessException::class, InvocationTargetException::class)
//    fun createBean(config: BeanConfig): Any {
//        val bean = config.create(true)
//        if (bean is Map<*, *>) {
//            val map = bean as Map<String, Any>
//            for (entry in config.params.entries) {
//                val key = entry.key
//                var value = entry.value
//                if (value is BeanConfig) {
//                    val mapBeanConfig = entry.value as BeanConfig
//                    value = mapBeanConfig.create(true)
//                    mapping(value, mapBeanConfig.params)
//                }
//                map.put(key, value)
//            }
//        } else if (bean is List<*>) {
//        } else {
//            mapping(bean, config.params)
//        }
//        return bean
//    }
//
//    /**
//     * 用于导出clazz这个JavaBean的所有属性的PropertyDescriptor
//
//     * @param clazz
//     * *
//     * @return
//     */
//    private fun getDescriptors(clazz: Class<*>): Array<PropertyDescriptor> {
//        //PropertyDescriptor类表示JavaBean类通过存储器导出一个属性
//        val pds: Array<PropertyDescriptor>
//        val list: MutableList<PropertyDescriptor>
//        var pds2: Array<PropertyDescriptor>? = descriptors[clazz]
//        //该clazz是否第一次加载
//        if (null == pds2) {
//            try {
//                val beanInfo = Introspector.getBeanInfo(clazz)
//                pds = beanInfo.propertyDescriptors
//                list = ArrayList<PropertyDescriptor>()
//                //加载每一个类型不为空的property
//                for (i in pds.indices) {
//                    if (null != pds[i].propertyType) {
//                        list.add(pds[i])
//                    }
//                }
//                pds2 = arrayOfNulls<PropertyDescriptor>(list.size)
//                list.toTypedArray()
//            } catch (ie: IntrospectionException) {
//                LOGGER.error("ParameterMappingError", ie)
//                pds2 = arrayOfNulls<PropertyDescriptor>(0)
//            }
//
//        }
//        descriptors.put(clazz, pds2)
//        return pds2
//    }
//
//    private fun convert(cls: Class<*>, string: String): Any {
//        var method: Method? = null
//        var value: Any? = null
//        if (cls == String::class.java) {
//            value = string
//        } else if (cls == java.lang.Boolean.TYPE) {
//            value = java.lang.Boolean.valueOf(string)
//        } else if (cls == java.lang.Byte.TYPE) {
//            value = java.lang.Byte.valueOf(string)
//        } else if (cls == java.lang.Short.TYPE) {
//            value = java.lang.Short.valueOf(string)
//        } else if (cls == Integer.TYPE) {
//            value = Integer.valueOf(string)
//        } else if (cls == java.lang.Long.TYPE) {
//            value = java.lang.Long.valueOf(string)
//        } else if (cls == java.lang.Double.TYPE) {
//            value = java.lang.Double.valueOf(string)
//        } else if (cls == java.lang.Float.TYPE) {
//            value = java.lang.Float.valueOf(string)
//        } else if (cls == Boolean::class.java || cls == Byte::class.java || cls == Short::class.java
//                || cls == Int::class.java || cls == Long::class.java || cls == Float::class.java
//                || cls == Double::class.java) {
//            try {
//                method = cls.getMethod("valueOf", String::class.java)
//                value = method!!.invoke(null, string)
//            } catch (t: Exception) {
//                LOGGER.error("valueofError", t)
//                value = null
//            }
//
//        } else if (cls == Class<*>::class.java) {
//            try {
//                value = Class.forName(string)
//            } catch (e: ClassNotFoundException) {
//                throw ConfigException(e)
//            }
//
//        } else {
//            value = null
//        }
//        return value
//    }
//
//    private fun isPrimitiveType(cls: Class<*>): Boolean {
//        return cls == String::class.java || cls == java.lang.Boolean.TYPE || cls == java.lang.Byte.TYPE || cls == java.lang.Short.TYPE
//                || cls == Integer.TYPE || cls == java.lang.Long.TYPE || cls == java.lang.Double.TYPE
//                || cls == java.lang.Float.TYPE || cls == Boolean::class.java || cls == Byte::class.java
//                || cls == Short::class.java || cls == Int::class.java || cls == Long::class.java
//                || cls == Float::class.java || cls == Double::class.java || cls == Class<*>::class.java
//    }
//
//}