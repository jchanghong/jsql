package io.jsql;

import io.jsql.databaseorient.constant.Minformation_schama;
import io.jsql.netty.MServer;

/**
 * Created by 长宏 on 2017/4/29 0029.
 * 启动类
 */
public class Main {
    public static void main(String[] args) {

        try {
            Minformation_schama.init_if_notexits();
            MServer.main(args);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
