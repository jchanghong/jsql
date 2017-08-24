/*
 * Java-based distributed database like Mysql
 */

package io.jsql.sql.handler.adminstatement

import com.alibaba.druid.sql.ast.SQLStatement
import com.alibaba.druid.sql.ast.statement.SQLExplainStatement
import com.orientechnologies.orient.core.db.document.ODatabaseDocument
import com.orientechnologies.orient.core.record.OElement
import com.orientechnologies.orient.core.record.impl.ODocument
import io.jsql.config.ErrorCode
import io.jsql.config.Fields
import io.jsql.mysql.PacketUtil
import io.jsql.mysql.mysql.FieldPacket
import io.jsql.sql.OConnection
import io.jsql.sql.handler.MyResultSet
import io.jsql.sql.handler.SqlStatementHander
import io.jsql.storage.StorageException
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.util.regex.Pattern

/**
 * 此类用于解析explain select*from table
 * @author jsql
 */
@Component
class ExplainHandler : SqlStatementHander() {

    override fun supportSQLstatement(): Class<out SQLStatement> {
        return SQLExplainStatement::class.java
    }

    @Throws(Exception::class)
    override fun handle0(sqlStatement: SQLStatement, c: OConnection): Any? {
        val explain=sqlStatement as SQLExplainStatement
        val documentTx: ODatabaseDocument
        try {
            documentTx = OConnection.DB_ADMIN!!.getdb(c.schema!!)
        } catch (e: StorageException) {
            e.printStackTrace()
            return e.message!!
        }
        documentTx.activateOnCurrentThread()
        var tablename =explain.toString().split(" ")
        val oClass=documentTx.getClass(tablename[2])
        val element = ODocument()
        var elements=ArrayList<OElement>()

        element.setProperty("id","1")
        element.setProperty("select_type","simple")
        /*
        SIMPLE - 简单的 SELECT （没有使用 UNION 或子查询
        PRIMARY - 最外层的 SELECT
        UNION - 第二层，在SELECT 之后使用了 UNIO
        DEPENDENT UNION - UNION 语句中的第二个 SELECT，依赖于外部子查
        SUBQUERY - 子查询中的第一个 SELEC
        DEPENDENT SUBQUERY - 子查询中的第一个 SUBQUERY 依赖于外部的子查
        DERIVED - 派生表 SELECT（FROM 子句中的子查询
        */
        element.setProperty("table",oClass.toString())
        element.setProperty("partitions","")
        element.setProperty("type","all")//表连接类型
        element.setProperty("possible_keys","NULL")
        element.setProperty("key","NULL")
        element.setProperty("key_len","NULL")
        element.setProperty("ref","NULL")
        element.setProperty("rows",oClass.properties().size)
        element.setProperty("filtered","100.00")
        element.setProperty("extra","NULL")
        elements.add(element)




        return MyResultSet(elements, listOf("id","select_type","table","partitions","type","possible_keys","key","key_len","ref","rows","filtered","extra"))

    }

    companion object {

        private val logger = LoggerFactory.getLogger(ExplainHandler::class.java)
        private val pattern = Pattern.compile("(?:(\\s*next\\s+value\\s+for\\s*MYCATSEQ_(\\w+))(,|\\)|\\s)*)+", Pattern.CASE_INSENSITIVE)
        //    private static final RouteResultsetNode[] EMPTY_ARRAY = new RouteResultsetNode[0];
        private val FIELD_COUNT = 2
        private val fields = arrayOfNulls<FieldPacket>(FIELD_COUNT)

        init {
            fields[0] = PacketUtil.getField("DATA_NODE",
                    Fields.FIELD_TYPE_VAR_STRING)
            fields[1] = PacketUtil.getField("SQL", Fields.FIELD_TYPE_VAR_STRING)
        }

        fun handle(stmt: String, c: OConnection, offset: Int) {
            var stmt = stmt
            stmt = stmt.substring(offset).trim { it <= ' ' }



        fun handle(x: SQLExplainStatement, connection: OConnection) {
            connection.writeErrMessage(ErrorCode.ER_CHECK_NO_SUCH_TABLE, "not soupot")
        }
    }
}}
