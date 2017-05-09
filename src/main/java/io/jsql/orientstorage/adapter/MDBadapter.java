//package io.jsql.orientstorage.adapter;
//
//import com.orientechnologies.orient.core.db.OPartitionedDatabasePool;
//import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
//import com.orientechnologies.orient.core.record.impl.ODocument;
//import com.orientechnologies.orient.core.sql.OCommandSQL;
//import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;
//import io.jsql.storage.MException;
//
//import java.io.File;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//
///**
// * Created by 长宏 on 2017/3/20 0020.
// */
//public class MDBadapter {
//    public static final ExecutorService executor;
//    public static final Set<String> dbset = new HashSet<>();
//    /**
//     * The constant DB_DIR.
//     */
//    private static final String DB_DIR = "databases";
//    /**
//     * The Hash map.
//     */
//    private static final ConcurrentHashMap<String, OPartitionedDatabasePool> hashMap = new ConcurrentHashMap<>();
//    /**
//     * The constant currentDB.
//     */
//    public static String currentDB;
//
//    static {
//        executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
//        loaddbset();
//    }
//
//    public static ODatabaseDocumentTx getCurrentDB(String dbname) {
//        if (!dbset.contains(dbname)) {
//            return null;
//        }
//        return getdbpool(dbname).acquire();
//    }
//
//    public static ODatabaseDocumentTx getCurrentDB() {
//        if (currentDB == null) {
//            return null;
//        }
//        if (!dbset.contains(currentDB)) {
//            return null;
//        }
//        return getdbpool(currentDB).acquire();
//    }
//
//    /**
//     * Deletedb boolean.
//     *
//     * @param dbname the dbname
//     * @return the boolean
//     */
//    synchronized public static void deletedb(String dbname) throws MException {
//        if (dbset.contains(dbname)) {
//            dbset.remove(dbname);
//            executor.execute(() -> {
//                OPartitionedDatabasePool oDatabaseDocumentTx = getdbpool(dbname);
//                ODatabaseDocumentTx documentTx = oDatabaseDocumentTx.acquire();
//                documentTx.drop();
//                getdbpool(dbname).close();
//                hashMap.remove(dbname);
//            });
//        } else {
//            throw new MException("db 不存在");
//        }
//    }
//
//    /**
//     * Createdb boolean.
//     *
//     * @param dbname the dbname
//     * @return the boolean
//     */
//    static synchronized public void createdb(String dbname) throws MException {
//        if (dbset.contains(dbname)) {
//            throw new MException("db已经存在");
//        }
//        dbset.add(dbname);
//        executor.execute(() -> {
//            new ODatabaseDocumentTx(getlccalurl(dbname)).create().close();
//            OPartitionedDatabasePool pool = new OPartitionedDatabasePool(getlccalurl(dbname), "admin", "admin");
//            hashMap.put(dbname, pool);
//        });
//    }
//
//    /**
//     * Createdb boolean.
//     * 同步
//     *
//     * @param dbname the dbname
//     * @return the boolean
//     */
//    static synchronized public void createdbsyn(String dbname) throws MException {
//        if (dbset.contains(dbname)) {
//            throw new MException("db已经存在");
//        }
//        dbset.add(dbname);
//        new ODatabaseDocumentTx(getlccalurl(dbname)).create().close();
//        OPartitionedDatabasePool pool = new OPartitionedDatabasePool(getlccalurl(dbname), "admin", "admin");
//        hashMap.put(dbname, pool);
//    }
//
//    /**
//     * Gets .
//     *
//     * @return the
//     */
//    private static Set<String> loaddbset() {
//        dbset.clear();
//        File file = new File(DB_DIR);
//        if (!file.exists()) {
//            file.mkdir();
//            return dbset;
//        }
//        if (file.isFile()) {
////            file.delete();
//            file.mkdir();
//            return dbset;
//        }
//        for (File file1 : file.listFiles()) {
//            if (file1.isDirectory()) {
//                dbset.add(file1.getName());
//            }
//        }
//        return dbset;
//    }
//
//    public static OPartitionedDatabasePool getdbpool(String dbname) {
//
//        if (hashMap.containsKey(dbname)) {
//            return hashMap.get(dbname);
//        }
//        OPartitionedDatabasePool pool = new OPartitionedDatabasePool(getlccalurl(dbname), "admin", "admin");
//        hashMap.put(dbname, pool);
//        return pool;
//    }
//
//    /**
//     * Gets .
//     *
//     * @param petshop the petshop
//     * @return the
//     */
//    public static String getmemoryurl(String petshop) {
//        return "memory:" + DB_DIR + "/" + petshop;
//    }
//
//    /**
//     * Gets .
//     *
//     * @param petshop the petshop
//     * @return the
//     */
//    public static String getlccalurl(String petshop) {
//        return "plocal:" + DB_DIR + "/" + petshop;
//    }
//
//    /**
//     * Gets .
//     *
//     * @param url the url
//     * @return the
//     */
//    public static String getdbnamefromurl(String url) {
//        int index = url.lastIndexOf("/");
//        return url.substring(index + 1);
//    }
//
//    /**
//     * Gets .
//     *
//     * @param dbname the dbname
//     * @return the
//     */
//    public static String getfilepath(String dbname) {
//        return DB_DIR + "/" + dbname;
//    }
//
//    /**
//     * Exesql string.
//     *
//     * @param sql the sql 不是select语句或者show语句 ,showdb dddd
//     * @return the string
//     * @throws MException the orient exception
//     */
//    public static Object exesql(String sql, String dbname) throws MException {
//        ODatabaseDocumentTx documentTx = null;
//        if (dbname == null) {
//            throw new MException("db is null");
//        }
//        try {
//            documentTx = getdbpool(dbname).acquire();
//            documentTx.activateOnCurrentThread();
//            return documentTx.command(new OCommandSQL(sql)).execute();
//        } catch (Exception e) {
//            throw new MException(e.getMessage());
//        } finally {
//            if (documentTx != null && !documentTx.isClosed()) {
//                documentTx.close();
//            }
//        }
//
//    }
//
//    /**
//     * Exequery list.
//     *
//     * @param sqlquery the sqlquery
//     * @return the list
//     * @throws MException the orient exception
//     */
//    public static List<ODocument> exequery(String sqlquery, String dbname) throws MException {
//        ODatabaseDocumentTx documentTx = null;
//        if (dbname == null) {
//            throw new MException("db is null");
//        }
//        try {
//            documentTx = getdbpool(dbname).acquire();
//            documentTx.activateOnCurrentThread();
//            return documentTx.query(
//                    new OSQLSynchQuery<ODocument>(
//                            sqlquery));
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new MException(e.getMessage());
//        } finally {
//            if (documentTx != null && !documentTx.isClosed()) {
//                documentTx.close();
//            }
//        }
//    }
//}
