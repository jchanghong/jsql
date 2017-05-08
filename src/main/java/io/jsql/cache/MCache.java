package io.jsql.cache;

import com.google.common.base.MoreObjects;
import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.Configuration;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.xml.XmlConfiguration;
import org.springframework.stereotype.Component;

import java.net.URL;

/**
 * Created by 长宏 on 2017/5/8 0008.
 */
@Component
public class MCache {
    public CacheManager cacheManager;
    public MCache() {
        URL myUrl = getClass().getResource("/ehcache1.xml");
        Configuration xmlConfig = new XmlConfiguration(myUrl);
         cacheManager = CacheManagerBuilder.newCacheManager(xmlConfig);
        cacheManager.init();
    }

    public static void main(String[] args) {
        MCache mCache = new MCache();
        Cache<String, String> cache = mCache.cacheManager.getCache("b", String.class, String.class);
        cache.put("d", "ddd");
        for (int i = 0; i < 100; i++) {
            cache.put(i + "", "string");
        }
        cache.forEach(a -> System.out.println(a.getKey() +":"+ a.getValue()));
        mCache.cacheManager.close();
    }
}
