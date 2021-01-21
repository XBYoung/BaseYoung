package com.xbyoung.lib.utils

import android.annotation.SuppressLint
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.floor


/**
 * Created by Administrator on 2018/3/11.
 */
object DateUtils {
    object Format{
        val YMDHMS_ = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val YMDHMS2 = SimpleDateFormat("yyyy.MM.dd HH:mm:ss")
        val YMDHM = SimpleDateFormat("yyyy-MM-dd HH:mm")
        val YMDHM_POINT = SimpleDateFormat("yyyy.MM.dd HH:mm")
        val YMD = SimpleDateFormat("yyyy-MM-dd")
        val YMD_POINT = SimpleDateFormat("yyyy.MM.dd")
        val YM = SimpleDateFormat("yyyy-MM")
        val HMS = SimpleDateFormat("HH:mm:ss")
        val HM = SimpleDateFormat("HH:mm")
        val MD_CH = SimpleDateFormat("MM月dd日")
        val MDHM = SimpleDateFormat("MM-dd HH:mm")
        val Y = SimpleDateFormat("yyyy")
        val YMDHMS = SimpleDateFormat("yyyyMMddHHmmss")
        val YMDHMS1 = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
    }




    fun getDateByFormat(date: Long?, format: SimpleDateFormat): String {
        val relData = if (date == null) {
            Date(System.currentTimeMillis())
        } else {
            Date(date)
        }
        return format.format(relData)
    }

    fun decodeTimeToLong(format: SimpleDateFormat,string: String): Long {
        var date: Date? = null
        try {
            date = format.parse(string)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return date!!.time
    }


    fun dateToWeek(datetime: String): String {
        val f = SimpleDateFormat("yyyy-MM-dd")
        val weekDays = arrayOf("星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六")
        val cal = Calendar.getInstance() // 获得一个日历
        var datet: Date? = null
        try {
            datet = f.parse(datetime)
            cal.time = datet
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        var w = cal.get(Calendar.DAY_OF_WEEK) - 1 // 指示一个星期中的某天。
        if (w < 0)
            w = 0
        return weekDays[w]
    }

    fun getDatebyYM(year: Int, month: Int): String {
        val sb = StringBuffer()
        sb.append(year).append("年").append(month).append(month)
        return sb.toString()
    }

    fun formatDuration(s: Int): String {
        var time = ""
        var minute = 0
        var hour = 0
        var secend = s
        if (secend < 3600) {
            time = String.format("%1$02d:%2$02d", secend / 60, secend % 60)
        } else {
            time = String.format(
                "%1$02d:%2$02d:%3$02d",
                secend / 3600,
                secend % 3600 / 60,
                secend % 60
            )

        }

        return time
    }

    @SuppressLint("WrongConstant")
    fun getSupportBeginDayofMonth(year: Int, monthOfYear: Int): Date {
        val cal = Calendar.getInstance()
        // 不加下面2行，就是取当前时间前一个月的第一天及最后一天
        cal.set(Calendar.YEAR, year)
        cal.set(Calendar.MONTH, monthOfYear)
        cal.set(Calendar.DAY_OF_MONTH, 1)
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)

        cal.add(Calendar.DAY_OF_MONTH, -1)
        val lastDate = cal.time

        cal.set(Calendar.DAY_OF_MONTH, 1)
        val firstDate = cal.time
        return firstDate
    }

    @SuppressLint("WrongConstant")
    fun getSupportEndDayofMonth(year: Int, monthOfYear: Int): Date {
        val cal = Calendar.getInstance()
        // 不加下面2行，就是取当前时间前一个月的第一天及最后一天
        cal.set(Calendar.YEAR, year)
        cal.set(Calendar.MONTH, monthOfYear)
        cal.set(Calendar.DAY_OF_MONTH, 1)
        cal.set(Calendar.HOUR_OF_DAY, 23)
        cal.set(Calendar.MINUTE, 59)
        cal.set(Calendar.SECOND, 59)
        cal.add(Calendar.DAY_OF_MONTH, -1)
        val lastDate = cal.time
        cal.set(Calendar.DAY_OF_MONTH, 1)
        val firstDate = cal.time
        return lastDate
    }

    /**
     * 1s==1000ms
     */
    private val TIME_MILLISECONDS = 1000

    /**
     * 时间中的分、秒最大值均为60
     */
    private val TIME_NUMBERS = 60

    /**
     * 时间中的小时最大值
     */
    private val TIME_HOURSES = 24

    private fun getDataStringHm(date: Date?): String {
        var date = date
        if (date == null) {
            date = Date()
        }
        return Format.HM.format(date)

    }

    fun getDetailTimeForToday(date: Long, format: SimpleDateFormat): String {
        val todayStartTime = Format.YMD.parse(Format.YMD.format(Date())).time
        val yesterdayStartTime =
            todayStartTime - TIME_HOURSES * TIME_NUMBERS * TIME_NUMBERS * TIME_MILLISECONDS
        return when {
            date >= todayStartTime -> "今天 " + getDataStringHm(Date(date))
            date in yesterdayStartTime until todayStartTime -> "昨天 " + getDataStringHm(Date(date))
            else -> getDateByFormat(date, format)
        }
    }

    fun getMsgTime(date: Long?) : String {
        return date?.let {
            val today = Calendar.getInstance()
            today.timeInMillis = System.currentTimeMillis()
            val msgDate = Calendar.getInstance()
            msgDate.timeInMillis = date
            when {
                today.get(Calendar.YEAR) != msgDate.get(Calendar.YEAR) -> Format.YMDHM
                today.get(Calendar.DAY_OF_YEAR) != msgDate.get(Calendar.DAY_OF_YEAR) -> Format.MDHM
                else -> Format.HM
            }.format(Date(date))
        }?:""
    }


    //s->min
    fun secondToMin(duration: Long): String {
        return if (duration < 60) {
           "1"
        } else {
            floor(duration.toDouble()/60).toString().split(".")[0]
        }
    }


    // 判断是否是同一天
    fun isSameDay(time1: Long, time2: Long): Boolean {
        return Format.YMD.format(Date(time1)) == Format.YMD.format(Date(time2))
    }

    fun Int?.secondToDuration(): String? {
        return if (this ?: 0 < 3600) {
            "${this?.div(60) ?: 0}分钟"
        } else {
            "${this?.div(3600) ?: 0}小时${this?.rem(3600)?.div(60)?.let {
                if (it > 0) {
                    return@let "${it}分钟"
                } else {
                    return@let ""
                }
            }}"
        }
    }
}