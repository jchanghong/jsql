/*
 * Java-based distributed database like Mysql
 */

package io.jsql.sql.util

import java.util.*

/**
 * Created by 长宏 on 2017/2/28 0028.
 */
object Mcomputer {
    private val op1 = Arrays.asList('+', '-')
    private val op2 = Arrays.asList('*', '/', '%')
    private val opnnn = Arrays.asList('(', ')')

    /**
     * Compute double.计算器，比如，3，3+4，等

     * @param exp the exp
     * *
     * @return the double
     */
    fun compute(exp: String): Double {
        var exp = exp
        if (exp.contains("(")) {
            exp = dimissc(exp)//消除括号
        }
        return computeno(exp)
    }

    private fun computeno(exp: String): Double {//没有括号
        var error = false
        var re = 0.0
        try {
            re = java.lang.Double.parseDouble(exp)
        } catch (e: Exception) {
            error = true
        }

        if (!error) {
            return re
        }
        var index = exp.indexOf("+")
        if (index != -1) {
            return computeno(exp.substring(0, index)) + computeno(exp.substring(index + 1))
        }
        index = exp.indexOf("-")
        if (index != -1) {
            return computeno(exp.substring(0, index)) - computeno(exp.substring(index + 1))
        }
        index = exp.indexOf("*")
        if (index != -1) {
            return computeno(exp.substring(0, index)) * computeno(exp.substring(index + 1))
        }
        index = exp.indexOf("/")
        if (index != -1) {
            return computeno(exp.substring(0, index)) / computeno(exp.substring(index + 1))
        }
        return 0.0
    }

    private fun dimissc(exp: String): String {//去掉括号 (1+(1+2)+2)+((1+2))
        var exp = exp
        var start = exp.lastIndexOf("(")
        var end = -1
        while (start != -1) {
            end = exp.indexOf(")", start)
            exp = exp.substring(0, start) + computeno(exp.substring(start + 1, end)) + exp.substring(end + 1)
            start = exp.lastIndexOf("(")
        }
        return exp
    }

    /**
     * The entry point of application.

     * @param args the input arguments
     */
    @JvmStatic
    fun main(args: Array<String>) {
        println(dimissc("(1+(1+2)+2)+((1+2))"))
        println(compute("(1+(1+2)+2)+((1+2))"))
        val scanner = Scanner(System.`in`)
        var input: String
        while (true) {
            input = scanner.nextLine()
            println(compute(input))
        }
    }
}
