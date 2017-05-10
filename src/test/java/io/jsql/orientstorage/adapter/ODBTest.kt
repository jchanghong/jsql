package io.jsql.orientstorage.adapter

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import java.util.*

/**
 * Created by 长宏 on 2017/5/3 0003.
 */
class ODBTest {
    internal var db: ODB?=null
    //    @Before
    @Throws(Exception::class)
    fun setUp() {
        db = ODB()
        db!!.dbDIR = "databases"

    }

    //    @After
    @Throws(Exception::class)
    fun tearDown() {
        db!!.close()
    }

    @Test
    @Throws(Exception::class)
    fun deletedbSyn() {
    }

    @Test
    @Throws(Exception::class)
    fun deletedbAyn() {
    }

    //    @Test
    @Throws(Exception::class)
    fun createdbSyn() {
        db!!.createdbSyn("db2")
        assertEquals(db!!.getallDBs().size.toLong(), 5)
    }

    //    @Test
    @Throws(Exception::class)
    fun createdbAsyn() {
        val strings = ArrayList<String>()
        val dd = strings
        println(dd.javaClass.name)
    }

    //    @Test
    @Throws(Exception::class)
    fun getallDBs() {
        assertEquals(db!!.getallDBs().size.toLong(), 4)
    }

    @Test
    @Throws(Exception::class)
    fun exesqlforResult() {
    }

    @Test
    @Throws(Exception::class)
    fun exesqlNoResultAsyn() {
    }

    @Test
    @Throws(Exception::class)
    fun query() {
    }

    //    @Test
    @Throws(Exception::class)
    fun getdb() {
        assertNotNull(db!!.getdb("db1"))
    }

    @Test
    @Throws(Exception::class)
    fun close() {
    }

}