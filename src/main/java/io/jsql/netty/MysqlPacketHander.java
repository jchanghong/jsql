package io.jsql.netty;

import io.jsql.mysql.mysql.AuthPacket;
import io.jsql.mysql.mysql.CommandPacket;
import io.jsql.mysql.mysql.MySQLPacket;
import io.jsql.orientserver.OConnection;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Created by 长宏 on 2017/4/29 0029.
 */
public class MysqlPacketHander extends ChannelInboundHandlerAdapter {
    OConnection connection;
    MysqlPacketHander() {
    }
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        MySQLPacket mySQLPacket = (MySQLPacket) msg;
        if (mySQLPacket instanceof AuthPacket) {
            connection.handerAuth((AuthPacket) mySQLPacket);
        } else if (mySQLPacket instanceof CommandPacket) {
            connection.handerCommand((CommandPacket) mySQLPacket);
        }
        else {

        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        connection = new OConnection();
        connection.channelHandlerContext=(ctx);
        //发送握手包
        connection.register();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);
    }
}
