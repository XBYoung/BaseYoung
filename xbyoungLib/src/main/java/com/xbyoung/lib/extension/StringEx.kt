package com.xbyoung.lib.extension


import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import com.google.gson.Gson
import java.util.regex.Pattern


/**
 * Created by Administrator on 2018/2/12.
 *
 *
 */

fun String.isChinaTel(): Boolean {
    return this.startsWith("1") && this.length >= 11
}

fun String.isPwdLength(): Boolean {
    return this.length >= 8
}


fun String.hideTel(): String {
    return this.substring(0, 3) + "****" + this.substring(7, 11)
}

/**
 * 将字符串转换成Bitmap类型
 */
fun String.decodeBase64ToBitmap(): Bitmap? {
    var bitmap: Bitmap? = null
    try {
        val bitmapArray: ByteArray = Base64.decode(this, Base64.DEFAULT)
        bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.size)
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return bitmap
}

/**
 * 根据html页面返回全路径
 */
fun String.getHTML(): String {
    val sb = StringBuilder()
    sb.append("file:///android_asset/").append(this)
    return sb.toString()
}


/**
 * 密码规则
 * 8-32位，包含数字、大写、小写、特殊字符四选三
 */
fun String.passwordEnable(): Boolean {
    val regExp =
        "^(?![a-zA-Z]+\$)(?![A-Z0-9]+\$)(?![A-Z\\W_]+\$)(?![a-z0-9]+\$)(?![a-z\\W_]+\$)(?![0-9\\W_]+\$)[a-zA-Z0-9\\W_]{8,32}\$"
    val p = Pattern.compile(regExp)
    val m = p.matcher(this)
    return m.matches()
}


fun <T> String.toClass(Cls: Class<T>): T {
    return Gson().fromJson(this, Cls)
}

fun String?.nullToUnknown(): String {
    return this ?: "未知"
}

fun String?.safeValue(): String {
    return if (this.isNullOrEmpty()) "" else this
}

fun String?.removeChinese(): String {
    return if (this.isNullOrEmpty()) {
        ""
    } else {
        val REGEX_CHINESE = "[\u4e00-\u9fa5]"
        this.replace(Regex(REGEX_CHINESE), "")
    }
}
