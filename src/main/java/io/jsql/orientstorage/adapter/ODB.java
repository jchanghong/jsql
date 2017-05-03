package io.jsql.orientstorage.adapter;

import com.orientechnologies.orient.core.db.ODatabasePool;
import com.orientechnologies.orient.core.db.ODatabaseType;
import com.orientechnologies.orient.core.db.OrientDB;
import com.orientechnologies.orient.core.db.OrientDBConfig;
import com.orientechnologies.orient.core.db.document.ODatabaseDocument;
import com.orientechnologies.orient.core.record.OElement;
import com.orientechnologies.orient.core.sql.executor.OResult;
import com.orientechnologies.orient.core.sql.executor.OResultSet;
import io.jsql.sql.OConnection;
import io.jsql.storage.DB;
import io.jsql.storage.StorageException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
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
 public    ODB() {
        StringBuilder builder = new StringBuilder("embedded:");
        builder.append("./");
        builder.append(dbDIR);
        builder.append("/");
        OrientDBConfig confi = OrientDBConfig.defaultConfig();
        orientDB = new OrientDB(builder.toString(), null, null, confi);
    }

    @PostConstruct
    void post() {
        OConnection.DB_ADMIN = this;
    }

  private   ExecutorService executorService = Executors.newFixedThreadPool(2);
    public String getDbDIR() {
        return dbDIR;
    }

    public void setDbDIR(String dbDIR) {
        this.dbDIR = dbDIR;
    }

    private Map<String, ODatabasePool> dbpoolmap = new HashMap<>();

    @Override
    public void deletedbSyn(String dbname) throws StorageException {
        if (!orientDB.exists(dbname)) {
            throw new StorageException("db not exits");
        }
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
        document.close();
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
        document.close();
        return oElementStream;

    }

    @Override
    public ODatabaseDocument getdb(String dbname) throws StorageException{
        if (!orientDB.exists(dbname)) {
            throw  new StorageException("db not exists");
        }
        if (dbpoolmap.containsKey(dbname)) {
            return dbpoolmap.get(dbname).acquire();
        }
        else {
            ODatabasePool pool = new ODatabasePool(orientDB, dbname, "admin", "admin");
            dbpoolmap.put(dbname, pool);
            return pool.acquire();
        }
    }

    @Override
    public void close() {
        dbpoolmap.values().forEach(e -> e.close());
        orientDB.close();
        executorService.shutdown();
    }
}
