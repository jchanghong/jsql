package io.jsql.hazelcast;

import com.google.common.io.Files;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 长宏 on 2017/5/4 0004.
 */
@Component
public class LogFile {
    @Value("${log.file}")
  static   String filename = "./config/log/log.log";
    static Logger logger = LoggerFactory.getLogger(LogFile.class.getName());

    RandomAccessFile randomAccessFile;
    public LogFile() {
        File file = new File(filename);
        if (!file.exists()) {
            try {
                Files.createParentDirs(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            randomAccessFile = new RandomAccessFile(filename, "rw");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    public void write(SqlUpdateLog updateLog) {
        try {
            randomAccessFile.seek(randomAccessFile.length() == 0 ? 8 : randomAccessFile.length());
            randomAccessFile.writeLong(updateLog.LSN);
            randomAccessFile.writeUTF(updateLog.sql);
            randomAccessFile.writeUTF(updateLog.db);
            randomAccessFile.seek(0);
            randomAccessFile.writeLong(updateLog.LSN);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public long maxLSN() {
        try {
            randomAccessFile.seek(0);
            return randomAccessFile.readLong();
        } catch (IOException e) {
//            e.printStackTrace();
            return 0;
        }
    }
    public List<SqlUpdateLog> getall() {
        List<SqlUpdateLog> logs = new ArrayList<>();
        try {
            randomAccessFile.seek(8);
        } catch (IOException e) {
//            e.printStackTrace();
            return logs;
        }
        while (true) {
            try {
                long lsn = randomAccessFile.readLong();
                String sql = randomAccessFile.readUTF();
                String db = randomAccessFile.readUTF();
                logs.add(new SqlUpdateLog(lsn, sql,db));
            } catch (IOException e) {
//                e.printStackTrace();
                break;
            }
        }
        return logs;
    }

    public void close() {
        try {
            randomAccessFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public static void main(String[] args) throws IOException {

        LogFile logFile = new LogFile();
        logFile.write(new SqlUpdateLog(1, "sql","d1"));
        System.out.println(logFile.maxLSN());
        logFile.write(new SqlUpdateLog(2, "sql2","d2"));
        System.out.println(logFile.maxLSN());
        logFile.write(new SqlUpdateLog(3, "sql3","d3"));
        System.out.println(logFile.maxLSN());
        logFile.getall().forEach(a->System.out.println(a.toString()));
        logFile.close();

    }
}
