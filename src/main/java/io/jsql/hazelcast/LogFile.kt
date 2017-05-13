package io.jsql.hazelcast

import com.google.common.io.Files
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.io.RandomAccessFile
import java.util.*

/**
 * Created by 长宏 on 2017/5/4 0004.
 */
@Component
class LogFile {

   lateinit internal var randomAccessFile: RandomAccessFile
    @Value("\${log.file}")
    var filename = "./config/log/log.log"
    init {
        val file = File(filename)
        if (!file.exists()) {
            try {
                Files.createParentDirs(file)
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
        try {
            randomAccessFile = RandomAccessFile(filename, "rw")
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }

    }

    fun write(updateLog: SqlUpdateLog) {
        try {
            randomAccessFile.seek(if (randomAccessFile.length()==0L) 8 else randomAccessFile.length())
            randomAccessFile.writeLong(updateLog.LSN)
            randomAccessFile.writeUTF(updateLog.sql)
            randomAccessFile.writeUTF(updateLog.db)
            randomAccessFile.seek(0)
            randomAccessFile.writeLong(updateLog.LSN)
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    fun maxLSN(): Long =
        try {
            randomAccessFile.seek(0)
            randomAccessFile.readLong()
        } catch (e: IOException) {
            e.printStackTrace()
             0
        }

    fun getall(): List<SqlUpdateLog> {
        val logs = ArrayList<SqlUpdateLog>()
        try {
            randomAccessFile.seek(8)
        } catch (e: IOException) {
                        e.printStackTrace();
            return logs
        }

        while (true) {
            try {
                val lsn = randomAccessFile.readLong()
                val sql = randomAccessFile.readUTF()
                val db = randomAccessFile.readUTF()
                logs.add(SqlUpdateLog(lsn, sql, db))
            } catch (e: IOException) {
                                e.printStackTrace();
                break
            }
        }
        return logs
    }

    fun close() {
        try {
            randomAccessFile.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    companion object {
        internal var logger = LoggerFactory.getLogger(LogFile::class.java.name)
        @Throws(IOException::class)
        @JvmStatic fun main(args: Array<String>) {
            val logFile = LogFile()
            logFile.write(SqlUpdateLog(1, "sql", "d1"))
            println(logFile.maxLSN())
            logFile.write(SqlUpdateLog(2, "sql2", "d2"))
            println(logFile.maxLSN())
            logFile.write(SqlUpdateLog(3, "sql3", "d3"))
            println(logFile.maxLSN())
            logFile.getall().forEach { a -> println(a.toString()) }
            logFile.close()

        }
    }
}
