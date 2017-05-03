package io.jsql.storage;

import com.orientechnologies.orient.core.db.document.ODatabaseDocument;
import com.orientechnologies.orient.core.record.OElement;
import com.orientechnologies.orient.core.sql.executor.OResult;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Created by 长宏 on 2017/3/20 0020.
 * 和数据库有关的接口
 */
public interface  DB {
//  public static   String currentDB = null;
    /**
     * Deletedb syn.同步操作
     *
     * @param dbname the dbname
     * @throws StorageException the m exception
     */
    public abstract void deletedbSyn(String dbname) throws StorageException;
    abstract public void deletedbAyn(String dbname) ;
    abstract public void createdbSyn(String dbname) throws StorageException;
    abstract public void createdbAsyn(String dbname);
    abstract public List<String> getallDBs() throws StorageException;
    abstract public Optional<OResult> exesqlforResult(String sql, String dbname) throws StorageException;
    abstract public void exesqlNoResultAsyn(String sql, String dbname);
    abstract public Stream<OElement> query(String sqlquery, String dbname) throws StorageException;
    abstract public ODatabaseDocument getdb(String dbname) throws StorageException;

    abstract public void close();
}
