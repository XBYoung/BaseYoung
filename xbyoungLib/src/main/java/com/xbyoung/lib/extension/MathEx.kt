package com.xbyoung.lib.extension

import kotlin.math.floor

/**
 * Created by Administrator on 2018/5/15.
 */

fun Double.keep2(): String {
    return (floor(this * 100) / 100).toString()
}

fun Number.keep1(): String {
    val df = java.text.DecimalFormat("0.0")
    var format = df.format(this)
    return format
}
