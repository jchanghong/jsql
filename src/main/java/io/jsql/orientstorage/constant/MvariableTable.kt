package io.jsql.orientstorage.constant

import com.orientechnologies.orient.core.db.document.ODatabaseDocument
import com.orientechnologies.orient.core.metadata.schema.OType
import com.orientechnologies.orient.core.record.impl.ODocument
import io.jsql.sql.OConnection
import io.jsql.storage.StorageException

/**
 * Created by 长宏 on 2017/3/23 0023.
 * Variable_name  value
 */
object MvariableTable {
    val tablename = "variables"
    val tablenamestatus = "status"

    fun init_if_notexits() {
        var documentTx: ODatabaseDocument? = null
        try {
            documentTx = OConnection.DB_ADMIN.getdb(Minformation_schama.dbname)
        } catch (e: StorageException) {
            e.printStackTrace()
            System.exit(-1)
        }
        if (documentTx == null) {
            return
        }

        if (documentTx.metadata.schema.existsClass(tablename)) {
            OConnection.DB_ADMIN.close(documentTx)
        } else {
            val oClass = documentTx.metadata.schema.createClass(tablename)
            val oClass1 = documentTx.metadata.schema.createClass(tablenamestatus)
            oClass.isStrictMode = true
            oClass.createProperty("Variable_name", OType.STRING)
            oClass.createProperty("value", OType.STRING)
            oClass1.isStrictMode = true
            oClass1.createProperty("Variable_name", OType.STRING)
            oClass1.createProperty("value", OType.STRING)
            for ((k, vararg) in Mconstantvariables.MAP) {
                val document = ODocument(tablename)
                document.field("Variable_name", k)
                document.field("value", vararg)
                document.save()
            }
            for ((k, v) in MconstantStatusVariables.MAP) {
                val document = ODocument(tablenamestatus)
                document.field("Variable_name", k)
                document.field("value", v)
                document.save()
            }
            OConnection.DB_ADMIN.close(documentTx)
        }
    }
}
