package io.jsql.test;

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
    }

    @After
    public void tearDown() throws Exception {
    }

}