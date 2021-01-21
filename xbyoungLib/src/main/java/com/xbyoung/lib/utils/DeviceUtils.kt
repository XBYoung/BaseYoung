package com.xbyoung.lib.utils

import android.os.Build
import android.util.Log
import java.util.*

/**
 * Created by luke on 2016/10/24.
 */
object DeviceUtils {
    val version: String
        get() {
            LogUtils.d(Build.VERSION.RELEASE)
            return Build.VERSION.RELEASE
        }

    //获取手机型号
    val phoneModel: String
        get() = Build.MODEL

    val product: String
        get() = Build.MANUFACTURER

    val productAndModel: String
        get() {
            val sb = StringBuffer()
            sb.append(product)
            sb.append("-")
            sb.append(phoneModel)
            LogUtils.d(sb.toString())
            return sb.toString()
        }


    val device: String
        get() = Build.MANUFACTURER


    @JvmStatic
    fun getDeviceId(): String {
        val serial = "serial"
        val deviceCode =
            "LT" + Build.BOARD.length % 10
        + Build.BRAND.length % 10
        + Build.CPU_ABI.length % 10
        + Build.DEVICE.length % 10
        + Build.DISPLAY.length % 10
        + Build.ID.length % 10
        + Build.MANUFACTURER.length % 10
        + Build.MODEL.length % 10
        + Build.PRODUCT.length % 10
        val deviceId = UUID(deviceCode.hashCode().toLong(), serial.hashCode().toLong()).toString()
        Log.w("getDeviceId", "deviceId : $deviceId")
        return deviceId
    }


    /**
     * 获取当前时区

     * @return
     */
    fun getCurrentTimeZone(includeGmt: Boolean,
                           includeMinuteSeparator: Boolean, isFour: Boolean): String {
        val tz = TimeZone.getDefault()
        return createGmtOffsetString(includeGmt, includeMinuteSeparator, tz.rawOffset, isFour)
    }

    private fun createGmtOffsetString(includeGmt: Boolean,
                                      includeMinuteSeparator: Boolean, offsetMillis: Int, isFour: Boolean): String {
        var offsetMinutes = offsetMillis / 60000
        var sign = '+'
        if (offsetMinutes < 0) {
            sign = '-'
            offsetMinutes = -offsetMinutes
        }
        val builder = StringBuilder(9)
        if (includeGmt) {
            builder.append("GMT")
        }
        builder.append(sign)
        if (isFour) {
            appendNumber(builder, 2, offsetMinutes / 60)
            if (includeMinuteSeparator) {
                builder.append(':')
            }
            appendNumber(builder, 2, offsetMinutes % 60)
        } else {
            builder.append(offsetMinutes / 60)
        }
        return builder.toString()
    }

    /**
     * 返回四位需要

     * @param builder
     * *
     * @param count
     * *
     * @param value
     */

    private fun appendNumber(builder: StringBuilder, count: Int, value: Int) {
        val string = value.toString()
        for (i in 0 until count - string.length) {
            builder.append('0')
        }
        builder.append(string)
    }

    val OS_VERSION: String = Build.VERSION.RELEASE

}
