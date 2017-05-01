package io.jsql.orientstorage.adapter2;

import com.alibaba.druid.sql.ast.statement.SQLColumnDefinition;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlCreateTableStatement;
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.orientechnologies.orient.core.metadata.schema.OSchema;
import com.orientechnologies.orient.core.metadata.schema.OType;
import com.orientechnologies.orient.core.record.impl.ODocument;
import io.jsql.orientstorage.sqlhander.sqlutil.MSQLutil;
import io.jsql.storage.DBAdmin;
import io.jsql.storage.MException;
import io.jsql.storage.TableAdmin;

import java.io.IOException;
import java.util.*;

/**
 * Created by 长宏 on 2017/3/20 0020.
 */
public class OrientTableAdmin implements TableAdmin{
    private static OrientTableAdmin me = new OrientTableAdmin();
    private DBAdmin dbAdmin;
  private   OrientTableAdmin() {
        try {
            dbAdmin = OrientDbAdmin.getInstance();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static OrientTableAdmin getInstance() {
        return me;
    }

    @Override
    public void droptableSyn(String dbname, String table) throws MException {
        if (!dbAdmin.getallDBs().contains(dbname)) {
            throw new MException("db不存在");
        }
        try (ODatabaseDocumentTx db = dbAdmin.getdb(dbname)) {
            db.activateOnCurrentThread();
            OSchema oSchema = db.getMetadata().getSchema();
            if (oSchema.existsClass(table)) {
                        db.getMetadata().getSchema().dropClass(table);
            } else {
                throw new MException("table 不存在");
            }
        } catch (Exception e) {
            throw new MException(e.getMessage());
        }
    }

    @Override
    public void createtableSyn(String dbname, MySqlCreateTableStatement createTableStatement) throws MException {
        if (!dbAdmin.getallDBs().contains(dbname)) {
            throw new MException("db不存在");
        }
        ODatabaseDocumentTx db = dbAdmin.getdb(dbname);
        db.activateOnCurrentThread();
        OSchema oSchema = db.getMetadata().getSchema();
        String table = createTableStatement.getTableSource().toString();
        if (oSchema.existsClass(table)) {
            db.close();
            throw new MException("table已经存在");
        }
                OClass oClass = db.getMetadata().getSchema()
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
        db.close();

    }

    @Override
    public Set<String> getalltable(String dbname) {
        ODatabaseDocumentTx db = dbAdmin.getdb(dbname);
        db.activateOnCurrentThread();
        OSchema oSchema = db.getMetadata().getSchema();
        Set<String> map = new HashSet<>();
        oSchema.getClasses().forEach(a->map.add(a.getName()));
        db.close();
        return map;
    }

    @Override
    public OClass gettableclass(String tablename, String dbname) {
        ODatabaseDocumentTx db = dbAdmin.getdb(dbname);
        db.activateOnCurrentThread();
        OSchema oSchema = db.getMetadata().getSchema();
       OClass oClass= oSchema.getClass(tablename);
        db.close();
        return oClass;
    }

    @Override
    public List<ODocument> selectSyn(OClass oClass) {
        try {
            return dbAdmin.exequery("select * from " + oClass.getName(), DBAdmin.currentDB);
        } catch (MException e) {
            e.printStackTrace();
        }
        return null;
    }


}
