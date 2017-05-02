/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package io.jsql.netty;

import io.jsql.my_config.MyProperties;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * .
 * 服务器
 */
@Service
public final class NettyServer {
    @Value("${server.port}")
    private int PORT;
    @Autowired
    ApplicationContext applicationContext;
    Logger logger = LoggerFactory.getLogger(NettyServer.class.getName());
    public  void start() throws Exception {
        logger.info("port is " + PORT);
        // Configure the server.
        final  EventExecutorGroup group = new DefaultEventExecutorGroup(Runtime.getRuntime().availableProcessors());
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 100)
//             .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline p = ch.pipeline();
                            //p.addLast(new LoggingHandler(LogLevel.INFO));
                            p.addLast("idle", new IdleStateHandler(10, 5, 0));
                            p.addLast("decoder",  getByteToMysqlDecoder());
                            p.addLast("packet",  getByteToMysqlPacket());
                            p.addLast(group, "hander",getMysqlPacketHander());
                        }
                    });
            // Start the server.
            ChannelFuture f = b.bind(PORT).sync();
            // Wait until the server socket is closed.
            f.channel().closeFuture().sync();
        } finally {
            // Shut down all event loops to terminate all threads.
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    private ChannelHandler getByteToMysqlDecoder() {
        return applicationContext.getBean(ByteToMysqlDecoder.class);
    }

    private ChannelHandler getByteToMysqlPacket() {
        return applicationContext.getBean(ByteToMysqlPacket.class);
    }

    private ChannelHandler getMysqlPacketHander() {
        return applicationContext.getBean(MysqlPacketHander.class);
    }

}
