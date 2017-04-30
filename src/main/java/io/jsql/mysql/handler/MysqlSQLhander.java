package io.jsql.mysql.handler;

import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.dialect.mysql.parser.MySqlStatementParser;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
import io.jsql.config.ErrorCode;
import io.jsql.orientserver.OConnection;
import io.jsql.orientserver.handler.data_define.*;
import io.jsql.orientserver.handler.data_mannipulation.Mdo;
import io.jsql.orientserver.handler.data_mannipulation.Mhandler;
import io.jsql.orientserver.handler.data_mannipulation.Msubquery;
import io.jsql.orientserver.handler.utilstatement.ExplainStatement;
import io.jsql.orientserver.parser.MSQLvisitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by 长宏 on 2017/4/30 0030.
 */
public class MysqlSQLhander implements SQLHander{
    private static final Logger LOGGER = LoggerFactory
            .getLogger(MysqlSQLhander.class);

    private final OConnection source;
    protected Boolean readOnly;
    private Exception exception;
    private MySqlASTVisitor mySqlASTVisitor;

    public MysqlSQLhander(OConnection source) {
        this.source = source;
        mySqlASTVisitor = new MSQLvisitor(source);
    }
    public void setReadOnly(Boolean readOnly) {
        this.readOnly = readOnly;
    }
    @Override
    public void handle(String sql) {
        System.out.println(sql);
//        System.out.println(Thread.currentThread().getName());
        OConnection c = this.source;
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(new StringBuilder().append(c).append(sql).toString());
        }
        List<SQLStatement> lists;
        try {
            MySqlStatementParser parser = new MySqlStatementParser(sql);
            lists = parser.parseStatementList();
            lists.forEach(statement -> statement.accept(mySqlASTVisitor));
            if (lists != null&&lists.size()>0) {
                exception = null;
                return;
            }
        } catch (Exception e) {//如果不是合法的mysql语句，就报错
//            e.printStackTrace();
            exception = e;
//            return;
        }
        //druid支持的语句就用上面的方法语句处理，如果不支持，就会有异常，就自己写代码解析sql语句，处理。
        //下面是drop event语句的例子，这个例子druid不支持，所以自己写
        handleotherStatement(sql, c);
    }

    private void handleotherStatement(String sql, OConnection c) {
        if (AlterEvent.isme(sql)) {
            AlterEvent.handle(sql, c);
            return;
        }

        if (AlterFunction.isme(sql)) {
            AlterFunction.handle(sql, c);
            return;
        }
        if (AlterInstall.isme(sql)) {
            AlterInstall.handle(sql, c);
            return;
        }
        if (AlterLOGfileGroup.isme(sql)) {
            AlterLOGfileGroup.handle(sql, c);
            return;
        }
        if (AlterProcedure.isme(sql)) {
            AlterProcedure.handle(sql, c);
            return;
        }
        if (AlterServer.isme(sql)) {
            AlterServer.handle(sql, c);
            return;
        }
        if (AlterTableSpace.isme(sql)) {
            AlterTableSpace.handle(sql, c);
            return;
        }
        if (CreateEvent.isme(sql)) {
            CreateEvent.handle(sql, c);
            return;
        }
        if (CreateServer.isme(sql)) {
            CreateServer.handle(sql, c);
            return;
        }
        if (CreateFunction.isme(sql)) {
            CreateFunction.handle(sql, c);
            return;
        }
        if (CreateLogfilegroup.isme(sql)) {
            CreateLogfilegroup.handle(sql, c);
            return;
        }
        if (CreateServer.isme(sql)) {
            CreateServer.handle(sql, c);
            return;
        }
        if (CreateTableSpace.isme(sql)) {
            CreateTableSpace.handle(sql, c);
            return;
        }
        if (DropEVENT.isdropevent(sql)) {//判断是不是dropevent语句
            DropEVENT.handle(sql,c);
            return;
        }
        if (DropFunction.isme(sql)) {
            DropFunction.handle(sql, c);
            return;
        }
        if(ExplainStatement.isme(sql,c)){
            ExplainStatement.handle(sql,c);
            return;
        }
        if (DropLOGFILEGROUP.isme(sql)) {
            DropLOGFILEGROUP.handle(sql, c);
            return;
        }
        if (DropServer.isme(sql)) {
            DropServer.handle(sql, c);
            return;
        }
        if (Mdo.isme(sql)) {
            Mdo.handle(sql, c);
            return;
        }
        if (Mhandler.isme(sql)) {
            Mhandler.handle(sql, c);
            return;
        }
        if (Msubquery.isme(sql)) {
            Msubquery.handle(sql, c);
            return;
        }

        c.writeErrMessage(ErrorCode.ER_SP_BAD_SQLSTATE, exception == null ? "不支持的语句！！！" : exception.getMessage());
    }

}
