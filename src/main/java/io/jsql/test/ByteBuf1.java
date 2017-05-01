package io.jsql.test;

import io.jsql.mysql.MBufferUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;

/**
 * Created by 长宏 on 2017/5/1 0001.
 */

public class ByteBuf1 {
    public static void main(String[] args) {
        ByteBuf buf = Unpooled.buffer(1024);
        ByteBuf buf1 = Unpooled.buffer(1024);
        buf.writeBytes("12345".getBytes());
        buf.writeBytes("6789".getBytes());
        ByteBuf buf2 = Unpooled.copiedBuffer(buf, buf1);
        System.out.println(ByteBufUtil.prettyHexDump(buf));
        buf.writeInt(1);
        System.out.println(ByteBufUtil.getBytes(buf).length);
        System.out.println(ByteBufUtil.prettyHexDump(buf));
        System.out.println(ByteBufUtil.swapMedium(1));
        buf.clear();
        buf.writeInt(ByteBufUtil.swapInt(1));
        MBufferUtil.writeInt(buf, 1);
        System.out.println(ByteBufUtil.prettyHexDump(buf));


    }
}

