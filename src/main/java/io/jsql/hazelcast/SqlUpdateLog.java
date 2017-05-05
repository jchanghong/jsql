package io.jsql.hazelcast;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.collect.ComparisonChain;
import io.jsql.util.Mlogger;

import java.io.Serializable;

/**
 * Created by 长宏 on 2017/5/4 0004.
 * 代表一个sql更新语句.
 * Log sequence number（LSN1）：当前系统LSN最大值，新的事务日志LSN将在此基础上生成（LSN1+新日志的大小）；
 * <p>
 * Log flushed up to（LSN2）：当前已经写入日志文件的LSN；
 * <p>
 * Oldest modified data log（LSN3）：当前最旧的脏页数据对应的LSN，写Checkpoint的时候直接将此LSN写入到日志文件；
 * <p>
 * Last checkpoint at（LSN4）：当前已经写入Checkpoint的LSN；
 */
public class SqlUpdateLog implements Comparable<SqlUpdateLog>,Mlogger,Serializable{
    public long LSN;
    public String sql;
    public String db;

    public SqlUpdateLog(long LSN, String sql,String db) {
        this.LSN = LSN;
        this.sql = sql;
        this.db = db;
    }
    @Override
    public int compareTo(SqlUpdateLog o) {
        return ComparisonChain.start().compare(LSN, o.LSN).result();
    }
    @Override
    public boolean equals(Object obj) {
        SqlUpdateLog updateLog = (SqlUpdateLog) obj;
        return Objects.equal(obj != null, true)
                && Objects.equal(updateLog.LSN, LSN)
                && Objects.equal(updateLog.sql, sql)
                && java.util.Objects.equals(updateLog.db, db);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).
                add("LSN", LSN).add("sql", sql).add("db",db).
                toString();
    }

    public static void main(String[] args) {
        SqlUpdateLog sqlUpdateLog = new SqlUpdateLog(1, "ddd","db");
        System.out.println(sqlUpdateLog.toString());


    }
}
