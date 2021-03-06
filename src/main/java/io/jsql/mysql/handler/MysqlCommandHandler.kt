/*
 * Java-based distributed database like Mysql
 */
package io.jsql.mysql.handler


import io.jsql.config.ErrorCode
import io.jsql.mysql.MySQLMessage
import io.jsql.mysql.mysql.CommandPacket
import io.jsql.mysql.mysql.MySQLPacket
import io.jsql.sql.OConnection
import io.jsql.storage.StorageException
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.io.UnsupportedEncodingException

/**
 * 前端命令处理器
 * 处理命令包
 */
@Component
class MysqlCommandHandler : MysqlPacketHander {


    override fun hander(mySQLPacket: MySQLPacket, source: OConnection) {
        handle0(mySQLPacket as CommandPacket, source)
    }

    private fun handle0(data: CommandPacket, source: OConnection) {
        logger.debug(data.toString())
        logger.info("command info")

        //        if(source.getLoadDataInfileHandler()!=null&&source.getLoadDataInfileHandler().isStartLoadData())
        //        {
        //            MySQLMessage mm = new MySQLMessage(data);
        //            int  packetLength = mm.readUB3();
        //            if(packetLength+4==data.length)
        //            {
        //                source.loadDataInfileData(data);
        //            }
        //            return;
        //        }
        when (data.command) {
            MySQLPacket.COM_INIT_DB -> initDB(data, source)
            MySQLPacket.COM_QUERY -> query(data, source)
            MySQLPacket.COM_PING -> ping(source)
            MySQLPacket.COM_QUIT -> close("quit cmd", source)
            MySQLPacket.COM_PROCESS_KILL -> kill(data, source)
            MySQLPacket.COM_STMT_PREPARE -> stmtPrepare(data, source)
            MySQLPacket.COM_STMT_SEND_LONG_DATA -> stmtSendLongData(data, source)
            MySQLPacket.COM_STMT_RESET -> stmtReset(data, source)
            MySQLPacket.COM_STMT_EXECUTE -> stmtExecute(data, source)
            MySQLPacket.COM_STMT_CLOSE -> stmtClose(data, source)
            MySQLPacket.COM_HEARTBEAT -> heartbeat(data, source)
            else -> source.writeErrMessage(ErrorCode.ER_UNKNOWN_COM_ERROR,
                    "Unknown command")
        }
    }

    private fun heartbeat(data: CommandPacket, source: OConnection) {
        source.writeok()
    }

    private fun stmtClose(data: CommandPacket, source: OConnection) {
        source.writeok()
    }

    private fun stmtExecute(data: CommandPacket, source: OConnection) {
        source.writeok()
    }

    private fun stmtReset(data: CommandPacket, source: OConnection) {
        source.writeok()
    }

    private fun stmtSendLongData(data: CommandPacket, source: OConnection) {
        source.writeok()
    }

    private fun stmtPrepare(data: CommandPacket, source: OConnection) {
        source.writeok()
    }

    private fun kill(data: CommandPacket, source: OConnection) {
        source.close(data.toString())

    }

    private fun close(s: String, source: OConnection) {
        source.close(s)

    }

    private fun ping(source: OConnection) {
        source.writeok()
    }

    private fun query(data: CommandPacket, source: OConnection) {
        val mm = MySQLMessage(data.arg!!)
        mm.position(0)
        try {
            val sql = mm.readString(source.charset)
            source.sqlHander.handle(sql!!, source)
        } catch (e: UnsupportedEncodingException) {
            source.writeErrMessage(ErrorCode.ER_UNKNOWN_CHARACTER_SET, "Unknown charset '" + source.charset + "'")
            e.printStackTrace()
        }

    }

    private fun initDB(data: CommandPacket, source: OConnection) {
        val mm = MySQLMessage(data.arg!!)
        mm.position(0)
        val db = mm.readString()
        // 检查schema的有效性
        try {
            if (!OConnection.DB_ADMIN.getallDBs().contains(db)) {
                source.writeErrMessage(ErrorCode.ER_BAD_DB_ERROR, "Unknown database '$db'")
                return
            }
        } catch (e: StorageException) {
            e.printStackTrace()
            source.writeErrMessage(e.message!!)
            return
        }

        source.schema = db
        source.writeok()
    }

    companion object {
        var logger = LoggerFactory.getLogger(MysqlCommandHandler::class.java.simpleName)
    }


}