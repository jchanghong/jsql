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
//import java.util.*
//
///**
// * @author jsql
// */
//class OrderRetainingMap<K, V> : HashMap<K, V>() {
//
//    private val keyOrder = ArraySet<K>()
//    private val valueOrder = ArrayList<V>()
//
//    override fun put(key: K, value: V): V? {
//        keyOrder.add(key)
//        valueOrder.add(value)
//        return super.put(key, value)
//    }
//
//    override fun values(): Collection<V>? {
//        return Collections.unmodifiableList(valueOrder)
//    }
//
//    override fun keySet(): Set<K> {
//        return Collections.unmodifiableSet(keyOrder)
//    }
//
////    override fun entrySet(): Set<Map.Entry<K, V>> {
////        throw UnsupportedOperationException()
////    }
//
////    /**
////     * @author jsql
////     */
////    private class ArraySet<T> : ArrayList<T>(), Set<T> {
////        companion object {
////
////            private val serialVersionUID = 1L
////        }
////    }
//
//    companion object {
//        private val serialVersionUID = 1L
//    }
//
//}