package com.xbyoung.lib.utils

import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.absoluteValue

/**
 * Created on 2020/8/10.
 * 包名:com.xbyoung.lib.utils
 * @author 李舰舸 <jiange.li@56qq.com>
 */
inline fun Float.integerAsNecessary(point: Int): String {
    return if (this.absoluteValue >= 1000) { // 如果大于等于4位数, 不显示小数
        this.scaleHalfUp(0)
    } else {
        this.scaleHalfUp(point)
    }
}
inline fun Double.integerAsNecessary(point: Int): String {
    return if (this.absoluteValue >= 1000) { // 如果大于等于4位数, 不显示小数
        this.scaleHalfUp(0)
    } else {
        this.scaleHalfUp(point)
    }
}

fun Float.scaleHalfUp(point: Int): String {
    return BigDecimal(this.toString()).setScale(point, RoundingMode.HALF_UP)
        .stripTrailingZeros().toPlainString()
}

fun Double.scaleHalfUp(point: Int): String {
    return BigDecimal(this.toString()).setScale(point, RoundingMode.HALF_UP)
        .stripTrailingZeros().toPlainString()
}

fun Float.getDistanceUnit(point: Int = 2): String {
    return if (this > 1000) {
        (this / 1000f).integerAsNecessary(point) + "km"
    } else {
        this.integerAsNecessary(point) + "m"
    }
}
