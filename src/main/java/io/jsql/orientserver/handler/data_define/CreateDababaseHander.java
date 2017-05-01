package io.jsql.orientserver.handler.data_define;

import com.alibaba.druid.sql.ast.statement.SQLCreateDatabaseStatement;
import io.jsql.orientstorage.sqlhander.sqlutil.MSQLutil;
import io.jsql.orientserver.OConnection;
import io.jsql.storage.MException;

/**
 * Created by 长宏 on 2017/2/25 0025.
 * CREATE {DATABASE | SCHEMA} [IF NOT EXISTS] db_name
 * [create_specification] ...
 * <p>
 * create_specification:
 * [DEFAULT] CHARACTER SET [=] charset_name
 * | [DEFAULT] COLLATE [=] collation_name
 * CREATE DATABASE creates a database with the given name.
 * To use this statement, you need the CREATE privilege for the database.
 * CREATE SCHEMA is a synonym for CREATE DATABASE.
 */
public class CreateDababaseHander {
    public static void handle(SQLCreateDatabaseStatement createDatabaseStatement, OConnection c) {
            OConnection.DB_ADMIN.createdbAsyn(MSQLutil.getdbname(createDatabaseStatement));
            c.writeok();
    }
}
