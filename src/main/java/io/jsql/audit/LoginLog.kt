package io.jsql.audit

import java.util.*

/**
\* Created with IntelliJ IDEA.
\* User: jiang
\* Date: 2017/6/15 0015
\* Time: 15:07
\*/
data class LoginLog(val user: String, val host: String, val result: Boolean, val time: Date = Date())

fun LoginLog.sendesServer() {
    elasticUtil.put(this)
}