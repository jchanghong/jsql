package io.jsql.mysql.handler

import com.alibaba.druid.sql.ast.SQLStatement
import com.alibaba.druid.sql.dialect.mysql.parser.MySqlStatementParser
import com.google.common.collect.Lists
import io.jsql.config.ErrorCode
import io.jsql.hazelcast.MyHazelcast
import io.jsql.hazelcast.SqlUpdateLog
import io.jsql.sql.OConnection
import io.jsql.sql.handler.AllHanders
import io.jsql.sql.handler.data_define.*
import io.jsql.sql.handler.data_mannipulation.Mdo
import io.jsql.sql.handler.data_mannipulation.Mhandler
import io.jsql.sql.handler.data_mannipulation.Msubquery
import io.jsql.sql.handler.utilstatement.ExplainStatement
import io.jsql.sql.parser.MSQLvisitor
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component

import javax.annotation.PostConstruct

/**
 * Created by 长宏 on 2017/4/30 0030.
 */
@Component
@Scope("prototype")
class MysqlSQLhander : SQLHander {
    private var source: OConnection? = null
    @Autowired
    internal var applicationContext: ApplicationContext? = null
    private var mySqlASTVisitor: MSQLvisitor? = null
    private var exception: Exception? = null
    @Autowired
    private val allHanders: AllHanders? = null

    @PostConstruct
    internal fun init() {
        mySqlASTVisitor = applicationContext!!.getBean(MSQLvisitor::class.java)
    }

//    fun setReadOnly(readOnly: Boolean?) {
//        this.readOnly = readOnly
//    }

    @Autowired
    internal var myHazelcast: MyHazelcast? = null

    override fun handle(sql: String, source: OConnection) {
        logger.info(sql)
        val c = this.source
        if (logger.isDebugEnabled) {
            logger.debug(sql)
        }
        val sqlStatement: SQLStatement
        try {
            val parser = MySqlStatementParser(sql)
            sqlStatement = parser.parseStatement()
            val hander = allHanders!!.handerMap[sqlStatement.javaClass]
            if (isupdatesql(sql)) {
                myHazelcast!!.exeSql(sql, if (source!!.schema == null) "" else source!!.schema!!)
            }
            if (hander != null) {
                hander.handle(sqlStatement, c!!)
                return
            } else {
                sqlStatement.accept(mySqlASTVisitor)
            }

            exception = null
        } catch (e: Exception) {//如果不是合法的mysql语句，就报错
            //            e.printStackTrace();
            exception = e
            //            return;
        }

        if (exception == null) {
            if (isupdatesql(sql)) {
                myHazelcast!!.exeSql(sql, if (source!!.schema == null) "" else source!!.schema!!)
            }
        }
        //druid支持的语句就用上面的方法语句处理，如果不支持，就会有异常，就自己写代码解析sql语句，处理。
        //下面是drop event语句的例子，这个例子druid不支持，所以自己写
        handleotherStatement(sql, c!!)
    }

    fun handle(sql: SqlUpdateLog) {
        logger.info(sql.toString())
        val c = this.source
        if (logger.isDebugEnabled) {
            logger.debug(sql.toString())
        }
        val sqlStatement: SQLStatement
        try {
            val parser = MySqlStatementParser(sql.sql)
            sqlStatement = parser.parseStatement()
            val hander = allHanders!!.handerMap[sqlStatement.javaClass]
            if (hander != null) {
                hander.handle(sqlStatement, c!!)
                return
            } else {
                sqlStatement.accept(mySqlASTVisitor)
            }

            exception = null
        } catch (e: Exception) {//如果不是合法的mysql语句，就报错
            //            e.printStackTrace();
            exception = e
            //            return;
        }

        //druid支持的语句就用上面的方法语句处理，如果不支持，就会有异常，就自己写代码解析sql语句，处理。
        //下面是drop event语句的例子，这个例子druid不支持，所以自己写
        handleotherStatement(sql.sql, c!!)
    }

    private fun isupdatesql(sql: String): Boolean {
        val sqll = sql.toLowerCase()
        val list = Lists.newArrayList("delete", "drop", "create", "insert", "update")
        for (s in list) {
            if (sqll.contains(s)) {
                return true
            }
        }
        return false
    }

    override fun setConnection(connection: OConnection) {
        source = connection
        mySqlASTVisitor!!.setConnection(source!!)
    }

    private fun handleotherStatement(sql: String, c: OConnection) {
        if (AlterEvent.isme(sql)) {
            AlterEvent.handle(sql, c)
            return
        }

        if (AlterFunction.isme(sql)) {
            AlterFunction.handle(sql, c)
            return
        }
        if (AlterInstall.isme(sql)) {
            AlterInstall.handle(sql, c)
            return
        }
        if (AlterLOGfileGroup.isme(sql)) {
            AlterLOGfileGroup.handle(sql, c)
            return
        }
        if (AlterProcedure.isme(sql)) {
            AlterProcedure.handle(sql, c)
            return
        }
        if (AlterServer.isme(sql)) {
            AlterServer.handle(sql, c)
            return
        }
        if (AlterTableSpace.isme(sql)) {
            AlterTableSpace.handle(sql, c)
            return
        }
        if (CreateEvent.isme(sql)) {
            CreateEvent.handle(sql, c)
            return
        }
        if (CreateServer.isme(sql)) {
            CreateServer.handle(sql, c)
            return
        }
        if (CreateFunction.isme(sql)) {
            CreateFunction.handle(sql, c)
            return
        }
        if (CreateLogfilegroup.isme(sql)) {
            CreateLogfilegroup.handle(sql, c)
            return
        }
        if (CreateServer.isme(sql)) {
            CreateServer.handle(sql, c)
            return
        }
        if (CreateTableSpace.isme(sql)) {
            CreateTableSpace.handle(sql, c)
            return
        }
        if (DropEVENT.isdropevent(sql)) {//判断是不是dropevent语句
            DropEVENT.handle(sql, c)
            return
        }
        if (DropFunction.isme(sql)) {
            DropFunction.handle(sql, c)
            return
        }
        if (ExplainStatement.isme(sql, c)) {
            ExplainStatement.handle(sql, c)
            return
        }
        if (DropLOGFILEGROUP.isme(sql)) {
            DropLOGFILEGROUP.handle(sql, c)
            return
        }
        if (DropServer.isme(sql)) {
            DropServer.handle(sql, c)
            return
        }
        if (Mdo.isme(sql)) {
            Mdo.handle(sql, c)
            return
        }
        if (Mhandler.isme(sql)) {
            Mhandler.handle(sql, c)
            return
        }
        if (Msubquery.isme(sql)) {
            Msubquery.handle(sql, c)
            return
        }

        c.writeErrMessage(ErrorCode.ER_SP_BAD_SQLSTATE, if (exception == null) "不支持的语句！！！" else exception!!.message!!)
    }

    companion object {
        private val logger = LoggerFactory
                .getLogger(MysqlSQLhander::class.java)
    }

}
