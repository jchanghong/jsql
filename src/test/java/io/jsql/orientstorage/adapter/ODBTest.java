package io.jsql.orientstorage.adapter;

import io.jsql.storage.DB;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by 长宏 on 2017/5/3 0003.
 */
public class ODBTest {
    ODB db;
//    @Before
    public void setUp() throws Exception {
        db = new ODB();
        db.setDbDIR("databases");

    }

//    @After
    public void tearDown() throws Exception {
        db.close();
    }

    @Test
    public void deletedbSyn() throws Exception {
    }

    @Test
    public void deletedbAyn() throws Exception {
    }

//    @Test
    public void createdbSyn() throws Exception {
        db.createdbSyn("db2");
        assertEquals(db.getallDBs().size(),5);
    }

//    @Test
    public void createdbAsyn() throws Exception {
        List<String> strings = new ArrayList<>();
        Object dd = strings;
        List<String> strings1 = (List<String>) dd;
        System.out.println(strings1.getClass().getName());
    }

//    @Test
    public void getallDBs() throws Exception {
        assertEquals(db.getallDBs().size(),4);
    }

    @Test
    public void exesqlforResult() throws Exception {
    }

    @Test
    public void exesqlNoResultAsyn() throws Exception {
    }

    @Test
    public void query() throws Exception {
    }

//    @Test
    public void getdb() throws Exception {
      assertNotNull(db.getdb("db1"));
    }

    @Test
    public void close() throws Exception {
    }

}