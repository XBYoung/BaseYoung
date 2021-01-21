package com.xbyoung.lib.extension

import android.graphics.Paint
import android.graphics.Rect
import android.os.Handler
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import kotlin.math.max


/**
@author: Young <(￣︶￣)>
Create: 2019-07-15
 */


fun keepViewAboveKeyboard(root: View, moveView: View, showView: View) {

    root.viewTreeObserver.addOnGlobalLayoutListener {
        val rect = Rect()
        // 获取root在窗体的可视区域
        root.getWindowVisibleDisplayFrame(rect)
        // 获取root在窗体的不可视区域高度(被其他View遮挡的区域高度)
        val rootInvisibleHeight = root.rootView.height - rect.bottom
        // 若不可视区域高度大于200，则键盘显示,其实相当于键盘的高度
        if (rootInvisibleHeight > 200) {
            // 显示键盘时
            val srollHeight = Math.abs(root.rootView.height / 2 + showView.height / 2 - rect.bottom)
            if (srollHeight > 0) {
                //当键盘高度覆盖按钮时
                moveView.scrollTo(0, srollHeight)
            }
        } else {
            // 隐藏键盘时
            moveView.scrollTo(0, 0)
        }
    }
}

fun View.measureLength(defaultWidth: Int, measureSpec: Int): Int {
    var defaultWidth = defaultWidth
    val specMode = View.MeasureSpec.getMode(measureSpec)
    val specSize = View.MeasureSpec.getSize(measureSpec)
    when (specMode) {
        View.MeasureSpec.AT_MOST -> {
            defaultWidth = defaultWidth
        }
        View.MeasureSpec.EXACTLY -> {
            defaultWidth = specSize
        }
        View.MeasureSpec.UNSPECIFIED -> {
            defaultWidth = max(defaultWidth, specSize)
        }
    }
    return defaultWidth
}

fun View.dp2px(dpValue: Float): Float = (context.resources.displayMetrics.density * dpValue) + 0.5f

fun View.sp2px(spValue: Float): Float =
    (context.resources.displayMetrics.scaledDensity * spValue) + 0.5f

fun View.px2sp(pxValue: Float): Float =
    (pxValue / context.resources.displayMetrics.scaledDensity + 0.5f)

fun View.px2dp(pxValue: Float): Float =
    (pxValue / context.resources.displayMetrics.density + 0.5f)

private val clickLockHandler by lazy {
    Handler {
        lock = false
        true
    }
}
private var lock = false
fun View.click(safe: Boolean = true, doClick: () -> Unit) {
    this.setOnClickListener {
        if (!lock) {
            if (safe) {
                lock = true
                clickLockHandler.sendEmptyMessageDelayed(1, 500)
            }
            doClick()
        }
    }
}

fun View.touch(consume: Boolean = false, doTouch: (View, MotionEvent) -> Unit) {
    this.setOnTouchListener { v, event ->
        doTouch(v, event)
        return@setOnTouchListener consume
    }
}


fun Paint.getTextSize(str: String): Pair<Int, Int> {
    val rect = Rect()
    this.getTextBounds(str, 0, str.length, rect)
    val w = rect.width()
    val h = rect.height()
    return Pair(w, h)
}

fun Paint.getBaseLineToCenterDistance(): Float {

    val fontMetrics: Paint.FontMetrics = fontMetrics
    return (fontMetrics.descent - fontMetrics.ascent) / 2 - fontMetrics.descent
}

fun ViewGroup.contentHeight(): Int {
    return measuredHeight - paddingTop - paddingBottom
}


