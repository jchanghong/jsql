/*
 * Java-based distributed database like Mysql
 */

package io.jsql.sql.handler

import com.orientechnologies.orient.core.record.OElement
import io.jsql.sql.OConnection

/**
 * Created by 长宏 on 2017/5/3 0003.
 * 所有的sql语句处理器必须是这个类的子类.
 * 可以参考 data_mannipulation包下面的类。模仿实现。。。。。
 * 比如Mupdate。Mselect
 */
abstract class SqlStatementHander {
    /**
     * Support sq lstatement class.

     * @return the class支持的sql语句类型的class
     */
    abstract fun supportSQLstatement(): Class<out SQLStatement>

    /**
     * Handle.

     * @param sqlStatement the sql statement
     * *
     * @return the object 返回值只有4种可能，不然报错！！！
     * * 一种是long类型， 一种是MyResultSet 一种是null ,一种是string表示错误的消息
     * * 返回其他对面都是错误的
     * *
     * @throws Exception the exception
     */
    @Throws(Exception::class)
    protected abstract fun handle0(sqlStatement: SQLStatement, c: OConnection): Any?

    /**
     * Handle.
     * @param sqlStatement the sql statement
     * *
     * @param connection   the connection
     */
    fun handle(sqlStatement: SQLStatement, connection: OConnection) {
        try {
            val result = handle0(sqlStatement, connection)
            when (result) {
                null -> connection.writeok()
                is MyResultSet -> onsuccess(result.data, result.columns, connection)
                is Long -> onsuccess(result, connection)
                is String -> connection.writeErrMessage(result)
                else -> connection.writeok()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            onerror(e, connection)
        }

    }

    /**
     * Onsuccess.如果是更新语句，可以调用这个函数

     * @param influences_rows the influences rows
     * *
     * @param connection      the connection
     */
    private fun onsuccess(influences_rows: Long, connection: OConnection) {
        DefaultHander.onseccess(influences_rows, connection)
    }

    /**
     * Onerror.

     * @param e          the e
     * *
     * @param connection the connection
     */
    private fun onerror(e: Exception, connection: OConnection) {
        DefaultHander.onerror(e, connection)
    }

    /**
     * Onsuccess.如果有返回结果集。可以直接用这个函数。减少重复代码，

     * @param data       the data
     * *
     * @param columns
     * *
     * @param connection the connection
     */
    private fun onsuccess(data: List<OElement>, columns: List<String>, connection: OConnection) {

        DefaultHander.onseccess(data, columns, connection)
    }
}
