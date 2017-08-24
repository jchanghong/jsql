/*
 * Java-based distributed database like Mysql
 */
package io.jsql.config

/**
 * 事务隔离级别定义

 * @author jsql
 */
interface Isolations {
    companion object {

        val READ_UNCOMMITTED = 1
        val READ_COMMITTED = 2
        val REPEATED_READ = 3
        val SERIALIZABLE = 4
    }

}
