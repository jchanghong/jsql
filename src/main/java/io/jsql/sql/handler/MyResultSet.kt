package io.jsql.sql.handler

import com.orientechnologies.orient.core.record.OElement

/**
 * Created by 长宏 on 2017/5/3 0003.
 */
class MyResultSet(
        /**
         * The Data.数据对象包括所有的列
         */
        var data: List<OElement>,
        /**
         * The Columns.需要显示的列
         */
        var columns: List<String>)
