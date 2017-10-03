/*
 * Java-based distributed database like Mysql
 */
package io.jsql.mysql

import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.*

/**
 * @author jsql, CrazyPig
 */
class PreparedStatement(val id: Long, val statement: String, val columnsNumber: Int, val parametersNumber: Int) {
    val parametersType: IntArray
    /**
     * 存放COM_STMT_SEND_LONG_DATA命令发送过来的字节数据
     * <pre>
     * key : param_id
     * value : byte data
    </pre> *
     */
    private val longDataMap: MutableMap<Long, ByteArrayOutputStream>

    init {
        this.parametersType = IntArray(parametersNumber)
        this.longDataMap = HashMap<Long, ByteArrayOutputStream>()
    }

    fun getLongData(paramId: Long): ByteArrayOutputStream? {
        return longDataMap[paramId]
    }

    /**
     * COM_STMT_RESET命令将调用该方法进行数据重置
     */
    fun resetLongData() {
        for (paramId in longDataMap.keys) {
            longDataMap[paramId]!!.reset()
        }
    }

    /**
     * 追加数据到指定的预处理参数

     * @param paramId
     * *
     * @param data
     * *
     * @throws IOException
     */
    @Throws(IOException::class)
    fun appendLongData(paramId: Long, data: ByteArray) {
        if (getLongData(paramId) == null) {
            val out = ByteArrayOutputStream()
            out.write(data)
            longDataMap.put(paramId, out)
        } else {
            longDataMap[paramId]!!.write(data)
        }
    }
}