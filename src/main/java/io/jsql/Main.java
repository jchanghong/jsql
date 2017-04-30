package io.jsql;

import io.jsql.netty.MServer;

/**
 * Created by 长宏 on 2017/4/29 0029.
 */
public class Main {
    public static void main(String[] args) {

        try {
            MServer.main(args);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
