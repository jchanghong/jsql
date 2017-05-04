package io.jsql.shutdown;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by 长宏 on 2017/5/4 0004.
 */
public class ChannelHander extends ChannelInboundHandlerAdapter {
    Logger logger = LoggerFactory.getLogger(ChannelHander.class.getName());
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("send exit to server......");
        ChannelFuture future = ctx.writeAndFlush(Unpooled.wrappedBuffer(Shutdown.exit_command));
        future.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                logger.info("send complete...");
                ctx.close();
            }
        });
    }
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        super.channelRead(ctx, msg);
    }
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
