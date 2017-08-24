package io.jsql.test;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.dialect.mysql.parser.MySqlStatementParser;

import java.util.List;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: jiang
 * \* Date: 2017/8/2 0002
 * \* Time: 20:26
 * \
 */
public class Druid {
    public static void main(String[] args) {
        String sql1 = "ALTER TABLE t2 DROP COLUMN c, DROP COLUMN d;";
//        String sql="DESCRIBE City;";
        String sql="EXPLAIN select * from t1;";
        List<SQLStatement> list = SQLUtils.parseStatements(sql, "mysql");

        for (SQLStatement sqlStatement:list
                ) {
            System.out.println(sqlStatement.getClass().getName());

        }
        System.out.println("----------");
        MySqlStatementParser parser = new MySqlStatementParser(sql);
        System.out.println(parser.parseSpStatement().getClass().getName());
    }
}
