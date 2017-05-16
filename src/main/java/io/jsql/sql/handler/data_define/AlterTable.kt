package io.jsql.sql.handler.data_define

import com.alibaba.druid.sql.ast.SQLStatement
import com.alibaba.druid.sql.ast.statement.SQLAlterTableStatement
import io.jsql.sql.OConnection
import io.jsql.sql.handler.SqlStatementHander
import org.springframework.stereotype.Component

/**
 * Created by 长宏 on 2017/3/18 0018.
 * ALTER TABLE t2 DROP COLUMN c, DROP COLUMN d;
 */
@Component
class AlterTable : SqlStatementHander() {

    override fun supportSQLstatement(): Class<out SQLStatement> {
        return SQLAlterTableStatement::class.java
    }

    @Throws(Exception::class)
    override fun handle0(sqlStatement: SQLStatement, c: OConnection): Any? {
        return null
    }

    companion object {
        fun handle(x: SQLAlterTableStatement, connection: OConnection) {

            connection.writeok()

        }
    }
}
