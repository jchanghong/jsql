/*
 * Java-based distributed database like Mysql
 */

package io.jsql.sql.handler.utilstatement;

import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlDescribeStatement;
import com.orientechnologies.orient.core.db.document.ODatabaseDocument;
import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.orientechnologies.orient.core.record.OElement;
import com.orientechnologies.orient.core.record.impl.ODocument;
import io.jsql.sql.OConnection;
import io.jsql.sql.handler.MyResultSet;
import io.jsql.sql.handler.SqlStatementHander;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: jiang
 * \* Date: 2017/8/2 0002
 * \* Time: 21:02
 * \
 */
@Component
public class MySqlDescribeStatement2 extends SqlStatementHander {
    private static final Logger LOGGER = LoggerFactory.getLogger(MySqlDescribeStatement2.class);
    @NotNull
    @Override
    public Class<? extends SQLStatement> supportSQLstatement() {
        return MySqlDescribeStatement.class;
    }
    @Nullable
    @Override
    protected Object handle0(@NotNull SQLStatement sqlStatement, @NotNull OConnection c) throws Exception {
        MySqlDescribeStatement describeStatement = (MySqlDescribeStatement) sqlStatement;
        LOGGER.info(describeStatement.getClass().getName());
        ODatabaseDocument documentTx = OConnection.DB_ADMIN.getdb(c.getSchema());
        documentTx.activateOnCurrentThread();
        OClass oClass = documentTx.getClass(describeStatement.getObject().getSimpleName());
        if (oClass == null) {
            return "表不存在";
        }
        List<OElement> elements = new ArrayList<>();
        oClass.properties().forEach(a -> {
            ODocument element = new ODocument();
            element.setProperty("Field", a.getName());
            element.setProperty("Type", a.getType());
            element.setProperty("Null", a.isNotNull());
            element.setProperty("Key", " ");
            element.setProperty("Default", a.getDefaultValue());
            element.setProperty("Extra", " ");
            elements.add(element);
        });
        String[] list=new String[]{"Field", "Type", "Null", "Key", "Default", "Extra"};
        return new MyResultSet(elements, Arrays.asList(list));
    }
}
