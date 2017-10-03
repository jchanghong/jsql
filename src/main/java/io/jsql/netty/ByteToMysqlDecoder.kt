/*
 * Java-based distributed database like Mysql
 */

package io.jsql.netty

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component

/**
 * Created by 长宏 on 2017/4/30 0030.
 * byte到完整的byte包
 */
@Component
@Scope("prototype")
class ByteToMysqlDecoder : ChannelInboundHandlerAdapter() {
    private var buf: ByteBuf? = null
    @Throws(Exception::class)
    override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
        val m = msg as ByteBuf // (1)
        if (m.readableBytes() < 1) {
            return
        }
        if (buf == null) {
            buf = ctx.alloc().heapBuffer(BUFF_SIZE)
        }
        try {
            buf!!.writeBytes(m)
        } finally {
            m.release()
        }
    }

    @Throws(Exception::class)
    override fun channelReadComplete(ctx: ChannelHandlerContext) {
        if (buf != null) {
            val bytes = ByteArray(buf!!.readableBytes())
            if (bytes.size == 1) {
                System.exit(0)
            }
            buf!!.readBytes(bytes)
            buf!!.release()
            buf = null
            ctx.fireChannelRead(bytes)
        }
    }

    companion object {
        private val BUFF_SIZE = 512

        internal var logger = LoggerFactory.getLogger(ByteToMysqlDecoder::class.java.name)
    }

}
