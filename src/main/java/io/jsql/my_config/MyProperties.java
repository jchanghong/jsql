package io.jsql.my_config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

/**
 * Created by 长宏 on 2017/5/1 0001.
 */
public class MyProperties {
    private static final String filename = "./config/config.properties";
    private static final Map<String, String> v = new HashMap<>();
    private static Properties properties;
    static {

        properties = new Properties();
        try {
            properties.load(new FileInputStream(filename));
            Enumeration enum1 = properties.propertyNames();//得到配置文件的名字
              while(enum1.hasMoreElements()) {
                     String strKey = (String) enum1.nextElement();
                         String strValue = properties.getProperty(strKey);
//                         System.out.println(strKey + "=" + strValue);
                  v.put(strKey, strValue);
                      }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public static Object get(String key) {
        return v.get(key);
    }
    public static int getInt(String key) {
        return Integer.parseInt(get(key).toString());
    }

    public static Set<String> keys() {
        return v.keySet();
    }
}
