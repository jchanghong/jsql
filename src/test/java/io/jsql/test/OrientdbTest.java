package io.jsql.test;

import com.orientechnologies.orient.core.db.OrientDB;
import com.orientechnologies.orient.core.db.OrientDBConfig;
import com.orientechnologies.orient.core.db.document.ODatabaseDocument;
import com.orientechnologies.orient.core.record.OElement;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.autoconfigure.web.ServerProperties;

import static org.junit.Assert.*;

/**
 * Created by 长宏 on 2017/5/3 0003.
 */
public class OrientdbTest {
    @Before
    public void setUp() throws Exception {
        assertEquals(1,1);
    }

    @Test
    public void test1() throws Exception {
        assertEquals(1,1);
    }

    @Test
    public void test2() throws Exception {
        OrientDB orientDB = new OrientDB("embedded:./databases", null, null, OrientDBConfig.defaultConfig());
        ODatabaseDocument document = orientDB.open("db1", "admin", "admin");
        assertEquals(document.getName(),"db1");
    }

    @After
    public void tearDown() throws Exception {
    }

}