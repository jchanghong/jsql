/*
 * Copyright (c) 2013, OpenCloudDB/MyCAT and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software;Designed and Developed mainly by many Chinese 
 * opensource volunteers. you can redistribute it and/or modify it under the 
 * terms of the GNU General Public License version 2 only, as published by the
 * Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 * 
 * Any questions about this component can be directed to it's project Web address 
 * https://code.google.com/p/opencloudb/.
 *
 */
package io.jsql.sql.handler.data_mannipulation;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOpExpr;
import com.alibaba.druid.sql.ast.statement.SQLSelectItem;
import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlSelectQueryBlock;
import com.orientechnologies.orient.core.record.OElement;
import com.orientechnologies.orient.core.record.impl.ODocument;
import io.jsql.orientstorage.constant.Minformation_schama;
import io.jsql.orientstorage.constant.MvariableTable;
import io.jsql.sql.OConnection;
import io.jsql.sql.handler.MyResultSet;
import io.jsql.sql.handler.SqlStatementHander;
import io.jsql.sql.response.*;
import io.jsql.sql.util.Mcomputer;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author changhong
 *         select 语句有些是selct table from,,,,,，
 *         select 6+1;
 *         select dabase();
 *         有些比如是select suer（）；
 */
@Component
public final class MSelectHandler extends SqlStatementHander{


    @Override
    public Class<? extends SQLStatement> supportSQLstatement() {
        return SQLSelectStatement.class;
    }

    @Override
    protected Object handle(SQLStatement sqlStatement) throws Exception {
        SQLSelectStatement selectStatement = (SQLSelectStatement) sqlStatement;
        MySqlSelectQueryBlock queryBlock = (MySqlSelectQueryBlock) selectStatement.getSelect().getQuery();
        if (queryBlock.getFrom() != null) {
            return MorientSelectResponse.getdata(selectStatement,c);
        }
        SQLSelectItem selectItem = queryBlock.getSelectList().get(0);
        boolean number = true;
        try {
            Double.parseDouble(selectItem.toString());
        } catch (Exception e) {
//            e.printStackTrace();
            number = false;
        }
        if (number) {
            OElement element = new ODocument();
            element.setProperty(selectItem.toString(), selectItem.toString());
            List<OElement> ts = Collections.singletonList(element);
            List<String> singleton = Collections.singletonList(selectItem.toString());
            MyResultSet resultSet = new MyResultSet(ts, singleton);
            return resultSet;
        }
        if (selectItem.getExpr() instanceof SQLBinaryOpExpr) {
         return    handleopexpr((SQLBinaryOpExpr) selectItem.getExpr());
        }

        SQLExpr sqlExpr = selectItem.getExpr();
        if (selectStatement.toString().contains("@") && selectStatement.toString().contains("AS")) {
            List<String> column = MselectVariables.getcolumn(selectStatement);
            List<OElement> value = MselectVariables.getbs(selectStatement, column);
            MyResultSet resultSet = new MyResultSet(value, column);
            return resultSet;
        }
        String what = sqlExpr.toString().toUpperCase();
        if (what.contains("VERSION_COMMENT")) {
            return MSelectVersionComment.getdata();
        }
        if (what.contains("DATABASE")) {
            return MSelectDatabase.getdata(c);
        }
        if (what.contains("CONNECTION_ID")) {
            return MSelectConnnectID.getdata();

        }
        if (what.contains("USER")) {
         return    MSelectUser.getdata();
        }
        if (what.contains("VERSION")) {
         return    MSelectVersion.getdata();

        }
        //SESSION_INCREMENT
        if (what.contains("INCREMENT")) {
         return    MSessionIncrement.getdata();
        }
        //SESSION_ISOLATION
        if (what.contains("ISOLATION")) {
         return    SessionIsolation.getdata();
        }
        if (what.contains("LAST_INSERT_ID")) {
            return MSelectLastInsertId.getdata();
        }
        if (what.contains("IDENTITY")) {
            return MSelectIdentity.getdata();
        }
        if (what.contains("SELECT_VAR_ALL")) {
        return     ShowVariables.getdata();


        }
        //SESSION_TX_READ_ONLY
        if (what.contains("TX_READ_ONLY")) {
        return     MSelectTxReadOnly.getdata();
        }
        what = sqlExpr.toString();
        if (what.contains("@@")) {
            int index = what.indexOf(".");
            if (index != -1) {
                what = what.substring(index + 1);
                what = "select value from " + MvariableTable.tablename + "  where Variable_name='" + what + "';";
                    Stream<OElement> documents = OConnection.DB_ADMIN.query(what, Minformation_schama.dbname);
                    List<String> column = Collections.singletonList("value");
                    return new MyResultSet(documents.collect(Collectors.toList()), column);
//                    MSelect1Response.response(c, what, Arrays.asList(documents.findAny().get().getProperty("value")));

            }
        }
        return null;
    }
//     private void handle(SQLSelectStatement selectStatement, OConnection c) {
//
//        MySqlSelectQueryBlock queryBlock = (MySqlSelectQueryBlock) selectStatement.getSelect().getQuery();
//        if (queryBlock.getFrom() != null) {
//            MorientSelectResponse.responseselect(c, selectStatement.toString(), selectStatement);
//            return;
//        }
//        SQLSelectItem selectItem = queryBlock.getSelectList().get(0);
//        boolean number = true;
//        try {
//            Double.parseDouble(selectItem.toString());
//        } catch (Exception e) {
////            e.printStackTrace();
//            number = false;
//        }
//        if (number) {
//            MSelect1Response.response(c, selectItem.toString(), Collections.singletonList(selectItem.toString()));
//            return;
//        }
//        if (selectItem.getExpr() instanceof SQLBinaryOpExpr) {
////            return
////            handleopexpr((SQLBinaryOpExpr) selectItem.getExpr());
//
//        }
//
//        SQLExpr sqlExpr = selectItem.getExpr();
////        if (selectStatement.toString().contains("@") && selectStatement.toString().contains("AS")) {
////            List<String> column = selectVariables.getcolumn(selectStatement);
////            List<String> value = selectVariables.getbs(selectStatement, column);
////            MselectNResponse.response(c, column, value);
////            return;
////        }
//        String what = sqlExpr.toString().toUpperCase();
//        if (what.contains("VERSION_COMMENT")) {
//            MSelectVersionComment.response(c);
//            return;
//        }
//        if (what.contains("DATABASE")) {
//            MSelectDatabase.response(c);
//            return;
//        }
//        if (what.contains("CONNECTION_ID")) {
//            MSelectConnnectID.response(c);
//            return;
//        }
//        if (what.contains("USER")) {
//            MSelectUser.response(c);
//            return;
//        }
//        if (what.contains("VERSION")) {
//            MSelectVersion.response(c);
//            return;
//        }
//        //SESSION_INCREMENT
//        if (what.contains("INCREMENT")) {
//            MSessionIncrement.response(c);
//            return;
//        }
//        //SESSION_ISOLATION
//        if (what.contains("ISOLATION")) {
//            SessionIsolation.response(c);
//            return;
//        }
//        if (what.contains("LAST_INSERT_ID")) {
//            MSelectLastInsertId.response(c, selectStatement.toString(), 1);
//            return;
//        }
//        if (what.contains("IDENTITY")) {
//            MSelectIdentity.response(c, selectStatement.toString(), 1, "mysql");
//            return;
//        }
//        if (what.contains("SELECT_VAR_ALL")) {
//            ShowVariables.response(c);
//            return;
//
//        }
//        //SESSION_TX_READ_ONLY
//        if (what.contains("TX_READ_ONLY")) {
//            MSelectTxReadOnly.response(c);
//            return;
//        }
//        what = sqlExpr.toString();
//        if (what.contains("@@")) {
//            int index = what.indexOf(".");
//            if (index != -1) {
//                what = what.substring(index + 1);
//                what = "select value from " + MvariableTable.tablename + "  where Variable_name='" + what + "';";
//                try {
//                    Stream<OElement> documents = OConnection.DB_ADMIN.query(what, Minformation_schama.dbname);
//                    MSelect1Response.response(c, what, Arrays.asList(documents.findAny().get().getProperty("value")));
//                    return;
//                } catch (StorageException e) {
//                    e.printStackTrace();
//                    c.writeErrMessage(e.getMessage());
//                    return;
//                }
//            }
//        }
//        c.writeNotSurrport();
//    }

    private static Object handleopexpr(SQLBinaryOpExpr sqlBinaryOpExpr) {
        String columnname = sqlBinaryOpExpr.toString();
        OElement element = new ODocument();
        element.setProperty(columnname, Mcomputer.compute(columnname) + "");
        return new MyResultSet(Collections.singletonList(element), Collections.singletonList(columnname));
    }


}
