package io.jsql.util

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

import java.text.ParseException
import java.util.Date

/**
 * 使用joda解析date,可以得到date的year,month,day等字段值

 * @author CrazyPig
 */
object DateUtil {


    val DEFAULT_DATE_PATTERN = "YYYY-MM-dd HH:mm:ss"
    val DATE_PATTERN_FULL = "YYYY-MM-dd HH:mm:ss.SSSSSS"
    val DATE_PATTERN_ONLY_DATE = "YYYY-MM-dd"
    val DEFAULT_TIME_PATTERN = "HHH:mm:ss"
    val TIME_PATTERN_FULL = "HHH:mm:ss.SSSSSS"

    /**
     * 根据日期字符串和日期格式解析得到date类型日期

     * @param dateStr
     * *
     * @param datePattern
     * *
     * @return
     * *
     * @throws ParseException
     */
    @Throws(ParseException::class)
    @JvmOverloads fun parseDate(dateStr: String, datePattern: String = DEFAULT_DATE_PATTERN): Date {
        val dt = DateTimeFormat.forPattern(datePattern).parseDateTime(dateStr)
        return dt.toDate()
    }

    /**
     * 获取date对象年份

     * @param date
     * *
     * @return
     */
    fun getYear(date: Date): Int {
        val dt = DateTime(date)
        return dt.year
    }

    /**
     * 获取date对象月份

     * @param date
     * *
     * @return
     */
    fun getMonth(date: Date): Int {
        val dt = DateTime(date)
        return dt.monthOfYear
    }

    /**
     * 获取date对象天数

     * @param date
     * *
     * @return
     */
    fun getDay(date: Date): Int {
        val dt = DateTime(date)
        return dt.dayOfMonth
    }

    /**
     * 获取date对象小时数

     * @param date
     * *
     * @return
     */
    fun getHour(date: Date): Int {
        val dt = DateTime(date)
        return dt.hourOfDay
    }

    /**
     * 获取date对象分钟数

     * @param date
     * *
     * @return
     */
    fun getMinute(date: Date): Int {
        val dt = DateTime(date)
        return dt.minuteOfHour
    }

    /**
     * 获取date对象秒数

     * @param date
     * *
     * @return
     */
    fun getSecond(date: Date): Int {
        val dt = DateTime(date)
        return dt.secondOfMinute
    }

    /**
     * 获取date对象毫秒数

     * @param date
     * *
     * @return
     */
    fun getMicroSecond(date: Date): Int {
        val dt = DateTime(date)
        return dt.millisOfSecond
    }

}
/**
 * 根据日期字符串解析得到date类型日期

 * @param dateStr
 * *
 * @return
 * *
 * @throws ParseException
 */