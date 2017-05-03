package io.jsql.sql.handler;

import com.orientechnologies.orient.core.record.OElement;

import java.util.List;

/**
 * Created by 长宏 on 2017/5/3 0003.
 */
public class MyResultSet {
    public MyResultSet(List<OElement> data, List<String> columns) {
        this.data = data;
        this.columns = columns;
    }
    /**
     * The Data.数据对象包括所有的列
     */
    public List<OElement> data;
    /**
     * The Columns.需要显示的列
     */
    public List<String> columns;
}
