package io.jsql.orientstorage;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.orientechnologies.orient.core.db.OrientDB;
import com.orientechnologies.orient.core.db.document.ODatabaseDocument;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

/**
 * Created by 长宏 on 2017/5/4 0004.
 * 必须线程安全
 */
@Component
public class MdatabasePool {
    private OrientDB orientDB;
    private Multimap<String, ODatabaseDocument> multimap;

    public void setOrientDB(OrientDB orientDB) {
        this.orientDB = orientDB;
    }

    /**
     * Instantiates a new Mdatabase pool.
     *
     * @param orientDB the orient db
     */
    public MdatabasePool() {
        this.orientDB = orientDB;
        Multimap<String, ODatabaseDocument> tem = HashMultimap.create();
        multimap = Multimaps.synchronizedMultimap(tem);
    }

    /**
     * Get o database document.
     *
     * @param dbname the dbname数据库必须存在
     * @return the o database document
     */
    public ODatabaseDocument get(String dbname) {
        if (multimap.get(dbname).size() == 0) {
            ODatabaseDocument document = orientDB.open(dbname, "admin", "admin");
            return document;
        }
        else {
            ODatabaseDocument next = multimap.get(dbname).iterator().next();
            multimap.remove(dbname, next);
            return next;
        }
    }
    public void close() {
        multimap.values().forEach(a -> a.close());
    }
    public void close(ODatabaseDocument databaseDocument) {
        multimap.put(databaseDocument.getName(), databaseDocument);
    }

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     * @throws InterruptedException the interrupted exception
     */
    public static void main(String[] args) throws InterruptedException {
        HashMultimap<String, String> map1 = HashMultimap.create();
        Multimap<String, String> map = Multimaps.synchronizedMultimap(map1);
        CountDownLatch latch = new CountDownLatch(1);
        CountDownLatch latch1 = new CountDownLatch(2);
        new Thread(()->{
            try {
                latch.await();
                for (int i = 0; i < 1000; i++) {
                    map.put(i + "", i + "");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("1");
            latch1.countDown();
        }).start();
        new Thread(()->{
            try {
                latch.await();
                for (int i = 0; i < 1000; i++) {
                    map.put(i + "", i + "");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("2");
            latch1.countDown();
        }).start();
        latch.countDown();
        latch1.await();
        System.out.println("end"+map.keySet().size());
        map.get("ddd").add("hhh");
        System.out.println("end"+map.get("ddd").size());


    }
}
