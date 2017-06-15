package io.jsql

import org.elasticsearch.bootstrap.Elasticsearch
import java.util.concurrent.CountDownLatch
import kotlin.concurrent.thread

/**
\* Created with IntelliJ IDEA.
\* User: jiang
\* Date: 2017/6/15 0015
\* Time: 17:51
启动elasticsearch服务器
\*/
fun main(args: Array<String>) {
    var down = CountDownLatch(1)
    thread (start = true){  Elasticsearch.main(args)}
    println("start.......")
    down.await()

}