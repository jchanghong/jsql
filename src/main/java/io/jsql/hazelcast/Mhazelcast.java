package io.jsql.hazelcast;

import com.google.common.collect.Lists;
import com.hazelcast.config.Config;
import com.hazelcast.core.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 长宏 on 2017/5/5 0005.
 */
//@Component
public class Mhazelcast implements ItemListener<SqlUpdateLog>, MessageListener<String> {
   public HazelcastInstance hazelcastInstance;
  public   IQueue<SqlUpdateLog> sqlUpdateLogs;
    public IQueue<SqlUpdateLog> sendingSQLa;
    @Autowired
    LogFile logFile;
    ILock iLock;
    ILock iLockread;
    ITopic<String> iTopicCMD;
    Logger logger = LoggerFactory.getLogger(Mhazelcast.class.getName());
    long maxlsn;
   public Member member;
    IAtomicLong iAtomicLong;
    public Mhazelcast() {
        Config config = new Config();
        hazelcastInstance = Hazelcast.newHazelcastInstance(config);
        member = hazelcastInstance.getCluster().getLocalMember();
        sqlUpdateLogs = hazelcastInstance.getQueue("sqls");
        sendingSQLa = hazelcastInstance.getQueue("sendingsqls");
        sqlUpdateLogs.addItemListener(this, true);
        iLock = hazelcastInstance.getLock("cansending");
        iLockread = hazelcastInstance.getLock("ilockread");
        iAtomicLong = hazelcastInstance.getAtomicLong("lsn");
        iTopicCMD = hazelcastInstance.getTopic("itopiccmd");
        iTopicCMD.addMessageListener(this);

        maxlsn = logFile.maxLSN();
        if (maxlsn >= iAtomicLong.get()) {
            iAtomicLong.set(maxlsn);
        }
        else {
            iLockread.lock();
            iTopicCMD.publish("getdata");
            long tmp = iAtomicLong.get();
            try {
                List<SqlUpdateLog> logs = new ArrayList<>();
                while (maxlsn < tmp) {
                    SqlUpdateLog sqlUpdateLog = sendingSQLa.poll();
                    if (sqlUpdateLog != null) {
                        maxlsn = sqlUpdateLog.LSN;
                        logFile.write(sqlUpdateLog);
                        logs.add(sqlUpdateLog);
                    }
                }
                dosql(logs);
            }
            finally {
                iLockread.lock();
            }

        }
    }

    private void dosql(List<SqlUpdateLog> logs) {

    }

    @Override
    public void onMessage(Message<String> message) {
        if (message.getMessageObject().equals("getdata")) {

        }

    }
    public void sendsql(String sql) {
        if (isupdatesql(sql)) {
            maxlsn = iAtomicLong.addAndGet(1);
            SqlUpdateLog log = new SqlUpdateLog(maxlsn, sql);
            sqlUpdateLogs.offer(log);
        }
    }
    private boolean isupdatesql(String sql) {
        String sqll = sql.toLowerCase();
        List<String> list = Lists.newArrayList("delete", "drop", "create", "insert", "update");
        for (String s : list) {
            if (sqll.contains(s)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void itemAdded(ItemEvent<SqlUpdateLog> item) {
        logger.info(item.toString());
        logger.info(item.getMember().toString());
        logger.info(item.getMember().equals(member)+"");
    }

    @Override
    public void itemRemoved(ItemEvent<SqlUpdateLog> item) {
        logger.info(item.toString());
    }


}
