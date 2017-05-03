package io.jsql.orientstorage.constant;

import io.jsql.orientstorage.adapter.ODB;
import io.jsql.sql.OConnection;
import io.jsql.storage.StorageException;

/**
 * Created by 长宏 on 2017/3/23 0023.
 */
public class Minformation_schama {
    public static final String dbname = "information_schema";

    public static void init_if_notexits() {
        try {
            if (OConnection.DB_ADMIN.getallDBs().contains(dbname)) {
            } else {
                try {
                    OConnection.DB_ADMIN.createdbSyn(dbname);
                    MvariableTable.init_if_notexits();
                } catch (StorageException e) {
                    e.printStackTrace();
                }
            }
        } catch (StorageException e) {
            e.printStackTrace();
            System.exit(-1);

        }
    }
}
