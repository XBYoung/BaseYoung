package com.xbyoung.lib.extension

import android.animation.Animator
import android.animation.ObjectAnimator
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import com.xbyoung.lib.utils.LogUtils


/**
 * Created by Administrator on 2018/2/26.
 */
/**
 * button 扩展函数，提取适配button是否可用
 */
fun TextView.unClick(@DrawableRes res: Int) {
    this?.isClickable = false
    this?.setBackgroundResource(res)
}

fun TextView.canClick(@DrawableRes res: Int) {
    this?.isClickable = true
    this?.setBackgroundResource(res)
}


fun TextView.mText(): String {
    if (this.text.isNullOrEmpty()) {
        return ""
    }
    return this.text.toString()
}

fun TextView.mTextOrNull(): String? {
    if (this.text.isNullOrEmpty()) {
        return null
    } else {
        return this.text.toString()
    }
}


fun EditText.afterChange(afterChange: (Editable?) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            afterChange(s)
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }
    })

}

fun View?.show() {
    this?.visibility = View.VISIBLE
}

fun View?.hide() {
    this?.visibility = View.INVISIBLE

}

fun View?.gone() {
    this?.visibility = View.GONE

}

/**
 * 动画结束后添加动作
 */
fun ObjectAnimator.doAfter(after: () -> Unit) {
    this.addListener(object : Animator.AnimatorListener {
        override fun onAnimationRepeat(animation: Animator?) {

        }

        override fun onAnimationEnd(animation: Animator?) {
            after.invoke()
        }

        override fun onAnimationCancel(animation: Animator?) {
        }

        override fun onAnimationStart(animation: Animator?) {
        }
    })
}
/**
 * 动画结束后添加动作
 */
fun ObjectAnimator.doStart(start: () -> Unit) {
    this.addListener(object : Animator.AnimatorListener {
        override fun onAnimationRepeat(animation: Animator?) {

        }

        override fun onAnimationEnd(animation: Animator?) {

        }

        override fun onAnimationCancel(animation: Animator?) {
        }

        override fun onAnimationStart(animation: Animator?) {
            start()
        }
    })
}


fun TextView.safeText(info: String?) {
    this.text = info.nullToUnknown()
}

fun ImageView.canClick(@DrawableRes res: Int) {
    this.setImageResource(res)
}

fun ImageView.unClick(@DrawableRes res: Int) {
    this.setImageResource(res)
}

fun TextView.drawableRightClick(click: () -> Unit) {
    setOnTouchListener(OnTouchListener { v, event -> // et.getCompoundDrawables()得到一个长度为4的数组，分别表示左右上下四张图片
        val drawable: Drawable = compoundDrawables[2] ?: return@OnTouchListener false
        //如果右边没有图片，不再处理
        //如果不是按下事件，不再处理
        if (event.action == MotionEvent.ACTION_DOWN) {
            LogUtils.d("x = ${event.x}   width = $width    paddingRight = $paddingRight    intrinsicWidth = ${drawable.intrinsicWidth}"  )
            val x = event.x
            val widthx = width
            val paddingRightx = paddingRight
            val pWidth = drawable.intrinsicWidth
            if (event.x > (width - paddingRight - drawable.intrinsicWidth)
            ) {
                click()
            }
        }

        false
    })
}