/*
 * Java-based distributed database like Mysql
 */

package io.jsql.sql

import io.jsql.audit.LoginLog
import io.jsql.config.Capabilities
import io.jsql.config.ErrorCode
import io.jsql.config.Versions
import io.jsql.mysql.CharsetUtil
import io.jsql.mysql.handler.MysqlAuthHander
import io.jsql.mysql.handler.MysqlCommandHandler
import io.jsql.mysql.handler.MysqlPacketHander
import io.jsql.mysql.handler.SQLHander
import io.jsql.mysql.mysql.*
import io.jsql.storage.DB
import io.jsql.storage.Table
import io.jsql.util.RandomUtil
import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import io.netty.channel.ChannelHandlerContext
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component
import java.io.UnsupportedEncodingException
import javax.annotation.PostConstruct
import javax.annotation.PreDestroy

/**
 * The type O connection.

 * @author changhong.前端连接
 */
@Component
@Qualifier("OConnection")
@Scope("prototype")
open class OConnection {
    val txInterrupted: Boolean
    @Autowired
  lateinit  var authhander: MysqlAuthHander
    @Autowired
   lateinit var comhander: MysqlCommandHandler
    @Autowired
   lateinit var sqlHander: SQLHander
    @Volatile var charsetIndex: Int = 0
    @Volatile var txIsolation: Int = 0
    @Volatile var autocommit: Boolean = false
    @Volatile var txInterrputMsg = ""
    var lastInsertId: Long = 0
    /**
     * 标志是否执行了lock tables语句，并处于lock状态
     */
    @Volatile var isLocked = false
    var charset = "utf8"
    var schema: String? = null
    var channelHandlerContext: ChannelHandlerContext? = null
    var seed: ByteArray?=null
    var authenticated: Boolean = false
    var user: String? = null
    var id: Long=++OConnection.connectionId

    var host = "127.0.0.1"


    @PostConstruct
    internal open fun init() {
        LOGGER.info("postconstruct:"+this.toString())
    }

    @PreDestroy
    internal fun destroy() {
        LOGGER.info("predestroy:"+this.toString())
    }

    init {
        this.txInterrupted = false
        this.autocommit = true
    }

    fun setCharset(charset: String?): Boolean {
        var charset = charset

        // 修复PHP字符集设置错误, 如： set names 'utf8'
        if (charset != null) {
            charset = charset.replace("'", "")
        }

        val ci = CharsetUtil.getIndex(charset)
        if (ci > 0) {
            this.charset = if (charset!!.equals("utf8mb4", ignoreCase = true)) "utf8" else charset
            this.charsetIndex = ci
            return true
        } else {
            return false
        }
    }

    open fun writeNotSurrport() {
        writeErrMessage(ErrorCode.ER_NOT_SUPPORTED_YET, "暂时不支持！！！")
    }

    open fun writeErrMessage(errno: Int, msg: String) {
        writeErrMessage(1.toByte(), errno, msg)
    }

    open fun writeErrMessage(id: Byte, errno: Int, msg: String) {
        val err = ErrorPacket()
        err.packetId = id
        err.errno = errno
        err.message = encodeString(msg, charset)
        err.write(channelHandlerContext!!.channel())
    }


    //    /**
    //     * 回滚事务
    //     */
    //    public void rollback() {
    //        // 状态检查
    //        if (txInterrupted) {
    //            txInterrupted = false;
    //        }
    //
    //        // 执行回滚
    ////        session.rollback();
    //    }

    //    /**
    //     * 执行lock tables语句方法
    //     *
    //     * @param sql
    //     */
    //    public void lockTable(String sql) {
    //        // 事务中不允许执行lock table语句
    //        if (!autocommit) {
    //            writeErrMessage(ErrorCode.ER_YES, "can't lock table in transaction!");
    //            return;
    //        }
    //        // 已经执行了lock table且未执行unlock table之前的连接不能再次执行lock table命令
    //        if (isLocked) {
    //            writeErrMessage(ErrorCode.ER_YES, "can't lock multi-table");
    //            return;
    //        }
    ////        RouteResultset rrs = routeSQL(sql, ServerParse.LOCK);
    ////        if (rrs != null) {
    ////            session.lockTable(rrs);
    ////        }
    //    }

    //    /**
    //     * 执行unlock tables语句方法
    //     *
    //     * @param sql
    //     */
    //    public void unLockTable(String sql) {
    //        sql = sql.replaceAll("\n", " ").replaceAll("\t", " ");
    //        String[] words = SplitUtil.split(sql, ' ', true);
    //        if (words.length == 2 && ("table".equalsIgnoreCase(words[1]) || "tables".equalsIgnoreCase(words[1]))) {
    //            isLocked = false;
    ////            session.unLockTable(sql);
    //        } else {
    //            writeErrMessage(ErrorCode.ER_UNKNOWN_COM_ERROR, "Unknown command");
    //        }
    //
    //    }

    open fun close(reason: String) {
        //        super.close(reason);
        //        if (getLoadDataInfileHandler() != null) {
        //            getLoadDataInfileHandler().clear();
        //        }
        if (channelHandlerContext!!.channel().isActive) {
            val channelFuture = channelHandlerContext!!.writeAndFlush(Unpooled.wrappedBuffer(reason.toByteArray()))
            channelFuture.addListener { channelHandlerContext!!.close() }

        }
    }

    override fun toString(): String {
        return "OConnection [id=$id, schema=$schema, user=$user,txIsolation=$txIsolation, autocommit=$autocommit, schema=$schema]"
    }

    open fun writeok() {
        write(Unpooled.wrappedBuffer(OkPacket.OK))
    }

    open fun writeErrMessage(message: String) {
        writeErrMessage(ErrorCode.ER_BAD_DB_ERROR, message)
    }

    open fun register() {
        if (channelHandlerContext != null) {
            // 生成认证数据
            val rand1 = RandomUtil.randomBytes(8)
            val rand2 = RandomUtil.randomBytes(12)

            // 保存认证数据
            val seed = ByteArray(rand1.size + rand2.size)
            System.arraycopy(rand1, 0, seed, 0, rand1.size)
            System.arraycopy(rand2, 0, seed, rand1.size, rand2.size)
            this.seed = seed

            // 发送握手数据包
            val useHandshakeV10 = true
            if (useHandshakeV10) {
                val hs = HandshakeV10Packet()
                hs.packetId = 0
                hs.protocolVersion = Versions.PROTOCOL_VERSION
                hs.serverVersion = Versions.SERVER_VERSION
                hs.threadId = 0
                hs.seed = rand1
                hs.serverCapabilities = serverCapabilities
                hs.serverCharsetIndex = (charsetIndex and 0xff).toByte()
                hs.serverStatus = 2
                hs.restOfScrambleBuff = rand2
                hs.write(channelHandlerContext!!.channel())
            } else {
                val hs = HandshakePacket()
                hs.packetId = 0
                hs.protocolVersion = Versions.PROTOCOL_VERSION
                hs.serverVersion = Versions.SERVER_VERSION
                hs.threadId = 0
                hs.seed = rand1
                hs.serverCapabilities = serverCapabilities
                hs.serverCharsetIndex = (charsetIndex and 0xff).toByte()
                hs.serverStatus = 2
                hs.restOfScrambleBuff = rand2
                hs.write(channelHandlerContext!!.channel())
            }
        }
    }

    open fun allocate(): ByteBuf {
        return Unpooled.buffer(256)
    }

    private // flag |= Capabilities.CLIENT_NO_SCHEMA;
            // flag |= Capabilities.CLIENT_SSL;
            // flag |= ServerDefs.CLIENT_RESERVED;
    val serverCapabilities: Int
        get() {
            var flag = 0
            flag = flag or Capabilities.CLIENT_LONG_PASSWORD
            flag = flag or Capabilities.CLIENT_FOUND_ROWS
            flag = flag or Capabilities.CLIENT_LONG_FLAG
            flag = flag or Capabilities.CLIENT_CONNECT_WITH_DB
            val usingCompress = false
            if (usingCompress) {
                flag = flag or Capabilities.CLIENT_COMPRESS
            }

            flag = flag or Capabilities.CLIENT_ODBC
            flag = flag or Capabilities.CLIENT_LOCAL_FILES
            flag = flag or Capabilities.CLIENT_IGNORE_SPACE
            flag = flag or Capabilities.CLIENT_PROTOCOL_41
            flag = flag or Capabilities.CLIENT_INTERACTIVE
            flag = flag or Capabilities.CLIENT_IGNORE_SIGPIPE
            flag = flag or Capabilities.CLIENT_TRANSACTIONS
            flag = flag or Capabilities.CLIENT_SECURE_CONNECTION
            flag = flag or Capabilities.CLIENT_MULTI_STATEMENTS
            flag = flag or Capabilities.CLIENT_MULTI_RESULTS
            val useHandshakeV10 = true
            if (useHandshakeV10) {
                flag = flag or Capabilities.CLIENT_PLUGIN_AUTH
            }
            return flag
        }

    open fun handerAuth(authPacket: AuthPacket) {
        authhander.hander(authPacket,this )
    }

    open fun handerCommand(commandPacket: CommandPacket) {
        comhander.hander(commandPacket,this )
    }

    open fun write(byteBuf: ByteBuf) {
        channelHandlerContext!!.writeAndFlush(byteBuf)
    }

    open fun writeResultSet(header: MySQLPacket, filed: Array<MySQLPacket>, eof1: MySQLPacket, rows: Array<MySQLPacket>, eof2: MySQLPacket) {

        val buf = allocate()
        header.write(buf)
        for (packet in filed) {
            packet.write(buf)
        }
        eof1.write(buf)
        for (row in rows) {
            row.write(buf)
        }
        eof2.write(buf)
        //        byte[] bytes = new byte[buf.readableBytes()];
        //        buf.readBytes(bytes);
        channelHandlerContext!!.channel().writeAndFlush(buf)
    }

    companion object {
      lateinit  var DB_ADMIN: DB
       lateinit var TABLE_ADMIN: Table
        var connectionId:Long=0
        private val LOGGER = LoggerFactory
                .getLogger(OConnection::class.java)

        private fun encodeString(src: String?, charset: String?): ByteArray? {
            if (src == null) {
                return null
            }
            if (charset == null) {
                return src.toByteArray()
            }
            try {
                return src.toByteArray(charset(charset))
            } catch (e: UnsupportedEncodingException) {
                return src.toByteArray()
            }

        }
    }
}
