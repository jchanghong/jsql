package io.jsql.test;

import com.orientechnologies.orient.core.db.OrientDB;
import com.orientechnologies.orient.core.db.OrientDBConfig;
import com.orientechnologies.orient.core.db.document.ODatabaseDocument;
import com.orientechnologies.orient.core.sql.executor.OResult;
import com.orientechnologies.orient.core.sql.executor.OResultSet;

import java.io.IOException;

/**
 * Created by 长宏 on 2017/5/1 0001.
 */
public class Orientdb {
    public static void main(String[] args) throws IOException {
//        String url = ODB.getRemoteUrl("db3");
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

        OrientDBConfig confi = OrientDBConfig.defaultConfig();
        OrientDB orientDb = new OrientDB("embedded:./databases/", null, null, confi);
//        orientDb.create("test",ODatabaseType.PLOCAL);
        ODatabaseDocument session = orientDb.open("test","admin","admin");
        System.out.println(session.isPooled());

//        OElement element = session.newElement("table");
//        element.setProperty("name", "changhong");
//        element.save();
        OResultSet set = session.query("select * from table", (Object[]) null);
        set.stream().forEach(a->{
            System.out.println(a.getMetadata("name"));
            System.out.println(a.getElement().get().getProperty("name")+"");
        });
//        OClass customer = session.createClass("Customer");
//        customer.createProperty("name", OType.STRING);

//        OClass invoice = session.createClass("Invoice");
//        invoice.createProperty("id", OType.INTEGER);
//        invoice.createProperty("date", OType.DATE);
//        invoice.createProperty("customer", OType.LINK, customer);
//        OElement c = session.newElement("Customer");
//        OElement i = session.newElement("Invoice");
//        i.setProperty("customer", c);
//        c.save();
//        i.save();
        OResultSet resultSet = session.command("insert into table(id) values(1);");
        OResult next = resultSet.next();
        System.out.println(next.getClass().getName());
        session.close();
        orientDb.close();



    }
}
