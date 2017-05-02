package io.jsql.orientserver;

import com.orientechnologies.common.parser.OSystemVariableResolver;
import com.orientechnologies.orient.server.OServer;
import com.orientechnologies.orient.server.OServerMain;
import io.jsql.orientstorage.constant.Minformation_schama;
import org.springframework.stereotype.Service;

/**
 * Created by 长宏 on 2017/4/29 0029.
 * 启动orient server类
 */
@Service
public class OrientServer {

    public  void start() throws Exception{
        OServer instance = null;
            OSystemVariableResolver.setEnv("ORIENTDB_HOME", ".");
            OSystemVariableResolver.setEnv("orientdb.www.path", "src/site");
            OSystemVariableResolver.setEnv("java.util.logging.config.file", "./config/orientdb-server-log.properties");
            OSystemVariableResolver.setEnv("orientdb.config.file", "./config/orientdb-server-config.xml");
            OSystemVariableResolver.setEnv("rhino.opt.level", "9");
            OSystemVariableResolver.setEnv("distributed", "true");
            instance = OServerMain.create();
            instance.startup().activate();
            Minformation_schama.init_if_notexits();
    }
}
