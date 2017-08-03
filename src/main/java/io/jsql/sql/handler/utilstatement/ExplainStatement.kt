//package io.jsql.sql.handler.utilstatement
//
//import com.alibaba.druid.sql.ast.SQLStatement
//import com.alibaba.druid.sql.ast.statement.SQLExplainStatement
//import com.alibaba.druid.sql.ast.statement.SQLSelectStatement
//import com.alibaba.druid.sql.ast.statement.SQLUseStatement
//import com.orientechnologies.orient.core.db.document.ODatabaseDocument
//import com.orientechnologies.orient.core.metadata.schema.OClass
//import com.orientechnologies.orient.core.metadata.schema.OProperty
//import com.orientechnologies.orient.core.record.OElement
//import com.orientechnologies.orient.core.record.impl.ODocument
//import io.jsql.config.ErrorCode
//import io.jsql.config.Fields
//import io.jsql.mysql.PacketUtil
//import io.jsql.mysql.mysql.EOFPacket
//import io.jsql.mysql.mysql.FieldPacket
//import io.jsql.mysql.mysql.ResultSetHeaderPacket
//import io.jsql.orientstorage.sqlhander.sqlutil.MSQLutil
//import io.jsql.sql.OConnection
//import io.jsql.sql.handler.MyResultSet
//import io.jsql.sql.handler.SqlStatementHander
//import io.jsql.storage.StorageException
//import org.springframework.stereotype.Component
//import java.util.function.Consumer
//
///**
// * Created by 长宏 on 2017/3/27.
// */
//@Component//spring标记，这样以后，就会自动生成这个对象。不用手工new
//class ExplainStatement: SqlStatementHander() {
//    override fun supportSQLstatement(): Class<out SQLStatement> {
//        return SQLExplainStatement::class.java
//    }
//
//    //下面这个语句才会经过这个类
//    /*val sql = "EXPLAIN select * from t1;"*/
//    override fun handle0(sqlStatement: SQLStatement, c: OConnection): Any? {
//        if (sqlStatement is SQLExplainStatement) {
//            var  sqls=sqlStatement.statement
//            var  sql=sqls.database.toString()
//            val documentTx: ODatabaseDocument
//            try {
//                documentTx = OConnection.DB_ADMIN!!.getdb(c.schema!!)
//            } catch (e: StorageException) {
//                e.printStackTrace()
//                return e.message!!
//            }
//            documentTx.activateOnCurrentThread()
//          var tablename=sqlStatement.toString().split(" ").toList()
//val oClass=documentTx.getClass(tablename[1])
////           val oClass = documentTx.getClass(MSQLutil.gettablename("explain OUser" ))
//          var list=ArrayList<String>()
//            var elements=ArrayList<OElement>()
//            oClass.properties().forEach { a -> val element = ODocument()
////                list.add(a.name)
//                element.setProperty("Field",a.name)
//                element.setProperty("Type",a.type)
//                element.setProperty("Null",a.isNotNull)
//                element.setProperty("Key"," ")
//                element.setProperty("Default",a.defaultValue)
//                element.setProperty("Extra"," ")
//                elements.add(element)
//            }
//            return MyResultSet(elements, listOf("Field","Type","Null","Key","Default","Extra"))
//        }
//        else{
//            return "error"
//        }
//    }
//
//
//}
//
