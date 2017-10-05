/*
 * Java-based distributed database like Mysql
 */
package io.jsql.mysql.handler

import io.jsql.audit.LoginLog
import io.jsql.audit.sendesServer
import io.jsql.config.ErrorCode
import io.jsql.my_config.MyConfig
import io.jsql.mysql.CharsetUtil
import io.jsql.mysql.mysql.AuthPacket
import io.jsql.mysql.mysql.MySQLPacket
import io.jsql.sql.OConnection
import io.jsql.wireshark.Wireshck
import io.netty.buffer.Unpooled
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * 前端认证处理器
 * 处理auth包
 */
@Component
open class MysqlAuthHander : MysqlPacketHander {

    @Autowired
    lateinit var config: MyConfig

    private fun handle0(auth: AuthPacket, source: OConnection) {
        // check quit packet
        //        if (data.length == QuitPacket.QUIT.length && data[4] == MySQLPacket.COM_QUIT) {
        ////            source.close("quit packet");
        //            return;
        //        }
        source.schema = auth.database

        // check user
        //        if (!checkUser(auth.user, source.getHost())) {
        //            failure(ErrorCode.ER_ACCESS_DENIED_ERROR, "Access denied for user '" + auth.user + "' with host '" + source.getHost()+ "'");
        //            return;
        //        }

        // check password
        if (!checkPassword(auth.password!!, auth.user!!)) {
            if (config.audit) {

                LoginLog(auth.user ?: "null", source.host, false).sendesServer()
            }
            failure(ErrorCode.ER_ACCESS_DENIED_ERROR, "Access denied for user '" + auth.user + "', because password is error ", source)
        } else {
            success(auth, source)

        }

        // check degrade
        //        if ( isDegrade( auth.user ) ) {
        //        	 failure(ErrorCode.ER_ACCESS_DENIED_ERROR, "Access denied for user '" + auth.user + "', because service be degraded ");
        //             return;
        //        }

        //            DB.currentDB = auth.database;
        //        source.setSchema(auth.database);
        // check schema
        //        switch (checkSchema(auth.database, auth.user)) {
        //        case ErrorCode.ER_BAD_DB_ERROR:
        //            failure(ErrorCode.ER_BAD_DB_ERROR, "Unknown database '" + auth.database + "'");
        //            break;
        //        case ErrorCode.ER_DBACCESS_DENIED_ERROR:
        //            String s = "Access denied for user '" + auth.user + "' to database '" + auth.database + "'";
        //            failure(ErrorCode.ER_DBACCESS_DENIED_ERROR, s);
        //            break;
        //        default:
        //            success(auth);
        //        }
    }

    //TODO: add by zhuam
    //前端 connection 达到该用户设定的阀值后, 立马降级拒绝连接
    protected fun isDegrade(user: String): Boolean {

        //    	int benchmark = source.getPrivileges().getBenchmark(user);
        //    	if ( benchmark > 0 ) {
        //
        //	    	int forntedsLength = 0;
        //	    	NIOProcessor[] processors = MycatServer.getInstance().getProcessors();
        //			for (NIOProcessor p : processors) {
        //				forntedsLength += p.getForntedsLength();
        //			}
        //
        //			if ( forntedsLength >= benchmark ) {
        //				return true;
        //			}
        //    	}

        return false
    }

    //    protected boolean checkUser(String user, String host) {
    //        return
    //                source.getPrivileges().userExists(user, host);
    //    }

    private fun checkPassword(password: ByteArray, user: String): Boolean {
        //        String pass = source.getPrivileges().getPassword(user);

        // check null
        //        if (pass == null || pass.length() == 0) {
        //            if (password == null || password.length == 0) {
        //                return true;
        //            } else {
        //                return false;
        //            }
        //        }
        //        if (password == null || password.length == 0) {
        //            return false;
        //        }

        // encrypt
        //        byte[] encryptPass = null;
        //        try {
        ////            encryptPass = SecurityUtil.scramble411(pass.getBytes(), source.getSeed());
        //        } catch (NoSuchAlgorithmException e) {
        ////            LOGGER.warn(source.toString(), e);
        //            return false;
        //        }
        //        if (encryptPass != null && (encryptPass.length == password.length)) {
        //            int i = encryptPass.length;
        //            while (i-- != 0) {
        //                if (encryptPass[i] != password[i]) {
        //                    return false;
        //                }
        //            }
        //        } else {
        //            return false;
        //        }

        return true
    }

    //    protected int checkSchema(String schema, String user) {
    //        if (schema == null) {
    //            return 0;
    //        }
    ////        FrontendPrivileges privileges = source.getPrivileges();
    ////        if (!privileges.schemaExists(schema)) {
    ////            return ErrorCode.ER_BAD_DB_ERROR;
    ////        }
    ////        Set<String> schemas = privileges.getUserSchemas(user);
    ////        if (schemas == null || schemas.size() == 0 || schemas.contains(schema)) {
    ////            return 0;
    ////        } else {
    ////            return ErrorCode.ER_DBACCESS_DENIED_ERROR;
    ////        }
    //        return 0;
    //    }

    private fun success(auth: AuthPacket, source: OConnection) {

        source.authenticated = true
        source.user = auth.user
        source.schema = auth.database
        source.charsetIndex = auth.charsetIndex
        source.charset = CharsetUtil.getCharset(source.charsetIndex)
        if (config.audit) {
            LoginLog(source.user ?: "null", source.host, true).sendesServer()
        }
        if (LOGGER.isInfoEnabled) {
            val s = StringBuilder()
            s.append(source).append('\'').append(auth.user).append("' login success")
            val extra = auth.extra
            if (extra != null && extra.size > 0) {
                s.append(",extra:").append(String(extra))
            }
            LOGGER.info(s.toString())
        }

        //        ByteBuffer buffer = source.allocate();
        //        source.write(source.writeToBuffer(AUTH_OK, buffer));
//        source.write(Unpooled.wrappedBuffer(AUTH_OK))
        source.write(Unpooled.wrappedBuffer(Wireshck.authOK))
        //        boolean clientCompress = Capabilities.CLIENT_COMPRESS==(Capabilities.CLIENT_COMPRESS & auth.clientFlags);
        //        boolean usingCompress = false;
        //        if(clientCompress&&usingCompress)
        //        {
        ////            source.setSupportCompress(true);
        //        }
    }

    private fun failure(errno: Int, info: String, source: OConnection) {
        LOGGER.error(source.toString() + info)
        source.writeErrMessage(2.toByte(), errno, info)
    }

    override fun hander(mySQLPacket: MySQLPacket, source: OConnection) {
        println(mySQLPacket.toString())
        handle0(mySQLPacket as AuthPacket, source)
    }

    companion object {

        private val LOGGER = LoggerFactory.getLogger(MysqlAuthHander::class.java)
        private val AUTH_OK = byteArrayOf(7, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0)
    }
}