package io.jsql.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by 长宏 on 2017/4/30 0030.
 */
public class JDBC {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:9999/changhong?"
                + "user=root&password=0000&useUnicode=true&characterEncoding=UTF8";
        try {
            Connection connection = DriverManager.getConnection(url);
            System.out.println(connection.getAutoCommit());
            System.out.println(connection.getCatalog());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
