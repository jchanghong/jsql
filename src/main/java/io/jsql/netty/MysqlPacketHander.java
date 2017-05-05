package io.jsql.netty;

import io.jsql.mysql.mysql.AuthPacket;
import io.jsql.mysql.mysql.CommandPacket;
import io.jsql.mysql.mysql.MySQLPacket;
import io.jsql.sql.OConnection;
import io.jsql.sql.OconnectionPool;
import io.jsql.sql.handler.AllHanders;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Created by 长宏 on 2017/4/29 0029.
 */
@Component
@Scope("prototype")
public class MysqlPacketHander extends ChannelInboundHandlerAdapter {
    @Autowired
    AllHanders allHanders;
    OConnection connection;
    @Autowired
   private OconnectionPool pool;
    @Autowired
    ApplicationContext applicationContext;
    MysqlPacketHander() {
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        if (connection != null) {
            pool.remove(connection);
            connection = null;
        }
//        ctx.channel().
        ctx.close();
    }
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        MySQLPacket mySQLPacket = (MySQLPacket) msg;
        if (mySQLPacket instanceof AuthPacket) {
            connection.handerAuth((AuthPacket) mySQLPacket);
            pool.add(connection);
        } else if (mySQLPacket instanceof CommandPacket) {
            if (!pool.checkMax()) {
                connection.writeErrMessage("too much connection!!!");
                pool.remove(connection);
                connection = null;
                ctx.close();
                return;
            }
            connection.handerCommand((CommandPacket) mySQLPacket);
        } else {

        }
    }
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        connection = (OConnection) applicationContext.getBean("OConnection");
        connection.channelHandlerContext = (ctx);
        //发送握手包
        connection.register();
    }
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);
    }
}
