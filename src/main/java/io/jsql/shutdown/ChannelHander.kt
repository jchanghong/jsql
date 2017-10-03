/*
 * Java-based distributed database like Mysql
 */

package io.jsql.shutdown

import io.netty.buffer.Unpooled
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import org.slf4j.LoggerFactory

/**
 * Created by 长宏 on 2017/5/4 0004.
 */
class ChannelHander : ChannelInboundHandlerAdapter() {
    internal var logger = LoggerFactory.getLogger(ChannelHander::class.java.name)
    @Throws(Exception::class)
    override fun channelActive(ctx: ChannelHandlerContext) {
        logger.info("send exit to server......")
        val future = ctx.writeAndFlush(Unpooled.wrappedBuffer(Shutdown.exit_command))
        future.addListener {
            logger.info("send complete...")
            ctx.close()
        }
    }

    @Throws(Exception::class)
    override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
        super.channelRead(ctx, msg)
    }

    @Throws(Exception::class)
    override fun channelReadComplete(ctx: ChannelHandlerContext) {
        super.channelReadComplete(ctx)
    }

    @Throws(Exception::class)
    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        ctx.close()
    }
}
