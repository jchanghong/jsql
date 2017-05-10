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
package io.jsql.netty

import io.jsql.hazelcast.MyHazelcast
import io.jsql.my_config.MyProperties
import io.jsql.orientstorage.constant.Minformation_schama
import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.*
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.handler.timeout.IdleStateHandler
import io.netty.util.concurrent.DefaultEventExecutorGroup
import io.netty.util.concurrent.EventExecutorGroup
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Service

import javax.annotation.PostConstruct

/**
 * .
 * 服务器
 */
@Service
class NettyServer {
    @Value("\${server.port}")
    private val PORT: Int = 0
    @Autowired
    internal var applicationContext: ApplicationContext? = null
    internal var logger = LoggerFactory.getLogger(NettyServer::class.java.name)
    @Autowired
    internal var myHazelcast: MyHazelcast? = null

    @Throws(Exception::class)
    fun start() {
        logger.info("port is " + PORT)
        // Configure the server.
        val group = DefaultEventExecutorGroup(Runtime.getRuntime().availableProcessors())
        val bossGroup = NioEventLoopGroup(1)
        val workerGroup = NioEventLoopGroup()
        try {
            val b = ServerBootstrap()
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel::class.java)
                    .option(ChannelOption.SO_BACKLOG, 100)
                    //             .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(object : ChannelInitializer<SocketChannel>() {
                        @Throws(Exception::class)
                        public override fun initChannel(ch: SocketChannel) {
                            val p = ch.pipeline()
                            //p.addLast(new LoggingHandler(LogLevel.INFO));
                            p.addLast("idle", IdleStateHandler(10, 5, 0))
                            p.addLast("decoder", byteToMysqlDecoder)
                            p.addLast("packet", byteToMysqlPacket)
                            p.addLast(group, "hander", mysqlPacketHander)
                        }
                    })
            // Start the server.
            val f = b.bind(PORT).sync()
            // Wait until the server socket is closed.
            logger.info("server start  complete.................... ")
            myHazelcast!!.inits()
            Minformation_schama.init_if_notexits()
            f.channel().closeFuture().sync()
        } finally {
            // Shut down all event loops to terminate all threads.
            bossGroup.shutdownGracefully()
            workerGroup.shutdownGracefully()
        }
    }

    private val byteToMysqlDecoder: ChannelHandler
        get() = applicationContext!!.getBean(ByteToMysqlDecoder::class.java)

    private val byteToMysqlPacket: ChannelHandler
        get() = applicationContext!!.getBean(ByteToMysqlPacket::class.java)

    private val mysqlPacketHander: ChannelHandler
        get() = applicationContext!!.getBean(MysqlPacketHander::class.java)

}
