/*
 * Java-based distributed database like Mysql
 */

package io.jsql.hazelcast

import com.google.common.base.MoreObjects
import com.google.common.base.Objects
import com.google.common.collect.ComparisonChain
import io.jsql.util.Mlogger

import java.io.Serializable

/**
 * Created by 长宏 on 2017/5/4 0004.
 * 代表一个sql更新语句.
 * Log sequence number（LSN1）：当前系统LSN最大值，新的事务日志LSN将在此基础上生成（LSN1+新日志的大小）；
 *
 *
 * Log flushed up to（LSN2）：当前已经写入日志文件的LSN；
 *
 *
 * Oldest modified data log（LSN3）：当前最旧的脏页数据对应的LSN，写Checkpoint的时候直接将此LSN写入到日志文件；
 *
 *
 * Last checkpoint at（LSN4）：当前已经写入Checkpoint的LSN；
 */
class SqlUpdateLog(var LSN: Long, var sql: String, var db: String) : Comparable<SqlUpdateLog>, Mlogger, Serializable {

    constructor(log:String) : this(0,"","") {
       val strings= log.split(",")
        LSN=strings[0].toLong()
        sql=strings[1]
        db=strings[2]
    }

    override fun compareTo(o: SqlUpdateLog): Int {
        return ComparisonChain.start().compare(LSN, o.LSN).result()
    }

    override fun equals(obj: Any?): Boolean {
        val updateLog = obj as SqlUpdateLog?
        return Objects.equal(obj != null, true)
                && Objects.equal(updateLog!!.LSN, LSN)
                && Objects.equal(updateLog.sql, sql)
                && updateLog.db == db
    }

    override fun toString(): String {
        return MoreObjects.toStringHelper(this).add("LSN", LSN).add("sql", sql).add("db", db).toString()
    }

    //存文件中的格式，方便调试查看
     fun toStringLog(): String {
         return "$LSN,$sql,$db"

    }

    companion object {

        @JvmStatic fun main(args: Array<String>) {
            var sqlUpdateLog = SqlUpdateLog(1, "ddd", "db")
            println(sqlUpdateLog.toString())
            sqlUpdateLog= SqlUpdateLog("2,ddd2,db2")
            println(sqlUpdateLog.toString())
            println(sqlUpdateLog.toStringLog())
            var log2=SqlUpdateLog(sqlUpdateLog.toStringLog())
            println(log2.toString())


        }
    }
}
