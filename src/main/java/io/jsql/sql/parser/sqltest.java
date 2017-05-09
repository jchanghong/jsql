package io.jsql.sql.parser;

import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.dialect.mysql.parser.MySqlStatementParser;

/**
 * Created by 长宏 on 2017/2/25 0025.
 * 测试
 */
public class sqltest {
    public static void main(String[] args) {
        String sql = "showdb tables;";
        MySqlStatementParser parser = new MySqlStatementParser(sql);
        SQLStatement mySqlStatement = parser.parseStatement();
        MSQLvisitor visitor = new MSQLvisitor();
        mySqlStatement.accept(visitor);

    }
}
