package com.xbyoung.lib.utils

import android.os.CountDownTimer

/**
 * 不同类型倒计时切换将覆盖将重置倒计时间
 */
class DownTimeUtil {
    companion object {
        fun ins() = Inner.ins
    }

    class Inner {
        companion object {
            val ins = DownTimeUtil()
        }
    }

    private var singleTimer: CountDownTimer? = null
    private var singleRemainSecond = 0


    private var timer: CountDownTimer? = null
    private var cancelTag = false
    private var downingPhone = ""
    private var tick: (Int) -> Unit? = {}
    private var finish: () -> Unit? = {}

    /**
     * 共享时间，用于登录时候共享倒计时
     */
    fun startDownTimeSingle(
        phone: String,
        second: Int,
        tick: (Int) -> Unit?,
        finish: () -> Unit?
    ) {
        singleTimer?.let {
            it.cancel()
        }
        cancelTag = false
        if (!singleIsDowning(downingPhone)) {
            singleRemainSecond = second
        }
        tick(singleRemainSecond)
        singleTimer = createNewTimer(singleRemainSecond, tick, finish)
        singleTimer!!.start()
        downingPhone = phone

    }

    fun cancelSingle() {
        cancelTag = true
    }

    fun cancel() {
        timer?.cancel()
        timer = null
    }

    fun singleIsDowning(phone: String): Boolean {
        return singleRemainSecond > 0 && phone == downingPhone && null != singleTimer
    }


    /**
     * 普通时间，不用共享同一时间,用于大部分非登录倒计时
     */

    fun startDownTime(
        second: Int,
        tick: (Int) -> Unit?,
        finish: () -> Unit?
    ) {
        timer?.cancel()
        timer = createNewTimer(second, tick, finish)
        timer?.start()

    }

    /**
     * 调用后销毁
     */
    private fun createNewTimer(
        second: Int, tick: (Int) -> Unit?,
        finish: () -> Unit?
    ): CountDownTimer {
        this.tick = tick
        this.finish = finish
        //调用 CountDownTimer 对象的 start() 方法开始倒计时，也不涉及到线程处理
        return object : CountDownTimer(second * 1000L, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                this@DownTimeUtil.singleRemainSecond = (millisUntilFinished / 1000).toInt()
                if (!this@DownTimeUtil.cancelTag) {
                    this@DownTimeUtil.tick(singleRemainSecond)
                }
            }

            override fun onFinish() {
                if (!this@DownTimeUtil.cancelTag) {
                    this@DownTimeUtil.finish()
                }
            }
        }
    }
}