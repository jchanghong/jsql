/*
 * Java-based distributed database like Mysql
 */

package io.jsql.hazelcast

import java.io.Serializable

/**
 * Created by 长宏 on 2017/5/5 0005.
 */
class ReplicationCMD(var fromlsn: Long, var tolsn: Long, var id: Long
) : Serializable
