package io.jsql.test;

import com.orientechnologies.orient.core.db.OPartitionedDatabasePool;
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import io.jsql.my_config.MyProperties;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by 长宏 on 2017/5/1 0001.
 */
public class Orientdb {
    public static void main(String[] args) throws IOException {
//        String url = MDBadapter.getRemoteUrl("db3");
//        System.out.println(url);
//        ODatabaseDocumentTx oDatabaseDocumentTx;
//        OServer server;
//        OServerAdmin admin = new OServerAdmin("remote:localhost");
//
//        admin.connect("root", "root");
//        admin.createDatabase("db4", "document", "plocal").close();
//        OPartitionedDatabasePool pool = new OPartitionedDatabasePool("remote:localhost/db7", "root", "root");
//        ODatabaseDocumentTx documentTx = pool.acquire();
//        System.out.println(documentTx.getMetadata().getSchema().countClasses());
//        documentTx.close();
//        pool.close();

        System.out.println(MyProperties.getInt("port"));



    }
}
