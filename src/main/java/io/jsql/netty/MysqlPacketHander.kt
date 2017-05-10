package io.jsql.netty

import io.jsql.mysql.mysql.AuthPacket
import io.jsql.mysql.mysql.CommandPacket
import io.jsql.mysql.mysql.MySQLPacket
import io.jsql.sql.OConnection
import io.jsql.sql.OconnectionPool
import io.jsql.sql.handler.AllHanders
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component

/**
 * Created by 长宏 on 2017/4/29 0029.
 */
@Component
@Scope("prototype")
class MysqlPacketHander internal constructor() : ChannelInboundHandlerAdapter() {
    @Autowired
    internal var allHanders: AllHanders? = null
    internal var connection: OConnection? = null
    @Autowired
    private val pool: OconnectionPool? = null
    @Autowired
    internal var applicationContext: ApplicationContext? = null

    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        // Close the connection when an exception is raised.
        cause.printStackTrace()
        if (connection != null) {
            pool!!.remove(connection!!)
            connection = null
        }
        //        ctx.channel().
        ctx.close()
    }

    @Throws(Exception::class)
    override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
        val mySQLPacket = msg as MySQLPacket
        if (mySQLPacket is AuthPacket) {
            connection!!.handerAuth(mySQLPacket)
            pool!!.add(connection!!)
        } else if (mySQLPacket is CommandPacket) {
            if (!pool!!.checkMax()) {
                connection!!.writeErrMessage("too much connection!!!")
                pool.remove(connection!!)
                connection = null
                ctx.close()
                return
            }
            connection!!.handerCommand(mySQLPacket)
        } else {

        }
    }

    @Throws(Exception::class)
    override fun channelActive(ctx: ChannelHandlerContext) {
        connection = applicationContext!!.getBean("OConnection") as OConnection
        connection!!.channelHandlerContext = ctx
        //发送握手包
        connection!!.register()
    }

    @Throws(Exception::class)
    override fun userEventTriggered(ctx: ChannelHandlerContext, evt: Any) {
        super.userEventTriggered(ctx, evt)
    }
}
