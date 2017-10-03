/*
 * Java-based distributed database like Mysql
 */
package io.jsql.sql

import com.google.common.base.MoreObjects
import io.jsql.mysql.mysql.AuthPacket
import io.jsql.mysql.mysql.CommandPacket
import io.jsql.mysql.mysql.MySQLPacket
import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled

/**
 * The type O connection.

 * @author changhong
 * * 只用来执行后台命令。
 * 用于分布式实现
 */
class ONullConnection : OConnection() {

    internal override fun init() {}

    override fun writeNotSurrport() {}

    override fun writeErrMessage(errno: Int, msg: String) {}

    override fun writeErrMessage(id: Byte, errno: Int, msg: String) {}

    override fun close(reason: String) {}

    override fun toString(): String {
        return MoreObjects.toStringHelper(this).toString()
    }

    override fun writeok() {}

    override fun writeErrMessage(message: String) {}

    override fun register() {}

    override fun allocate(): ByteBuf {
        return Unpooled.buffer(12)
    }


    override fun handerAuth(authPacket: AuthPacket) {}

    override fun handerCommand(commandPacket: CommandPacket) {}

    override fun write(byteBuf: ByteBuf) {}

    override fun writeResultSet(header: MySQLPacket, filed: Array<MySQLPacket>, eof1: MySQLPacket, rows: Array<MySQLPacket>, eof2: MySQLPacket) {}
}
