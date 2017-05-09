package io.jsql.cache;

import io.jsql.sql.handler.MyResultSet;
import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.Configuration;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.xml.XmlConfiguration;

import java.net.URL;

/**
 * Created by 长宏 on 2017/5/8 0008.
 */
public class MCache {
    public static CacheManager cacheManager;
    static {
        URL myUrl = MCache.class.getResource("/ehcache1.xml");
        Configuration xmlConfig = new XmlConfiguration(myUrl);
        cacheManager = CacheManagerBuilder.newCacheManager(xmlConfig);
        cacheManager.init();
    }
    private  MCache() {
    }

  static   public Cache<String, MyResultSet> showdb() {
        Cache<String, MyResultSet> cache = cacheManager.getCache("showdb", String.class, MyResultSet.class);
        return cache;
    }
}
