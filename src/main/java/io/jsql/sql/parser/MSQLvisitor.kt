package io.jsql.sql.parser

import com.alibaba.druid.sql.ast.*
import com.alibaba.druid.sql.ast.statement.*
import com.alibaba.druid.sql.dialect.mysql.ast.*
import com.alibaba.druid.sql.dialect.mysql.ast.clause.*
import com.alibaba.druid.sql.dialect.mysql.ast.expr.MySqlUserName
import com.alibaba.druid.sql.dialect.mysql.ast.statement.*
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitorAdapter
import io.jsql.sql.OConnection
import io.jsql.sql.handler.adminstatement.MSetHandler
import io.jsql.sql.handler.adminstatement.ShowHandler
import io.jsql.sql.handler.data_define.*
import io.jsql.sql.handler.data_mannipulation.*
import io.jsql.sql.handler.utilstatement.HelpStatement
import io.jsql.sql.handler.utilstatement.Usedatabase
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component

/**
 * Created by 长宏 on 2017/3/19 0019.
 * 不再使用
 */
class MSQLvisitor : MySqlASTVisitorAdapter() {
    private var connection: OConnection? = null

    fun setconnection(connection: OConnection) {
        this.connection = connection
    }


    init {
        this.connection = connection
    }

    override fun visit(x: MySqlSelectQueryBlock.Limit?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: MySqlTableIndex?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: MySqlKey?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: MySqlPrimaryKey?): Boolean {
        connection!!!!.writeok()
        return false
    }


    override fun visit(x: MySqlPrepareStatement?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: MySqlExecuteStatement?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: MysqlDeallocatePrepareStatement?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: MySqlDeleteStatement?): Boolean {
        Mdelete.handle(x!!, connection!!!!)
        return false
    }

    override fun visit(x: MySqlInsertStatement?): Boolean {
        if (x != null) {
            Minsert.handle(x!!, connection!!!!)
        }
        return false
    }

    override fun visit(x: MySqlLoadDataInFileStatement?): Boolean {
        MloaddataINfile.handle(x!!, connection!!!!)
        return false
    }

    override fun visit(x: MySqlLoadXmlStatement?): Boolean {
        Mloadxml.handle(x!!, connection!!!!)
        return false
    }

    override fun visit(x: MySqlReplaceStatement?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: MySqlStartTransactionStatement?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: MySqlCommitStatement?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: MySqlRollbackStatement?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: MySqlShowColumnsStatement?): Boolean {
        ShowHandler.showcolumn(x!!, connection!!!!)
        return false
    }

    override fun visit(x: MySqlShowDatabasesStatement?): Boolean {
        ShowHandler.showdatabase(x!!, connection!!!!)
        return false
    }

    override fun visit(x: MySqlShowWarningsStatement?): Boolean {
        ShowHandler.showwarnings(x!!, connection!!!!)
        return false
    }

    override fun visit(x: MySqlShowStatusStatement?): Boolean {
        if (x != null) {
            ShowHandler.showstatus(x!!, connection!!!!)
        }
        return false
    }

    override fun visit(x: CobarShowStatus?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: MySqlKillStatement?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: MySqlBinlogStatement?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: MySqlResetStatement?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: MySqlCreateUserStatement?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: MySqlCreateUserStatement.UserSpecification?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: MySqlPartitionByKey?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: MySqlSelectQueryBlock?): Boolean {
        connection!!!!.writeok()
        return false
    }


    override fun visit(x: MySqlDescribeStatement?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: MySqlUpdateStatement?): Boolean {
        Mupdate.handle(x!!, connection!!!!)
        return false
    }

    override fun visit(x: MySqlSetTransactionStatement?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: MySqlSetNamesStatement?): Boolean {

        connection!!!!.writeok()
        return false
    }

    override fun visit(x: MySqlSetCharSetStatement?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: MySqlShowAuthorsStatement?): Boolean {
        ShowHandler.showauthors(x!!, connection!!!!)
        return false
    }

    override fun visit(x: MySqlShowBinaryLogsStatement?): Boolean {
        ShowHandler.showbinarylogs(x!!, connection!!!!)
        return false
    }

    override fun visit(x: MySqlShowMasterLogsStatement?): Boolean {
        ShowHandler.showmasterlogs(x!!, connection!!)
        return false
    }

    override fun visit(x: MySqlShowCollationStatement?): Boolean {
        ShowHandler.showcollation(x!!, connection!!!!)
        return false
    }

    override fun visit(x: MySqlShowBinLogEventsStatement?): Boolean {
        ShowHandler.showbinlogevent(x!!, connection!!!!)
        return false
    }

    override fun visit(x: MySqlShowCharacterSetStatement?): Boolean {
        ShowHandler.showcharater(x!!, connection!!!!)
        return false
    }

    override fun visit(x: MySqlShowContributorsStatement?): Boolean {
        ShowHandler.showcontributors(x!!, connection!!!!)

        return false
    }

    override fun visit(x: MySqlShowCreateDatabaseStatement?): Boolean {
        ShowHandler.showcreatedatabase(x!!, connection!!!!)
        return false
    }

    override fun visit(x: MySqlShowCreateEventStatement?): Boolean {
        ShowHandler.showcreateevent(x!!, connection!!!!)
        return false
    }

    override fun visit(x: MySqlShowCreateFunctionStatement?): Boolean {
        ShowHandler.showcreatefunction(x!!, connection!!!!)
        return false
    }

    override fun visit(x: MySqlShowCreateProcedureStatement?): Boolean {
        if (x != null) {
            ShowHandler.showcreateproccedure(x!!, connection!!!!)
        }
        return false
    }

    override fun visit(x: MySqlShowCreateTableStatement?): Boolean {
        ShowHandler.showcreatetable(x!!, connection!!!!)
        return false
    }

    override fun visit(x: MySqlShowCreateTriggerStatement?): Boolean {
        ShowHandler.showcreatetrigger(x!!, connection!!!!)
        return false
    }

    override fun visit(x: MySqlShowCreateViewStatement?): Boolean {
        ShowHandler.showcreateview(x!!, connection!!!!)
        return false
    }

    override fun visit(x: MySqlShowEngineStatement?): Boolean {
        ShowHandler.showengine(x!!, connection!!!!)
        return false
    }

    override fun visit(x: MySqlShowEnginesStatement?): Boolean {
        ShowHandler.showengines(x!!, connection!!!!)
        return false
    }

    override fun visit(x: MySqlShowErrorsStatement?): Boolean {
        ShowHandler.showerrors(x!!, connection!!!!)
        return false
    }

    override fun visit(x: MySqlShowEventsStatement?): Boolean {
        ShowHandler.showevents(x!!, connection!!!!)
        return false
    }

    override fun visit(x: MySqlShowFunctionCodeStatement?): Boolean {
        ShowHandler.showfunctioncode(x!!, connection!!!!)
        return false
    }

    override fun visit(x: MySqlShowFunctionStatusStatement?): Boolean {
        ShowHandler.showfunctionstatus(x!!, connection!!!!)
        return false
    }

    override fun visit(x: MySqlShowGrantsStatement?): Boolean {
        ShowHandler.showgrants(x!!, connection!!!!)

        return false
    }

    override fun visit(x: MySqlUserName?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: MySqlShowIndexesStatement?): Boolean {
        ShowHandler.showindexs(x!!, connection!!!!)
        return false
    }

    override fun visit(x: MySqlShowKeysStatement?): Boolean {
        ShowHandler.showkeys(x!!, connection!!!!)
        return false
    }

    override fun visit(x: MySqlShowMasterStatusStatement?): Boolean {
        ShowHandler.showmasterstatus(x!!, connection!!!!)
        return false
    }

    override fun visit(x: MySqlShowOpenTablesStatement?): Boolean {
        ShowHandler.showopentable(x!!, connection!!!!)
        return false
    }

    override fun visit(x: MySqlShowPluginsStatement?): Boolean {
        ShowHandler.showplugins(x!!, connection!!!!)
        return false
    }

    override fun visit(x: MySqlShowPrivilegesStatement?): Boolean {
        ShowHandler.showprivileges(x!!, connection!!!!)
        return false
    }

    override fun visit(x: MySqlShowProcedureCodeStatement?): Boolean {
        ShowHandler.showprovedureode(x!!, connection!!!!)
        return false
    }

    override fun visit(x: MySqlShowProcedureStatusStatement?): Boolean {
        ShowHandler.showproedurestatus(x!!, connection!!!!)
        return false
    }

    override fun visit(x: MySqlShowProcessListStatement?): Boolean {
        ShowHandler.showprocesslist(x!!, connection!!!!)
        return false
    }

    override fun visit(x: MySqlShowProfileStatement?): Boolean {
        ShowHandler.showprofile(x!!, connection!!)
        return false
    }

    override fun visit(x: MySqlShowProfilesStatement?): Boolean {
        ShowHandler.showprofiles(x!!, connection!!)
        return false
    }

    override fun visit(x: MySqlShowRelayLogEventsStatement?): Boolean {
        ShowHandler.showrelaylog(x!!, connection!!)
        return false
    }

    override fun visit(x: MySqlShowSlaveHostsStatement?): Boolean {
        ShowHandler.showslavehosts(x!!, connection!!)
        return false
    }

    override fun visit(x: MySqlShowSlaveStatusStatement?): Boolean {
        ShowHandler.showslavestatus(x!!, connection!!)
        return false
    }

    override fun visit(x: MySqlShowTableStatusStatement?): Boolean {
        ShowHandler.showtablestatus(x!!, connection!!)
        return false
    }

    override fun visit(x: MySqlShowTriggersStatement?): Boolean {
        ShowHandler.showtriggers(x!!, connection!!)
        return false
    }

    /*SHOW VARIABLES WHERE Variable_name = 'language'
OR Variable_name = 'net_write_timeout'
OR Variable_name = 'interactive_timeout'
OR Variable_name = 'wait_timeout'
OR Variable_name = 'character_set_client'
OR Variable_name = 'character_set_connection!!'
OR Variable_name = 'character_set'
OR Variable_name = 'character_set_server'
OR Variable_name = 'tx_isolation'
OR Variable_name = 'transaction_isolation'
OR Variable_name = 'character_set_results'
OR Variable_name = 'timezone'
OR Variable_name = 'time_zone'
OR Variable_name = 'system_time_zone'
OR Variable_name = 'lower_case_table_names'
OR Variable_name = 'max_allowed_packet'
OR Variable_name = 'net_buffer_length'
OR Variable_name = 'sql_mode'
OR Variable_name = 'query_cache_type'
OR Variable_name = 'query_cache_size'
OR Variable_name = 'license'
OR Variable_name = 'init_connect'*/
    override fun visit(x: MySqlShowVariantsStatement?): Boolean {
        ShowHandler.showvatiants(x!!, connection!!)
        return false
    }

    override fun visit(x: MySqlRenameTableStatement.Item?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: MySqlRenameTableStatement?): Boolean {
        RenameTable.handle(x!!, connection!!)
        return false
    }

    override fun visit(x: MySqlUnionQuery?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: MySqlUseIndexHint?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: MySqlIgnoreIndexHint?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: MySqlLockTableStatement?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: MySqlUnlockTablesStatement?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: MySqlForceIndexHint?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: MySqlAlterTableChangeColumn?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: MySqlAlterTableCharacter?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: MySqlAlterTableOption?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: MySqlCreateTableStatement?): Boolean {
        CreateTable.handle(x!!, connection!!)
        return false
    }

    override fun visit(x: MySqlHelpStatement?): Boolean {
        HelpStatement.handle(x!!, connection!!)
        return false
    }


    override fun visit(x: MySqlUnique?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: MysqlForeignKey?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: MySqlAlterTableModifyColumn?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: MySqlAlterTableDiscardTablespace?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: MySqlAlterTableImportTablespace?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: MySqlCreateTableStatement.TableSpaceOption?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: MySqlAnalyzeStatement?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: MySqlAlterUserStatement?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: MySqlOptimizeStatement?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: MySqlSetPasswordStatement?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: MySqlHintStatement?): Boolean {
        connection!!!!.writeok()
        return false
    }


    override fun visit(x: MySqlWhileStatement?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: MySqlCaseStatement?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: MySqlDeclareStatement?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: MySqlSelectIntoStatement?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: MySqlCaseStatement.MySqlWhenStatement?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: MySqlLeaveStatement?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: MySqlIterateStatement?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: MySqlRepeatStatement?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: MySqlCursorDeclareStatement?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: MySqlUpdateTableSource?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: MySqlAlterTableAlterColumn?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: MySqlSubPartitionByKey?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: MySqlSubPartitionByList?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: MySqlDeclareHandlerStatement?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: MySqlDeclareConditionStatement?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun endVisit(x: CobarShowStatus?) {
        super.endVisit(x)
    }


    override fun visit(astNode: SQLSelectStatement?): Boolean {
        //        Oconnection!!.sqlStatementHander = new MSelectHandler();
        //        Oconnection!!.sqlStatementHander.handle0(astNode, connection!!);
        connection!!!!.writeok()

        return false
    }

    override fun visit(x: SQLCreateTableStatement?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: SQLColumnDefinition?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: SQLColumnDefinition.Identity?): Boolean {
        connection!!!!.writeok()
        return false
    }


    override fun visit(x: SQLCreateViewStatement?): Boolean {
        CreateView.handle(x!!, connection!!)
        return false
    }

    override fun visit(x: SQLCreateViewStatement.Column?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: SQLAssignItem?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: SQLCallStatement?): Boolean {
        Mcall.handle(x!!, connection!!)
        return false
    }

    override fun visit(x: SQLCommentStatement?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: SQLAlterTableAddColumn?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: SQLAlterTableDropColumnItem?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: SQLCommentHint?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: SQLCreateDatabaseStatement?): Boolean {
        CreateDababaseHander.handle(x!!, connection!!)
        return false
    }

    override fun visit(x: SQLAlterTableDropIndex?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: SQLColumnPrimaryKey?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: SQLColumnUniqueKey?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: SQLWithSubqueryClause.Entry?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: SQLCharacterDataType?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: SQLAlterTableAlterColumn?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: SQLCheck?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: SQLAlterTableDropForeignKey?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: SQLAlterTableDropPrimaryKey?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: SQLAlterTableDisableKeys?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: SQLAlterTableEnableKeys?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: SQLAlterTableStatement?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: SQLAlterTableDisableConstraint?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: SQLAlterTableEnableConstraint?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: SQLColumnCheck?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: SQLAlterTableDropConstraint?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: SQLCreateIndexStatement?): Boolean {
        CreateIndex.handle(x!!, connection!!)
        return false
    }

    override fun visit(x: SQLAlterTableRenameColumn?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: SQLColumnReference?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: SQLAlterTableAddIndex?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: SQLAlterTableAddConstraint?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: SQLCreateTriggerStatement?): Boolean {
        CreateTrigger.handle(x!!, connection!!)
        return false
    }


    override fun visit(x: SQLAlterTableRename?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: SQLAlterViewRenameStatement?): Boolean {
        AlterView.handle(x!!, connection!!)
        return false
    }

    override fun visit(x: SQLAlterTableAddPartition?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: SQLAlterTableDropPartition?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: SQLAlterTableRenamePartition?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: SQLAlterTableSetComment?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: SQLAlterTableSetLifecycle?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: SQLAlterTableEnableLifecycle?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: SQLAlterTableDisableLifecycle?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: SQLAlterTableTouch?): Boolean {
        connection!!!!.writeok()
        return false
    }


    override fun visit(x: SQLCloseStatement?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: SQLIfStatement.Else?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: SQLIfStatement.ElseIf?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: SQLCreateProcedureStatement?): Boolean {
        CreateProcedure_function.handle(x!!, connection!!)
        return false
    }

    override fun visit(x: SQLBlockStatement?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: SQLAlterTableDropKey?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: SQLAlterDatabaseStatement?): Boolean {
        AlterDatabase.handle(x!!, connection!!)
        return false
    }

    override fun visit(x: SQLAlterTableConvertCharSet?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: SQLAlterTableReOrganizePartition?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: SQLAlterTableCoalescePartition?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: SQLAlterTableTruncatePartition?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: SQLAlterTableDiscardPartition?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: SQLAlterTableImportPartition?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: SQLAlterTableAnalyzePartition?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: SQLAlterTableCheckPartition?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: SQLAlterTableOptimizePartition?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: SQLAlterTableRebuildPartition?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: SQLAlterTableRepairPartition?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: SQLMergeStatement.MergeUpdateClause?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: SQLMergeStatement.MergeInsertClause?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: SQLCreateSequenceStatement?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun endVisit(x: MySqlRenameTableStatement.Item?) {
        super.endVisit(x)
    }


    override fun visit(x: SQLSelectGroupByClause?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: SQLSelectItem?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: SQLSelect?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: SQLSelectQueryBlock?): Boolean {
        connection!!!!.writeok()
        return false
    }


    override fun visit(x: SQLOrderBy?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: SQLSelectOrderByItem?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: SQLDropTableStatement?): Boolean {
        DropTable.handle(x!!, connection!!)
        return false
    }

    override fun visit(x: SQLDataType?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: SQLDeleteStatement?): Boolean {
        Mdelete.handle(x!!, connection!!)
        return false
    }

    override fun visit(x: SQLInsertStatement?): Boolean {
        Minsert.handle(x!!, connection!!)
        return false
    }

    override fun visit(x: SQLUpdateSetItem?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: SQLUpdateStatement?): Boolean {
        Mupdate.handle(x!!, connection!!)
        return false
    }

    override fun visit(x: SQLNotNullConstraint?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: SQLUnionQuery?): Boolean {
        connection!!!!.writeok()
        return false
    }


    override fun visit(x: SQLSetStatement?): Boolean {
        MSetHandler.handle(x!!, connection!!)
        return false
    }

    override fun visit(x: SQLJoinTableSource?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: SQLInsertStatement.ValuesClause?): Boolean {
        connection!!!!.writeok()
        return false
    }


    override fun visit(x: SQLSubqueryTableSource?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: SQLTruncateStatement?): Boolean {
        TruncteTable.handle(x!!, connection!!)
        return false
    }


    override fun visit(x: SQLUseStatement?): Boolean {
        Usedatabase.handle(x!!, connection!!)
        return false
    }

    override fun visit(x: SQLDropIndexStatement?): Boolean {
        DropIndex.handle(x!!, connection!!)
        return false
    }

    override fun visit(x: SQLDropViewStatement?): Boolean {
        DropView.handle(x!!, connection!!)
        return false
    }

    override fun visit(x: SQLSavePointStatement?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: SQLRollbackStatement?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: SQLReleaseSavePointStatement?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: SQLOver?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: SQLKeep?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: SQLWithSubqueryClause?): Boolean {
        connection!!!!.writeok()
        return false
    }


    override fun visit(x: SQLUnique): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: SQLPrimaryKeyImpl?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: SQLForeignKeyImpl?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: SQLDropSequenceStatement?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: SQLDropTriggerStatement?): Boolean {
        Droptrigger.handle(x!!, connection!!)
        return false
    }

    override fun visit(x: SQLDropUserStatement?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: SQLExplainStatement?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: SQLGrantStatement?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: SQLDropDatabaseStatement?): Boolean {
        DropDatabase.handle(x!!, connection!!)
        return false
    }

    override fun visit(x: SQLDropFunctionStatement?): Boolean {
        DropProcedure_function.handle(x!!, connection!!)
        return false
    }

    override fun visit(x: SQLDropTableSpaceStatement?): Boolean {
        DropTABLEspace.handle(x!!, connection!!)
        return false
    }

    override fun visit(x: SQLDropProcedureStatement?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: SQLUnionQueryTableSource?): Boolean {
        connection!!!!.writeok()
        return false
    }


    override fun visit(x: SQLRevokeStatement?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: SQLShowTablesStatement?): Boolean {
        ShowHandler.showtables(x!!, connection!!)
        //        MShowTables.response(connection!!, x!!, 1);
        return false
    }

    override fun visit(x: SQLOpenStatement?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: SQLFetchStatement?): Boolean {
        connection!!!!.writeok()
        return false
    }


    override fun visit(x: SQLIfStatement?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: SQLLoopStatement?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: SQLParameter?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: SQLDeclareItem?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: SQLPartitionValue?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: SQLPartition?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: SQLPartitionByRange?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: SQLPartitionByHash?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: SQLPartitionByList?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: SQLSubPartition?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: SQLSubPartitionByHash?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: SQLSubPartitionByList?): Boolean {
        connection!!!!.writeok()
        return false
    }


    override fun visit(x: SQLMergeStatement?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: SQLErrorLoggingClause?): Boolean {
        connection!!!!.writeok()
        return false
    }

    override fun visit(x: SQLNullConstraint?): Boolean {
        connection!!!!.writeok()
        return false
    }

    fun setConnection(source: OConnection) {
        this.connection=source
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
