package io.jsql.orientstorage.constant;

import io.jsql.orientstorage.adapter.MDBadapter;
import io.jsql.storage.MException;

/**
 * Created by 长宏 on 2017/3/23 0023.
 */
public class Minformation_schama {
    public static final String dbname = "information_schema";

    public static void init_if_notexits() {
        if (MDBadapter.dbset.contains(dbname)) {
        } else {
            try {
                MDBadapter.createdbsyn(dbname);
                MvariableTable.init_if_notexits();
            } catch (MException e) {
                e.printStackTrace();
            }
        }
    }
}
