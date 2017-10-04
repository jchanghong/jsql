/*
 * Java-based distributed database like Mysql
 */

package io.jsql.orientstorage

import com.orientechnologies.orient.core.db.OrientDB
import com.orientechnologies.orient.core.db.OrientDBConfig
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration


@Configuration
@ComponentScan
class OrientdbConfig {
    @Value("\${db.dir}")
    val dbDIR = "databases"

    @Bean
    fun orientdb(): OrientDB {
        val builder = StringBuilder("embedded:")
        builder.append("./")
        builder.append(dbDIR)
        builder.append("/")
        val confi = OrientDBConfig.defaultConfig()
        return OrientDB(builder.toString(), null, null, confi)
    }
}
