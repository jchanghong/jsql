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
//import java.text.AttributedString
//import java.util.HashMap
//
///**
// * @author jsql
// */
//class JVMInfo {
//
//    private val loaderCache = HashMap<String, Class<*>>()
////    private var reflectionProvider: ReflectionProvider? = null
//
//    fun loadClass(name: String): Class<*>? {
//        try {
//            var clazz: Class<*>? = loaderCache[name]
//            if (clazz == null) {
//                clazz = Class.forName(name, false, javaClass.classLoader)
//                loaderCache.put(name, clazz)
//            }
//            return clazz
//        } catch (e: ClassNotFoundException) {
//            return null
//        }
//
//    }
//
////    @Synchronized fun getReflectionProvider(): ReflectionProvider {
////        if (reflectionProvider == null) {
////            reflectionProvider = ReflectionProvider()
////        }
////        return reflectionProvider as ReflectionProvider
////    }
//
////    protected fun canUseSun14ReflectionProvider(): Boolean {
////        return (isSun || isApple || isHPUX || isIBM || isBlackdown || isBEAWithUnsafeSupport) && is14
////                && loadClass("sun.misc.Unsafe") != null
////    }
//
//    companion object {
//        private val DEFAULT_JAVA_VERSION = 1.3f
//        private val reverseFieldOrder: Boolean
//        private val majorJavaVersion = getMajorJavaVersion(System.getProperty("java.specification.version"))
//
//        init {
//            var reverse = false
//            val fields = AttributedString::class.java.declaredFields
//            for (i in fields.indices) {
//                if (fields[i].name == "text") {
//                    reverse = i > 3
//                }
//            }
//            reverseFieldOrder = reverse
//        }
//
//        /**
//         * Parses the java version system property to determine the major java
//         * version, ie 1.x
//
//         * @param javaVersion the system property 'java.specification.version'
//         * *
//         * @return A float of the form 1.x
//         */
//        fun getMajorJavaVersion(javaVersion: String): Float {
//            try {
//                return java.lang.Float.parseFloat(javaVersion.substring(0, 3))
//            } catch (e: NumberFormatException) {
//                // Some JVMs may not conform to the x.y.z java.version format
//                return DEFAULT_JAVA_VERSION
//            }
//
//        }
//
//        val is14: Boolean
//            get() = majorJavaVersion >= 1.4f
//
//        val is15: Boolean
//            get() = majorJavaVersion >= 1.5f
//
//        val is16: Boolean
//            get() = majorJavaVersion >= 1.6f
//
//        private val isSun: Boolean
//            get() = System.getProperty("java.vm.vendor").indexOf("Sun") != -1
//
//        private val isApple: Boolean
//            get() = System.getProperty("java.vm.vendor").indexOf("Apple") != -1
//
//        private val isHPUX: Boolean
//            get() = System.getProperty("java.vm.vendor").indexOf("Hewlett-Packard Company") != -1
//
//        private val isIBM: Boolean
//            get() = System.getProperty("java.vm.vendor").indexOf("IBM") != -1
//
//        private val isBlackdown: Boolean
//            get() = System.getProperty("java.vm.vendor").indexOf("Blackdown") != -1
//
//        /*
//     * Support for sun.misc.Unsafe and sun.reflect.ReflectionFactory is present
//     * in JRockit versions R25.1.0 and later, both 1.4.2 and 5.0 (and in future
//     * 6.0 builds).
//     */
//        private // This property should be "BEA Systems, Inc."
//                /*
//             * Recent 1.4.2 and 5.0 versions of JRockit have a java.vm.version
//             * string starting with the "R" JVM version number, i.e.
//             * "R26.2.0-38-57237-1.5.0_06-20060209..."
//             *//*
//                 * Wecould also check that it's R26 or later, but that is
//                 * implicitly true
//                 *//*
//             * For older JRockit versions we can check java.vm.info. JRockit
//             * 1.4.2 R24 -> "Native Threads, GC strategy: parallel" and JRockit
//             * 5.0 R25 -> "R25.2.0-28".
//             */// R25.1 or R25.2 supports Unsafe, other versions do not
//                // If non-BEA, or possibly some very old JRockit version
//        val isBEAWithUnsafeSupport: Boolean
//            get() {
//                if (System.getProperty("java.vm.vendor").indexOf("BEA") != -1) {
//                    val vmVersion = System.getProperty("java.vm.version")
//                    if (vmVersion.startsWith("R")) {
//                        return true
//                    }
//                    val vmInfo = System.getProperty("java.vm.info")
//                    if (vmInfo != null) {
//                        return vmInfo.startsWith("R25.1") || vmInfo.startsWith("R25.2")
//                    }
//                }
//                return false
//            }
//
//        fun reverseFieldDefinition(): Boolean {
//            return reverseFieldOrder
//        }
//
//        @JvmStatic fun main(args: Array<String>) {
//            println(majorJavaVersion)
//        }
//    }
//
//}