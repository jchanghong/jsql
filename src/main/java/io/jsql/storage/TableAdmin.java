package io.jsql.storage;

import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlCreateTableStatement;
import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.orientechnologies.orient.core.record.impl.ODocument;

import java.util.*;

/**
 * Created by 长宏 on 2017/3/20 0020.
 * 表有关的功能
 */
public interface TableAdmin {
      void droptableSyn(String dbname, String table) throws MException ;
      void createtableSyn(String dbname, MySqlCreateTableStatement createTableStatement) throws MException ;
      Set<String> getalltable(String dbname) ;
      OClass gettableclass(String tablename, String db);
    List<ODocument> selectSyn(OClass oClass);
}
