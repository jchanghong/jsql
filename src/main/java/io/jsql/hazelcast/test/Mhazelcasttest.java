package io.jsql.hazelcast.test;

import io.jsql.hazelcast.MyHazelcast;

/**
 * Created by 长宏 on 2017/5/5 0005.
 */
public class Mhazelcasttest {
    public static void main(String[] args) {
        MyHazelcast mhazelcast = new MyHazelcast();
        mhazelcast.exeSql("create database db3","d");
    }
}
