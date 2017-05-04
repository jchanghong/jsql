package io.jsql.shutdown;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * Created by 长宏 on 2017/5/4 0004.
 */
public class Shutdown {
    public static byte[] exit_command = {9};

    static int port = 10001;
    public static void main1(String[] args) throws Exception {
        // Configure the server.
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {

            for (int i = port; i > 9990; i--) {
                System.out.println("try"+i);
                try {
                    Bootstrap b = new Bootstrap();
                    b.group(workerGroup)
                            .channel(NioSocketChannel.class).
                            handler(new ChannelHander()).remoteAddress("127.0.0.1", i);
                    // Start the server.
                    ChannelFuture f = null;
                    f = b.connect().sync();
                    System.out.println(f.isDone());
                    f.channel().closeFuture().sync();
                } catch (Exception e) {
//                    e.printStackTrace();
                }
            }
        } finally {
            // Shut down all event loops to terminate all threads.
            workerGroup.shutdownGracefully();
        }
    }
}
