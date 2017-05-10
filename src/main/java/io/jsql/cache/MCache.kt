package io.jsql.cache

import io.jsql.sql.handler.MyResultSet
import org.ehcache.Cache
import org.ehcache.CacheManager
import org.ehcache.config.Configuration
import org.ehcache.config.builders.CacheManagerBuilder
import org.ehcache.xml.XmlConfiguration

import java.net.URL

/**
 * Created by 长宏 on 2017/5/8 0008.
 */
object MCache {
    var cacheManager: CacheManager

    init {
        val myUrl = MCache::class.java.getResource("/ehcache1.xml")
        val xmlConfig = XmlConfiguration(myUrl)
        cacheManager = CacheManagerBuilder.newCacheManager(xmlConfig)
        cacheManager.init()
    }

    fun showdb(): Cache<String, MyResultSet> {
        val cache = cacheManager.getCache("showdb", String::class.java, MyResultSet::class.java)
        return cache
    }
}
