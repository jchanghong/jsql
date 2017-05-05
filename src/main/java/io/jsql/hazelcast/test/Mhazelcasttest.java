package io.jsql.hazelcast.test;

import io.jsql.hazelcast.Mhazelcast;
import io.jsql.hazelcast.SqlUpdateLog;

/**
 * Created by 长宏 on 2017/5/5 0005.
 */
public class Mhazelcasttest {
    public static void main(String[] args) {
        Mhazelcast mhazelcast = new Mhazelcast();
        mhazelcast.sqlUpdateLogs.add(new SqlUpdateLog(1, "sql"));

        System.out.println(mhazelcast.sqlUpdateLogs.size());
    }
}
