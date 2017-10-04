/*
 * Java-based distributed database like Mysql
 */

package io.jsql.orientstorage.constant

import io.jsql.sql.OConnection
import io.jsql.storage.StorageException
import org.springframework.stereotype.Component

/**
 * Created by 长宏 on 2017/3/23 0023.
 */
object Minformation_schama {
    val dbname = "information_schema"

    fun init_if_notexits() {
        try {
            if (OConnection.DB_ADMIN.getallDBs().contains(dbname)) {
            } else {
                try {
                    OConnection.DB_ADMIN.createdbSyn(dbname)
                    MvariableTable.init_if_notexits()
                } catch (e: StorageException) {
                    e.printStackTrace()
                }

            }
        } catch (e: StorageException) {
            e.printStackTrace()
            System.exit(-1)

        }

    }
}
