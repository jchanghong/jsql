package io.jsql.test

import io.jsql.mysql.MBufferUtil
import io.netty.buffer.ByteBuf
import io.netty.buffer.ByteBufUtil
import io.netty.buffer.Unpooled

/**
 * Created by 长宏 on 2017/5/1 0001.
 */

object ByteBuf1 {
    @JvmStatic fun main(args: Array<String>) {
        val buf = Unpooled.buffer(1024)
        val buf1 = Unpooled.buffer(1024)
        buf.writeBytes("12345".toByteArray())
        buf.writeBytes("6789".toByteArray())
        val buf2 = Unpooled.copiedBuffer(buf, buf1)
        println(ByteBufUtil.prettyHexDump(buf))
        buf.writeInt(1)
        println(ByteBufUtil.getBytes(buf).size)
        println(ByteBufUtil.prettyHexDump(buf))
        println(ByteBufUtil.swapMedium(1))
        buf.clear()
        buf.writeInt(ByteBufUtil.swapInt(1))
        MBufferUtil.writeInt(buf, 1)
        println(ByteBufUtil.prettyHexDump(buf))


    }
}

