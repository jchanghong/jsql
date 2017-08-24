/*
 * Java-based distributed database like Mysql
 */

package io.jsql.shutdown

import io.netty.bootstrap.Bootstrap
import io.netty.channel.ChannelFuture
import io.netty.channel.EventLoopGroup
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioSocketChannel

/**
 * Created by 长宏 on 2017/5/4 0004.
 */
object Shutdown {
    var exit_command = byteArrayOf(9)

    internal var port = 10001
    @Throws(Exception::class)
    @JvmStatic fun main1(args: Array<String>) {
        // Configure the server.
        val workerGroup = NioEventLoopGroup()
        try {

            for (i in port downTo 9991) {
                println("try" + i)
                try {
                    val b = Bootstrap()
                    b.group(workerGroup)
                            .channel(NioSocketChannel::class.java).handler(ChannelHander()).remoteAddress("127.0.0.1", i)
                    // Start the server.
                    var f: ChannelFuture? = null
                    f = b.connect().sync()
                    println(f!!.isDone)
                    f.channel().closeFuture().sync()
                } catch (e: Exception) {
                    //                    e.printStackTrace();
                }

            }
        } finally {
            // Shut down all event loops to terminate all threads.
            workerGroup.shutdownGracefully()
        }
    }
}
