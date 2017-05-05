package io.jsql.hazelcast;

import java.io.Serializable;

/**
 * Created by 长宏 on 2017/5/5 0005.
 */
public class ReplicationCMD implements Serializable{
    public long fromlsn;
    public long tolsn;
    public long id;

    public ReplicationCMD(long fromlsn, long tolsn, long id
    ) {
        this.id = id;
        this.fromlsn = fromlsn;
        this.tolsn = tolsn;
    }
}
