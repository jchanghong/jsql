/*
 * Java-based distributed database like Mysql
 */

package io.jsql.audit

import org.apache.http.entity.ContentType
import org.apache.http.nio.entity.NStringEntity

/**
\* Created with IntelliJ IDEA.
\* User: jiang
\* Date: 2017/6/15 0015
简历maping，也就是建立表
\* Time: 22:36
\*/
fun main(args: Array<String>) {
    var stringlogin = """
{
  "mappings": {
    "loginlog": {
      "_all":       { "enabled": false  },
      "properties": {
        "user":    { "type": "text"  },
        "host":     { "type": "text"  },
        "result":  {
          "type":   "boolean"
        },
        "time":  {
          "type":   "date"

        }
        }
    }
  }
}

"""

    var stringsql = """
{
  "mappings": {
    "sqllog": {
      "_all":       { "enabled": false  },
      "properties": {
        "sql":    { "type": "text"  },
        "user":     { "type": "text"  },
        "host":      { "type": "text" } ,
"time" :{"type":"date"}
      }
    }
    }
  }
}

"""
    var entity = NStringEntity(stringlogin, ContentType.APPLICATION_JSON)
    var entitysql = NStringEntity(stringsql, ContentType.APPLICATION_JSON)
    try {
        elasticUtil.esrestClient.performRequest("delete", "/sqlindex", emptyMap())
        println("deleted--------sqlindex")
    } catch (e: Exception) {
        println("sqlindex not exits--------to be create.....................")
    } finally {
        elasticUtil.esrestClient.performRequest("put", "/sqlindex", emptyMap(), entitysql)
        println("sqlindex created")
    }

    try {
        elasticUtil.esrestClient.performRequest("delete", "/loginindex", emptyMap())
        println("deleted----------loginindex")
    } catch (e: Exception) {
        println("loginindex not exits--------to be create.....................")
    } finally {
        elasticUtil.esrestClient.performRequest("put", "/loginindex", emptyMap(), entity)
        println("loginindex created")
    }
}