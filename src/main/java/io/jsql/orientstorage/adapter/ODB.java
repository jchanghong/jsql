package io.jsql.orientstorage.adapter;

import com.orientechnologies.orient.core.db.ODatabaseType;
import com.orientechnologies.orient.core.db.OrientDB;
import com.orientechnologies.orient.core.db.OrientDBConfig;
import com.orientechnologies.orient.core.db.document.ODatabaseDocument;
import com.orientechnologies.orient.core.record.OElement;
import com.orientechnologies.orient.core.sql.executor.OResult;
import com.orientechnologies.orient.core.sql.executor.OResultSet;
import io.jsql.cache.MCache;
import io.jsql.orientstorage.MdatabasePool;
import io.jsql.sql.OConnection;
import io.jsql.storage.DB;
import io.jsql.storage.StorageException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

/**
 * Created by 长宏 on 2017/5/3 0003.
 */
@Service
public class ODB implements DB {
    private OrientDB orientDB;

    @Value("${db.dir}")
    private String dbDIR="databases";
    @Autowired
    private MdatabasePool pool;
 public    ODB() {
        StringBuilder builder = new StringBuilder("embedded:");
        builder.append("./");
        builder.append(dbDIR);
        builder.append("/");
        OrientDBConfig confi = OrientDBConfig.defaultConfig();
        orientDB = new OrientDB(builder.toString(), null, null, confi);
//     pool = new MdatabasePool(orientDB);

    }

    @PostConstruct
    void post() {
        OConnection.DB_ADMIN = this;
        pool.setOrientDB(orientDB);
    }

  private   ExecutorService executorService = Executors.newFixedThreadPool(2);
    public String getDbDIR() {
        return dbDIR;
    }

    public void setDbDIR(String dbDIR) {
        this.dbDIR = dbDIR;
    }

//    private Map<String, ODatabasePool> dbpoolmap = new HashMap<>();

    @Override
    public void deletedbSyn(String dbname) throws StorageException {
        if (!orientDB.exists(dbname)) {
            throw new StorageException("db not exits");
        }
        MCache.showdb().clear();
        orientDB.drop(dbname);
    }

    @Override
    public void deletedbAyn(String dbname) {

        executorService.execute(()->{
            try {
                deletedbSyn(dbname);
            } catch (StorageException e) {
                e.printStackTrace();
            }

        });
    }

    @Override
    public void createdbSyn(String dbname) throws StorageException {
        if (orientDB.exists(dbname)) {
            throw new StorageException("db exits");
        }
        MCache.showdb().clear();
        orientDB.create(dbname, ODatabaseType.PLOCAL);
    }

    @Override
    public void createdbAsyn(String dbname) {
        executorService.execute(()->{
            try {
                createdbSyn(dbname);
            } catch (StorageException e) {
                e.printStackTrace();
            }

        });
    }

    @Override
    public List<String> getallDBs() throws StorageException {
        return orientDB.list();
    }

    @Override
    public Optional<OResult> exesqlforResult(String sql, String dbname) throws StorageException {
        ODatabaseDocument document = getdb(dbname);
        document.activateOnCurrentThread();
        OResultSet command = document.command(sql, (Object[]) null);
        pool.close(document);
        return command.stream().findAny();
    }

    @Override
    public void exesqlNoResultAsyn(String sql, String dbname) {
        executorService.execute(()->{
            try {
                exesqlforResult(sql, dbname);
            } catch (StorageException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public Stream<OElement> query(String sqlquery, String dbname) throws StorageException {
        ODatabaseDocument document = getdb(dbname);
        document.activateOnCurrentThread();
     OResultSet resultSet= document.query(sqlquery, (Object[]) null);
        Stream<OElement> oElementStream = resultSet.stream().map(a -> a.toElement());
        pool.close(document);
        return oElementStream;

    }

    @Override
    public ODatabaseDocument getdb(String dbname) throws StorageException{
        if (!orientDB.exists(dbname)) {
            throw  new StorageException("db not exists");
        }
        return pool.get(dbname);
    }

    @Override
    public void close() {
        try {
            pool.close();
            orientDB.close();
            executorService.shutdown();
        } catch (Exception e) {
//            e.printStackTrace();
        }
    }

    @Override
    public void exe(String sql, String db) {

    }

    @Override
    public void close(ODatabaseDocument databaseDocument) {
        pool.close(databaseDocument);

    }
}
