/*
 * Java-based distributed database like Mysql
 */

package io.jsql.orientstorage

import com.google.common.collect.HashMultimap
import com.google.common.collect.Multimap
import com.google.common.collect.Multimaps
import com.orientechnologies.orient.core.db.OrientDB
import com.orientechnologies.orient.core.db.document.ODatabaseDocument
import org.springframework.stereotype.Component

import java.util.concurrent.CountDownLatch

/**
 * Created by 长宏 on 2017/5/4 0004.
 * 必须线程安全
 */
@Component
class MdatabasePool(var orientDB: OrientDB) {
    private val multimap: Multimap<String, ODatabaseDocument>
    /**
     * Instantiates a new Mdatabase pool.

     * @param orientDB the orient db
     */
    init {
        val tem = HashMultimap.create<String, ODatabaseDocument>()
        multimap = Multimaps.synchronizedMultimap(tem)
    }

    /**
     * Get o database document.

     * @param dbname the dbname数据库必须存在
     * *
     * @return the o database document
     */
    operator fun get(dbname: String): ODatabaseDocument {
        if (multimap.get(dbname).size == 0) {
            val document = orientDB!!.open(dbname, "admin", "admin")
            return document
        } else {
            val next = multimap.get(dbname).iterator().next()
            multimap.remove(dbname, next)
            return next
        }
    }

    fun close() {
        multimap.values().forEach { a -> a.close() }
    }

    fun close(databaseDocument: ODatabaseDocument) {
        multimap.put(databaseDocument.name, databaseDocument)
    }

    companion object {

        /**
         * The entry point of application.

         * @param args the input arguments
         * *
         * @throws InterruptedException the interrupted exception
         */
        @Throws(InterruptedException::class)
        @JvmStatic fun main(args: Array<String>) {
            val map1 = HashMultimap.create<String, String>()
            val map = Multimaps.synchronizedMultimap(map1)
            val latch = CountDownLatch(1)
            val latch1 = CountDownLatch(2)
            Thread {
                try {
                    latch.await()
                    for (i in 0..999) {
                        map.put(i.toString() + "", i.toString() + "")
                    }
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }

                println("1")
                latch1.countDown()
            }.start()
            Thread {
                try {
                    latch.await()
                    for (i in 0..999) {
                        map.put(i.toString() + "", i.toString() + "")
                    }
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }

                println("2")
                latch1.countDown()
            }.start()
            latch.countDown()
            latch1.await()
            println("end" + map.keySet().size)
            map.get("ddd").add("hhh")
            println("end" + map.get("ddd").size)


        }
    }
}
