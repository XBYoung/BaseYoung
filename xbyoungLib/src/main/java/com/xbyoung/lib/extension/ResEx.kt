package com.xbyoung.lib.extension

import android.graphics.Color
import androidx.annotation.StringRes
import com.xbyoung.lib.XBYoungLib


fun resString(@StringRes res: Int) = XBYoungLib.getApp().getString(res)

fun getCurrentColor(fraction: Float, startColor: Int, endColor: Int): Int {
    val redCurrent: Int
    val blueCurrent: Int
    val greenCurrent: Int
    val alphaCurrent: Int
    val redStart: Int = Color.red(startColor)
    val blueStart: Int = Color.blue(startColor)
    val greenStart: Int = Color.green(startColor)
    val alphaStart: Int = Color.alpha(startColor)
    val redEnd: Int = Color.red(endColor)
    val blueEnd: Int = Color.blue(endColor)
    val greenEnd: Int = Color.green(endColor)
    val alphaEnd: Int = Color.alpha(endColor)
    val redDifference = redEnd - redStart
    val blueDifference = blueEnd - blueStart
    val greenDifference = greenEnd - greenStart
    val alphaDifference = alphaEnd - alphaStart
    redCurrent = (redStart + fraction * redDifference).toInt()
    blueCurrent = (blueStart + fraction * blueDifference).toInt()
    greenCurrent = (greenStart + fraction * greenDifference).toInt()
    alphaCurrent = (alphaStart + fraction * alphaDifference).toInt()
    return Color.argb(alphaCurrent, redCurrent, greenCurrent, blueCurrent)
}
