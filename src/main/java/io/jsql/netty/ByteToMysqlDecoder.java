package io.jsql.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Created by 长宏 on 2017/4/30 0030.
 * byte到完整的byte包
 */
@Component
@Scope("prototype")
public class ByteToMysqlDecoder extends ChannelInboundHandlerAdapter {
    private final static int BUFF_SIZE = 512;
    private ByteBuf buf;
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf m = (ByteBuf) msg; // (1)
        if (m.readableBytes() < 1) {
            return;
        }
        if (buf == null) {
            buf = ctx.alloc().heapBuffer(BUFF_SIZE);
        }
        try {
            buf.writeBytes(m);
        } finally {
            m.release();
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        if (buf != null) {
            byte[] bytes = new byte[buf.readableBytes()];
            buf.readBytes(bytes);
            ctx.fireChannelRead(bytes);
            buf.release();
            buf = null;
        }
    }

}
