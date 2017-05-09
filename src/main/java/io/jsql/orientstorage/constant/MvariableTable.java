package io.jsql.orientstorage.constant;

import com.orientechnologies.orient.core.db.document.ODatabaseDocument;
import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.orientechnologies.orient.core.metadata.schema.OType;
import com.orientechnologies.orient.core.record.impl.ODocument;
import io.jsql.sql.OConnection;
import io.jsql.storage.StorageException;

/**
 * Created by 长宏 on 2017/3/23 0023.
 * Variable_name  value
 */
 class MvariableTable {
    public final static String tablename = "variables";
    public final static String tablenamestatus = "status";

    public static void init_if_notexits() {
        ODatabaseDocument documentTx = null;
        try {
            documentTx = OConnection.DB_ADMIN.getdb(Minformation_schama.dbname);
        } catch (StorageException e) {
            e.printStackTrace();
            System.exit(-1);

        }
        if (documentTx.getMetadata().getSchema().existsClass(tablename)) {
           OConnection.DB_ADMIN.close(documentTx);
        } else {
            OClass oClass = documentTx.getMetadata().getSchema().createClass(tablename);
            OClass oClass1 = documentTx.getMetadata().getSchema().createClass(tablenamestatus);
            oClass.setStrictMode(true);
            oClass.createProperty("Variable_name", OType.STRING);
            oClass.createProperty("value", OType.STRING);
            oClass1.setStrictMode(true);
            oClass1.createProperty("Variable_name", OType.STRING);
            oClass1.createProperty("value", OType.STRING);
            Mconstantvariables.MAP.entrySet().forEach(e -> {
                ODocument document = new ODocument(tablename);
                document.field("Variable_name", e.getKey());
                document.field("value", e.getValue());
                document.save();
            });
            MconstantStatusVariables.MAP.entrySet().forEach(e -> {
                ODocument document = new ODocument(tablenamestatus);
                document.field("Variable_name", e.getKey());
                document.field("value", e.getValue());
                document.save();
            });
            OConnection.DB_ADMIN.close(documentTx);
        }
    }
}
