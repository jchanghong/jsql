/*
 * Java-based distributed database like Mysql
 */
package io.jsql.util

import org.slf4j.LoggerFactory
import java.beans.IntrospectionException
import java.beans.Introspector
import java.io.*
import java.lang.reflect.InvocationTargetException
import java.util.*

/**
 * @author jsql
 */
object ObjectUtil {
    private val LOGGER = LoggerFactory.getLogger(ObjectUtil::class.java)


    fun getStaticFieldValue(className: String, fieldName: String): Any? {
        var clazz: Class<*>? = null
        try {
            clazz = Class.forName(className)
            val field = clazz!!.getField(fieldName)
            if (field != null) {
                return field.get(null)
            }
        } catch (e: ClassNotFoundException) {
            //LOGGER.error("getStaticFieldValue", e);
        } catch (e: IllegalAccessException) {
        } catch (e: NoSuchFieldException) {
        }

        return null
    }


    fun copyObject(`object`: Any): Any {
        val b = ByteArrayOutputStream()
        var s: ObjectOutputStream? = null
        try {
            s = ObjectOutputStream(b)
            s.writeObject(`object`)
            val ois = ObjectInputStream(ByteArrayInputStream(b.toByteArray()))
            return ois.readObject()
        } catch (e: IOException) {
            throw RuntimeException(e)
        } catch (e: ClassNotFoundException) {
            throw RuntimeException(e)
        }

    }

    /**
     * 递归地比较两个数组是否相同，支持多维数组。
     *
     *
     * 如果比较的对象不是数组，则此方法的结果同`ObjectUtil.equals`。
     *

     * @param array1 数组1
     * *
     * @param array2 数组2
     * *
     * @return 如果相等, 则返回`true`
     */
    fun equals(array1: Any?, array2: Any?): Boolean {
        if (array1 === array2) {
            return true
        }

        if (array1 == null || array2 == null) {
            return false
        }

        val clazz = array1.javaClass

        if (clazz != array2.javaClass) {
            return false
        }

        if (!clazz.isArray) {
            return array1 == array2
        }

        // array1和array2为同类型的数组
        if (array1 is LongArray) {
            val longArray1 = array1
            val longArray2 = array2 as LongArray?

            if (longArray1.size != longArray2!!.size) {
                return false
            }

            for (i in longArray1.indices) {
                if (longArray1[i] != longArray2[i]) {
                    return false
                }
            }

            return true
        } else if (array1 is IntArray) {
            val intArray1 = array1
            val intArray2 = array2 as IntArray?

            if (intArray1.size != intArray2!!.size) {
                return false
            }

            for (i in intArray1.indices) {
                if (intArray1[i] != intArray2[i]) {
                    return false
                }
            }

            return true
        } else if (array1 is ShortArray) {
            val shortArray1 = array1
            val shortArray2 = array2 as ShortArray?

            if (shortArray1.size != shortArray2!!.size) {
                return false
            }

            for (i in shortArray1.indices) {
                if (shortArray1[i] != shortArray2[i]) {
                    return false
                }
            }

            return true
        } else if (array1 is ByteArray) {
            val byteArray1 = array1
            val byteArray2 = array2 as ByteArray?

            if (byteArray1.size != byteArray2!!.size) {
                return false
            }

            for (i in byteArray1.indices) {
                if (byteArray1[i] != byteArray2[i]) {
                    return false
                }
            }

            return true
        } else if (array1 is DoubleArray) {
            val doubleArray1 = array1
            val doubleArray2 = array2 as DoubleArray?

            if (doubleArray1.size != doubleArray2!!.size) {
                return false
            }

            for (i in doubleArray1.indices) {
                if (java.lang.Double.doubleToLongBits(doubleArray1[i]) != java.lang.Double.doubleToLongBits(doubleArray2[i])) {
                    return false
                }
            }

            return true
        } else if (array1 is FloatArray) {
            val floatArray1 = array1
            val floatArray2 = array2 as FloatArray?

            if (floatArray1.size != floatArray2!!.size) {
                return false
            }

            for (i in floatArray1.indices) {
                if (java.lang.Float.floatToIntBits(floatArray1[i]) != java.lang.Float.floatToIntBits(floatArray2[i])) {
                    return false
                }
            }

            return true
        } else if (array1 is BooleanArray) {
            val booleanArray1 = array1
            val booleanArray2 = array2 as BooleanArray?

            if (booleanArray1.size != booleanArray2!!.size) {
                return false
            }

            for (i in booleanArray1.indices) {
                if (booleanArray1[i] != booleanArray2[i]) {
                    return false
                }
            }

            return true
        } else if (array1 is CharArray) {
            val charArray1 = array1
            val charArray2 = array2 as CharArray?

            if (charArray1.size != charArray2!!.size) {
                return false
            }

            for (i in charArray1.indices) {
                if (charArray1[i] != charArray2[i]) {
                    return false
                }
            }

            return true
        } else {
            val objectArray1 = array1 as Array<Any>?
            val objectArray2 = array2 as Array<Any>?

            if (objectArray1!!.size != objectArray2!!.size) {
                return false
            }

            for (i in objectArray1.indices) {
                if (!equals(objectArray1[i], objectArray2[i])) {
                    return false
                }
            }

            return true
        }
    }


    fun copyProperties(fromObj: Any, toObj: Any) {
        val fromClass = fromObj.javaClass
        val toClass = toObj.javaClass

        try {
            val fromBean = Introspector.getBeanInfo(fromClass)
            val toBean = Introspector.getBeanInfo(toClass)

            val toPd = toBean.propertyDescriptors
            val fromPd = Arrays.asList(*fromBean
                    .propertyDescriptors)

            for (propertyDescriptor in toPd) {
                propertyDescriptor.displayName
                val pd = fromPd[fromPd
                        .indexOf(propertyDescriptor)]
                if (pd.displayName == propertyDescriptor.displayName
                        && pd.displayName != "class"
                        && propertyDescriptor.writeMethod != null) {
                    propertyDescriptor.writeMethod.invoke(toObj, pd.readMethod.invoke(fromObj, null))
                }

            }
        } catch (e: IntrospectionException) {
            throw RuntimeException(e)
        } catch (e: InvocationTargetException) {
            throw RuntimeException(e)
        } catch (e: IllegalAccessException) {
            throw RuntimeException(e)
        } catch (e: IllegalArgumentException) {
            throw RuntimeException(e)
        }

    }
}
