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
package io.jsql.config.util

import io.jsql.util.StringUtil
import org.w3c.dom.*
import org.xml.sax.ErrorHandler
import org.xml.sax.InputSource
import org.xml.sax.SAXException
import org.xml.sax.SAXParseException

import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.parsers.ParserConfigurationException
import java.io.IOException
import java.io.InputStream
import java.util.HashMap
import java.util.Properties

/**
 * @author jsql
 */
object ConfigUtil {

    @JvmOverloads fun filter(text: String, properties: Properties = System.getProperties()): String {
        val s = StringBuilder()
        var cur = 0
        val textLen = text.length
        var propStart = -1
        var propStop = -1
        var propName: String? = null
        var propValue: String? = null
        while (cur < textLen) {
            propStart = text.indexOf("\${", cur)
            if (propStart < 0) {
                break
            }
            s.append(text.substring(cur, propStart))
            propStop = text.indexOf("}", propStart)
            if (propStop < 0) {
                throw ConfigException("Unterminated property: " + text.substring(propStart))
            }
            propName = text.substring(propStart + 2, propStop)
            propValue = properties.getProperty(propName)
            if (propValue == null) {
                s.append("\${").append(propName).append('}')
            } else {
                s.append(propValue)
            }
            cur = propStop + 1
        }
        return s.append(text.substring(cur)).toString()
    }

    @Throws(ParserConfigurationException::class, SAXException::class, IOException::class)
    fun getDocument(dtd: InputStream, xml: InputStream): Document {
        val factory = DocumentBuilderFactory.newInstance()
        factory.isValidating = true
        factory.isNamespaceAware = false
        val builder = factory.newDocumentBuilder()
        builder.setEntityResolver { publicId, systemId -> InputSource(dtd) }
        builder.setErrorHandler(object : ErrorHandler {
            override fun warning(e: SAXParseException) {}

            @Throws(SAXException::class)
            override fun error(e: SAXParseException) {
                throw e
            }

            @Throws(SAXException::class)
            override fun fatalError(e: SAXParseException) {
                throw e
            }
        })
        return builder.parse(xml)
    }

    fun loadAttributes(e: Element): Map<String, Any> {
        val map = HashMap<String, Any>()
        val nm = e.attributes
        for (j in 0..nm.length - 1) {
            val n = nm.item(j)
            if (n is Attr) {
                val attr = n
                map.put(attr.name, attr.nodeValue)
            }
        }
        return map
    }

    fun loadElement(parent: Element, tagName: String): Element? {
        val nodeList = parent.getElementsByTagName(tagName)
        if (nodeList.length > 1) {
            throw ConfigException(tagName + " elements length  over one!")
        }
        if (nodeList.length == 1) {
            return nodeList.item(0) as Element
        } else {
            return null
        }
    }

    /**
     * 获取节点下所有property

     * @param parent
     * *
     * @return key-value property键值对
     */
    fun loadElements(parent: Element): Map<String, Any> {
        val map = HashMap<String, Any>()
        val children = parent.childNodes
        for (i in 0..children.length - 1) {
            val node = children.item(i)
            if (node is Element) {
                val e = node
                val name = e.nodeName
                //获取property
                if ("property" == name) {
                    val key = e.getAttribute("name")
                    val nl = e.getElementsByTagName("bean")
                    if (nl.length == 0) {
                        val value = e.textContent
                        val empty:Boolean = StringUtil.isEmpty(value)
                        map.put(key, if (empty) null!! else value.trim { it <= ' ' })
                    } else {
                        map.put(key, loadBean(nl.item(0) as Element)!!)
                    }
                }
            }
        }
        return map
    }

    fun loadBean(parent: Element, tagName: String): BeanConfig {
        val nodeList = parent.getElementsByTagName(tagName)
        if (nodeList.length > 1) {
            throw ConfigException(tagName + " elements length over one!")
        }
        return loadBean(nodeList.item(0) as Element)!!
    }

    fun loadBean(e: Element?): BeanConfig? {
        if (e == null) {
            return null
        }
        val bean = BeanConfig()
        bean.name = e.getAttribute("name")
        val element = loadElement(e, "className")
        if (element != null) {
            bean.className = element.textContent
        } else {
            bean.className = e.getAttribute("class")
        }
        bean.params = loadElements(e)
        return bean
    }

}