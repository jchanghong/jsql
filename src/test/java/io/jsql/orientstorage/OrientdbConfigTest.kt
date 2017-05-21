package io.jsql.orientstorage

import com.orientechnologies.orient.core.db.OrientDB
import io.jsql.my_config.SpringProperty
import io.jsql.storage.DB
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import kotlin.test.assertEquals

@RunWith(SpringRunner::class)
@SpringBootTest(classes = arrayOf(OrientdbConfig::class,SpringProperty::class))
class OrientdbConfigTest {
    @Autowired
 lateinit   var springv:OrientDB
    @Autowired
    lateinit var path:OrientdbConfig
    @Autowired
    lateinit var dbs:MdatabasePool
    @Test
    fun getDbDIR() {
        println("hello")
        assertNotNull(springv)
        assertNotNull(path)
        assertNotNull(dbs)
        assertNotNull(dbs.orientDB)
        println(path.dbDIR)
        assertEquals(springv.list().size,2)
    }

    @Test
    fun orientdb() {
    }

}