package io.jsql.sql.handler.utilstatement;

import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.SQLExplainStatement;
import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlSelectQueryBlock;
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
public class ExplainStatement2 extends SqlStatementHander {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExplainStatement2.class);
    @NotNull
    @Override
    public Class<? extends SQLStatement> supportSQLstatement() {
        return SQLExplainStatement.class;
    }

    @Nullable
    @Override
    protected Object handle0(@NotNull SQLStatement sqlStatement, @NotNull OConnection c) throws Exception {
        SQLExplainStatement explainStatement = (SQLExplainStatement) sqlStatement;
        SQLSelectStatement statement = (SQLSelectStatement) explainStatement.getStatement();//得到explain后面的语句
        LOGGER.info(statement.toString());
        MySqlSelectQueryBlock tablename = (MySqlSelectQueryBlock) statement.getSelect().getQuery();
        ODatabaseDocument documentTx = OConnection.DB_ADMIN.getdb(c.getSchema());
        documentTx.activateOnCurrentThread();
        OClass oClass = documentTx.getClass(tablename.getFrom().toString().trim());
        if (oClass == null) {
            return null;
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
