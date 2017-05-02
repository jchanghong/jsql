package io.jsql;

import com.orientechnologies.common.parser.OSystemVariableResolver;
import com.orientechnologies.orient.server.OServer;
import com.orientechnologies.orient.server.OServerMain;
import io.jsql.my_config.MyProperties;
import io.jsql.netty.MServer;
import io.jsql.orientstorage.constant.Minformation_schama;
import io.jsql.storage.MException;
import org.springframework.boot.SpringApplication;

/**
 * Created by 长宏 on 2017/4/29 0029.
 * 启动类
 */
public class Main {
    public static void main(String[] args) {
        try {
            checkconfig();
        } catch (MException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        OServer instance = null;
        try {
            OSystemVariableResolver.setEnv("ORIENTDB_HOME", ".");
            OSystemVariableResolver.setEnv("orientdb.www.path", "src/site");
            OSystemVariableResolver.setEnv("java.util.logging.config.file", "./config/orientdb-server-log.properties");
            OSystemVariableResolver.setEnv("orientdb.config.file", "./config/orientdb-server-config.xml");
            OSystemVariableResolver.setEnv("rhino.opt.level", "9");
            OSystemVariableResolver.setEnv("distributed", "true");
            instance = OServerMain.create();
            instance.startup().activate();
//            OServerMain.main(args);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);

        }
        try {
            Minformation_schama.init_if_notexits();
            MServer.main(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
        instance.waitForShutdown();

    }
    private static void checkconfig() throws MException {
        if (!MyProperties.keys().contains("port")) {
            throw new MException("port");
        }

    }
}
