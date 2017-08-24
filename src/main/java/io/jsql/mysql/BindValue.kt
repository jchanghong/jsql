/*
 * Java-based distributed database like Mysql
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