/*
 * Java-based distributed database like Mysql
 */

//package io.jsql.orientstorage.adapter2;
//
//import com.orientechnologies.orient.client.remote.OServerAdmin;
//import com.orientechnologies.orient.core.db.OPartitionedDatabasePool;
//import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
//import com.orientechnologies.orient.core.record.impl.ODocument;
//import com.orientechnologies.orient.core.sql.OCommandSQL;
//import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;
//import io.jsql.storage.DB;
//import io.jsql.storage.StorageException;
//
//import java.io.IOException;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//
///**
// * Created by 长宏 on 2017/3/20 0020.
// */
//public class OrientDbAdmin extends DB{
//    private static final String user = "root";
//    private static final String password = "root";
//
//    private static OrientDbAdmin me;
//    private OServerAdmin oServerAdmin;
//    /**
//     * The Hash map.
//     */
//    private  final ConcurrentHashMap<String, OPartitionedDatabasePool> hashMap = new ConcurrentHashMap<>();
//    private   OPartitionedDatabasePool getdbpool(String dbname) {
//        if (hashMap.containsKey(dbname)) {
//            return hashMap.get(dbname);
//        }
//        OPartitionedDatabasePool pool = new OPartitionedDatabasePool(geturl(dbname), user, password);
//        hashMap.put(dbname, pool);
//        return pool;
//    }
//
//    private  OrientDbAdmin() throws IOException {
//            oServerAdmin = new OServerAdmin("remote:localhost");
//        oServerAdmin.connect(user, password);
//    }
//    public static OrientDbAdmin getInstance() throws StorageException {
//        if (me == null) {
//            synchronized (OrientDbAdmin.class) {
//                try {
//                    me = new OrientDbAdmin();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    throw new StorageException(e.getMessage());
//                }
//            }
//        }
//        return me;
//    }
//
//    public static final ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
//    public static void main(String[] args) throws IOException {
//        OServerAdmin admin = new OServerAdmin("remote:localhost");
//
//        admin.connect("root", "root");
//        admin.dropDatabase("db4", "plocal");
//        Map<String, String> map = admin.listDatabases();
//        System.out.println(map.size());
//
//    }
//
//
//    @Override
//    public void deletedbSyn(String dbname) throws StorageException {
//        try {
//            oServerAdmin.dropDatabase(dbname, "plocal");
//            hashMap.remove(dbname);
//        } catch (IOException e) {
//            e.printStackTrace();
//            throw new StorageException(e.getMessage());
//        }
//    }
//
//    @Override
//    public void deletedbAyn(String dbname) {
//        executor.execute(()->{
//            try {
//                deletedbSyn(dbname);
//            } catch (StorageException e) {
//                e.printStackTrace();
//            }
//        });
//
//    }
//
//    @Override
//    public void createdbSyn(String dbname) throws StorageException {
//        try {
//            oServerAdmin.createDatabase(dbname, "document", "plocal");
//            getdbpool(dbname);
//        } catch (IOException e) {
//            e.printStackTrace();
//            throw new StorageException(e.getMessage());
//        }
//
//    }
//
//    @Override
//    public void createdbAsyn(String dbname) {
//
//        executor.execute(()->{
//            try {
//                createdbSyn(dbname);
//            } catch (StorageException e) {
//                e.printStackTrace();
//            }
//
//        });
//    }
//
//    @Override
//    public Set<String> getallDBs() {
//        Map<String, String> map = null;
//        try {
//            map = oServerAdmin.listDatabases();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return map.keySet();
//    }
//
//    @Override
//    public Object exesqlforResult(String sql, String dbname) throws StorageException {
//        ODatabaseDocumentTx documentTx = null;
//        if (dbname == null) {
//            throw new StorageException("db is null");
//        }
//        try {
//            if (!oServerAdmin.existsDatabase(dbname, "plocal")) {
//                throw new StorageException("db not exist");
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//            throw new StorageException(e.getMessage());
//        }
//        try {
//            documentTx = getdbpool(dbname).acquire();
////            documentTx.activateOnCurrentThread();
//            Object execute = documentTx.command(new OCommandSQL(sql)).execute();
//            return execute;
//        } catch (Exception e) {
//            throw new StorageException(e.getMessage());
//        } finally {
//            if (documentTx != null && !documentTx.isClosed()) {
//                documentTx.close();
//            }
//        }
//    }
//
//    private String geturl(String dbname) {
//        return "remote:localhost/" + dbname;
//    }
//
//    @Override
//    public void exesqlNoResultAsyn(String sql, String dbname) {
//
//        executor.execute(()->{
//            try {
//                exesqlforResult(sql, dbname);
//            } catch (StorageException e) {
//                e.printStackTrace();
//            }
//        });
//    }
//    @Override
//    public  List<ODocument> query(String sqlquery, String dbname) throws StorageException {
//        ODatabaseDocumentTx documentTx = null;
//        if (dbname == null) {
//            throw new StorageException("db not select ");
//        }
//        try {
//            if (!oServerAdmin.existsDatabase(dbname, "plocal")) {
//                throw new StorageException("db not exist");
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//            throw new StorageException(e.getMessage());
//        }
//        try {
//            documentTx = getdbpool(dbname).acquire();
////            documentTx.activateOnCurrentThread();
//            return documentTx.query(
//                    new OSQLSynchQuery<ODocument>(
//                            sqlquery));
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new StorageException(e.getMessage());
//        } finally {
//            if (documentTx != null && !documentTx.isClosed()) {
//                documentTx.close();
//            }
//        }
//    }
//
//    @Override
//    public ODatabaseDocumentTx getdb(String dbname) {
//        ODatabaseDocumentTx databaseDocumentTx = getdbpool(dbname).acquire();
//        return databaseDocumentTx;
//    }
//}
