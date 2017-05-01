package io.jsql.storage;

import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.orientechnologies.orient.core.record.impl.ODocument;

import java.util.List;
import java.util.Set;

/**
 * Created by 长宏 on 2017/3/20 0020.
 * 和数据库有关的接口
 */
public abstract class DBAdmin {
  public static   String currentDB = null;
    /**
     * Deletedb syn.同步操作
     *
     * @param dbname the dbname
     * @throws MException the m exception
     */
    public abstract void deletedbSyn(String dbname) throws MException;
    abstract public void deletedbAyn(String dbname) ;
    abstract public void createdbSyn(String dbname) throws MException ;
    abstract public void createdbAsyn(String dbname);
    abstract public Set<String> getallDBs();
    abstract public Object exesqlforResult(String sql, String dbname) throws MException ;
    abstract public void exesqlNoResultAsyn(String sql, String dbname);
    abstract public List<ODocument> exequery(String sqlquery, String dbname) throws MException;

    abstract public ODatabaseDocumentTx getdb(String dbname);
}
