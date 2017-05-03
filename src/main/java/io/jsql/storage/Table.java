package io.jsql.storage;

import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlCreateTableStatement;
import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.orientechnologies.orient.core.record.OElement;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.orientechnologies.orient.core.sql.executor.OResult;

import java.util.*;
import java.util.stream.Stream;

/**
 * Created by 长宏 on 2017/3/20 0020.
 * 表有关的功能
 */
public interface Table {
      void droptableSyn(String dbname, String table) throws StorageException;
      void createtableSyn(String dbname, MySqlCreateTableStatement createTableStatement) throws StorageException;
      List<String> getalltable(String dbname) throws StorageException;
      OClass gettableclass(String tablename, String db) throws StorageException;
    Stream<OElement> selectSyn(OClass oClass,String dbname) throws StorageException;
}
