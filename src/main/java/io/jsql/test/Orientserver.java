//package io.jsql.test;
//
//import com.orientechnologies.common.parser.OSystemVariableResolver;
//import com.orientechnologies.orient.server.OServer;
//import com.orientechnologies.orient.server.OServerMain;
//
///**
// * Created by 长宏 on 2017/5/1 0001.
// */
//public class Orientserver {
////    public static Logger logger = LoggerFactory.getLogger(Orientserver.class.getSimpleName());
//    public static void main(String[] args) {
//        try {
//            OSystemVariableResolver.setEnv("ORIENTDB_HOME", ".");
//            OSystemVariableResolver.setEnv("orientdb.www.path", "src/site");
//            OSystemVariableResolver.setEnv("java.util.logging.config.file", "./config/orientdb-server-log.properties");
//            OSystemVariableResolver.setEnv("orientdb.config.file", "./config/orientdb-server-config.xml");
//            OSystemVariableResolver.setEnv("rhino.opt.level", "9");
//            OSystemVariableResolver.setEnv("distributed", "true");
//
//            OServerMain.main(args);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}
