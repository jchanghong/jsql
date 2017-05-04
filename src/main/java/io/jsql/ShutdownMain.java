package io.jsql;

import io.jsql.shutdown.Shutdown;

/**
 * Created by 长宏 on 2017/5/4 0004.
 * 关闭本机服务器
 */
public class ShutdownMain {
    public static void main(String[] args) {
        try {
            Shutdown.main1(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
