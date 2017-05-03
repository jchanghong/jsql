package io.jsql.orientstorage.adapter;

import com.alibaba.druid.sql.ast.statement.SQLColumnDefinition;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlCreateTableStatement;
import com.orientechnologies.orient.core.db.document.ODatabaseDocument;
import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.orientechnologies.orient.core.metadata.schema.OSchema;
import com.orientechnologies.orient.core.metadata.schema.OType;
import com.orientechnologies.orient.core.record.OElement;
import io.jsql.orientstorage.sqlhander.sqlutil.MSQLutil;
import io.jsql.sql.OConnection;
import io.jsql.storage.DB;
import io.jsql.storage.StorageException;
import io.jsql.storage.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Stream;

/**
 * Created by 长宏 on 2017/5/3 0003.
 */
@Service
public class OTable implements Table{
   public OTable() {
    }
    @Autowired
   private DB dbadmin;

   @PostConstruct
    void post() {
       OConnection.TABLE_ADMIN = this;
    }
    @Override
    public void droptableSyn(String dbname, String table) throws StorageException {
        ODatabaseDocument document = dbadmin.getdb(dbname);
        document.activateOnCurrentThread();
        OSchema schema = document.getMetadata().getSchema();
        if (!schema.existsClass(table)) {
            throw new StorageException("table exist");
        }
        schema.dropClass(table);
        document.close();
    }

    @Override
    public void createtableSyn(String dbname, MySqlCreateTableStatement createTableStatement) throws StorageException {

                if (!dbadmin.getallDBs().contains(dbname)) {
            throw new StorageException("db不存在");
        }
        ODatabaseDocument db = dbadmin.getdb(dbname);
        db.activateOnCurrentThread();
        OSchema oSchema = db.getMetadata().getSchema();
        String table = createTableStatement.getTableSource().toString();
        if (oSchema.existsClass(table)) {
            dbadmin.close();
            throw new StorageException("table已经存在");
        }
                OClass oClass = db
                        .createClass(table);
                oClass.setStrictMode(true);
                List<String> dstring = new ArrayList<>();
                createTableStatement.getTableElementList().forEach(a -> {
                    if (a instanceof SQLColumnDefinition) {
                        SQLColumnDefinition sqlColumnDefinition = (SQLColumnDefinition) a;
                        if (sqlColumnDefinition.getConstraints() != null) {
                            dstring.add(((SQLColumnDefinition) a).getName().toString());
                        }
                    }
                });
                Map<String, String> maps = MSQLutil.gettablenamefileds(createTableStatement);
                maps.entrySet().forEach(e -> {
                    if (e.getValue().toLowerCase().contains("int")) {
                        oClass.createProperty(e.getKey(), OType.INTEGER);
                        if (dstring.contains(e.getKey())) {
                            oClass.getProperty(e.getKey()).createIndex(OClass.INDEX_TYPE.NOTUNIQUE);
                        }
                    } else if (e.getValue().toLowerCase().contains("varchar")) {
                        oClass.createProperty(e.getKey(), OType.STRING);
                        if (dstring.contains(e.getKey())) {
                            oClass.getProperty(e.getKey()).createIndex(OClass.INDEX_TYPE.NOTUNIQUE);
                        }
                    } else if (e.getValue().toLowerCase().contains("datatime")) {
                        oClass.createProperty(e.getKey(), OType.DATETIME);
                        if (dstring.contains(e.getKey())) {
                            oClass.getProperty(e.getKey()).createIndex(OClass.INDEX_TYPE.NOTUNIQUE);
                        }
                    } else if (e.getValue().toLowerCase().contains("times")) {
                        oClass.createProperty(e.getKey(), OType.DATETIME);
                        if (dstring.contains(e.getKey())) {
                            oClass.getProperty(e.getKey()).createIndex(OClass.INDEX_TYPE.NOTUNIQUE);
                        }
                    } else {
                        oClass.createProperty(e.getKey(), OType.STRING);
                        if (dstring.contains(e.getKey())) {
                            oClass.getProperty(e.getKey()).createIndex(OClass.INDEX_TYPE.NOTUNIQUE);
                        }
                    }
                });
        dbadmin.close();
    }

    @Override
    public List<String> getalltable(String dbname) throws StorageException {
        ODatabaseDocument document = dbadmin.getdb(dbname);
        List<String> strings = new ArrayList<>();
        document.getMetadata().getSchema().getClasses().forEach(a -> strings.add(a.getName()));
        document.close();
        return strings;
    }

    @Override
    public OClass gettableclass(String tablename, String db) throws StorageException {
        ODatabaseDocument document = dbadmin.getdb(db);
        boolean b = document.getMetadata().getSchema().existsClass(tablename);
        if (!b) {
            throw new StorageException("not exits");
        }
       return document.getClass(tablename);
    }

    @Override
    public Stream<OElement> selectSyn(OClass oClass,String dbname) throws StorageException {
        return dbadmin.query("select * from " + oClass.getName(), dbname);
    }
}
