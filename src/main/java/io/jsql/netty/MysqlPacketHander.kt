/*
 * Java-based distributed database like Mysql
 */

package io.jsql.netty

import io.jsql.mysql.mysql.AuthPacket
import io.jsql.mysql.mysql.CommandPacket
import io.jsql.mysql.mysql.MySQLPacket
import io.jsql.sql.OConnection
import io.jsql.sql.OconnectionPool
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct

/**
 * Created by 长宏 on 2017/4/29 0029.
 */
@Component
@Scope("prototype")
class MysqlPacketHander internal constructor() : ChannelInboundHandlerAdapter() {
    @Autowired
  lateinit  internal var connection: OConnection
    @Autowired
    lateinit private var pool: OconnectionPool
    @Autowired
    lateinit internal var applicationContext: ApplicationContext
    @PostConstruct
    fun myinit() {
        connection = applicationContext.getBean("OConnection") as OConnection
    }

    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        // Close the connection when an exception is raised.
        cause.printStackTrace()
        if (true) {
            pool.remove(connection)
        }
        ctx.close()
    }

    @Throws(Exception::class)
    override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
        val mySQLPacket = msg as MySQLPacket
        if (mySQLPacket is AuthPacket) {
            connection.host=ctx.channel().remoteAddress().toString()
            connection.handerAuth(mySQLPacket)
            pool.add(connection)
        } else if (mySQLPacket is CommandPacket) {
            if (!pool.checkMax()) {
                connection.writeErrMessage("too much connection!!!")
                pool.remove(connection)
                ctx.close()
                return
            }
            connection.handerCommand(mySQLPacket)
        } else {

        }
    }

    @Throws(Exception::class)
    override fun channelActive(ctx: ChannelHandlerContext) {
        connection.channelHandlerContext = ctx
        //发送握手包
        connection.register()
    }

    @Throws(Exception::class)
    override fun userEventTriggered(ctx: ChannelHandlerContext, evt: Any) {
        super.userEventTriggered(ctx, evt)
    }
}
