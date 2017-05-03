package io.jsql.sql.handler;

import com.alibaba.druid.sql.ast.SQLStatement;
import com.orientechnologies.orient.core.record.OElement;
import io.jsql.sql.OConnection;

import java.util.List;

/**
 * Created by 长宏 on 2017/5/3 0003.
 * 所有的sql语句处理器必须是这个类的子类.
 * 可以参考 data_mannipulation包下面的类。模仿实现。。。。。
 * 比如Mupdate。Mselect
 */
abstract public class SqlStatementHander {
    protected OConnection c;
    /**
     * Support sq lstatement class.
     *
     * @return the class支持的sql语句类型的class
     */
    abstract public Class<? extends SQLStatement> supportSQLstatement();

    /**
     * Handle.
     *
     * @param sqlStatement the sql statement
     * @return the object 返回值只有4种可能，不然报错！！！
     * 一种是long类型， 一种是MyResultSet 一种是null ,一种是string表示错误的消息
     * 返回其他对面都是错误的
     * @throws Exception the exception
     */
    abstract protected Object handle(SQLStatement sqlStatement) throws Exception;

    /**
     * Handle.
     *
     * @param sqlStatement the sql statement
     * @param connection   the connection
     */
    public void handle(SQLStatement sqlStatement, OConnection connection) {
        this.c = connection;
        try {
            Object object = handle(sqlStatement);
            if (object == null) {
                connection.writeok();
            } else if (object instanceof MyResultSet) {
                MyResultSet data = (MyResultSet) object;
                onsuccess(data.data, data.columns, connection);
            } else if (object instanceof Long) {
                long l = (long) object;
                onsuccess(l, connection);
            } else if (object instanceof String) {
                connection.writeErrMessage(object.toString());
            } else {
                onsuccess(connection, object);
//                onerror(new StorageException("error"), connection);
            }
        } catch (Exception e) {
            e.printStackTrace();
            onerror(e, connection);
        }
    }

    private void onsuccess(OConnection connection, Object object) {
        if (object instanceof Number) {
            onsuccess((Long) object, connection);
        }
        else {
            connection.writeok();
        }
    }

    /**
     * Onsuccess.如果是更新语句，可以调用这个函数
     *
     * @param influences_rows the influences rows
     * @param connection      the connection
     */
    private void onsuccess(long influences_rows, OConnection connection) {
        DefaultHander.onseccess(influences_rows, connection);
    }

    /**
     * Onerror.
     *
     * @param e          the e
     * @param connection the connection
     */
    private void onerror(Exception e, OConnection connection) {
        DefaultHander.onerror(e, connection);
    }

    /**
     * Onsuccess.如果有返回结果集。可以直接用这个函数。减少重复代码，
     *
     * @param data       the data
     * @param columns
     * @param connection the connection
     */
    private void onsuccess(List<OElement> data, List<String> columns, OConnection connection) {
        DefaultHander.onseccess(data, columns, connection);
    }
}
