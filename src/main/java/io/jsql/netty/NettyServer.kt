/*
 * Java-based distributed database like Mysql
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
    private val PORT: Int = 9999
    @Autowired
  lateinit  internal var applicationContext: ApplicationContext
    internal var logger = LoggerFactory.getLogger(NettyServer::class.java.name)
    @Autowired
   lateinit internal var myHazelcast: MyHazelcast
    @Autowired
   lateinit var bytetomysql:ByteToMysqlPacket
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
                            p.addLast("packet", bytetomysql)
                            p.addLast(group, "hander", mysqlPacketHander)
                        }
                    })
            // Start the server.
            val f = b.bind(PORT).sync()
            // Wait until the server socket is closed.
            logger.info("server start  complete.................... ")
            Minformation_schama.init_if_notexits()
            myHazelcast.inits()
            f.channel().closeFuture().sync()
        } finally {
            // Shut down all event loops to terminate all threads.
            bossGroup.shutdownGracefully()
            workerGroup.shutdownGracefully()
        }
    }
    private val byteToMysqlDecoder: ChannelHandler
        get() = applicationContext.getBean(ByteToMysqlDecoder::class.java)
    private val mysqlPacketHander: ChannelHandler
        get() = applicationContext.getBean(MysqlPacketHander::class.java)

}
