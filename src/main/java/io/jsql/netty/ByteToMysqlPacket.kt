package io.jsql.netty

import io.jsql.mysql.mysql.AuthPacket
import io.jsql.mysql.mysql.CommandPacket
import io.jsql.mysql.mysql.MySQLPacket
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component

/**
 * Created by 长宏 on 2017/4/30 0030.
 * byte到mysql包的解析。
 */
@Component
@Scope("prototype")
class ByteToMysqlPacket : ChannelInboundHandlerAdapter() {
    @Throws(Exception::class)
    override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
        val data = msg as ByteArray
        val mySQLPacket: MySQLPacket
        if (data[4] >= MySQLPacket.MIN_COMP_NUMBER && data[4] <= MySQLPacket.MAX_COMP_NUMBER) {
            mySQLPacket = CommandPacket()
            mySQLPacket.read(data)
        } else {
            mySQLPacket = AuthPacket()
            mySQLPacket.read(data)
        }
        ctx.fireChannelRead(mySQLPacket)

    }
}
