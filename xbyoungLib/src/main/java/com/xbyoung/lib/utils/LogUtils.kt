package com.xbyoung.lib.utils


import android.util.Log
import com.xbyoung.lib.XBYoungLib

/**
 * Created by luke on 2016/9/23.
 */
class LogUtils {
    companion object {
        lateinit var TAG: String


        var LOGV = XBYoungLib.isDebug
        private val INN = "-->>"
        private val INIO = "-_-！"
        private val TEMP = "*********"
        val VERBOSE = 2
        val DEBUG = 3
        val INFO = 4
        val WARN = 5
        val ERROR = 6
        val ASSERT = 7

        @JvmStatic
        fun initLog(tag:String,open:Boolean) {
            LOGV = open
            TAG = tag
            Log.d("meta_data", "TAG = $TAG \nLOGV = $LOGV")
        }

        @JvmStatic
        fun v(message: String) {
            if (LOGV) {
                val stackTrace = Throwable().stackTrace[1]
                val info = stackTrace.className +
                        INIO +
                        stackTrace.lineNumber +
                        INIO +
                        stackTrace.methodName +
                        TEMP
                printLoger(2, joiningLog(stackTrace, INIO, message))
            }

        }

        @JvmStatic
        fun d(message: String) {
            if (LOGV) {
                val stackTrace = Throwable().stackTrace[1]
                printLoger(3, joiningLog(stackTrace, INIO, message))
            }
        }

        @JvmStatic
        fun i(message: String) {
            if (LOGV) {
                val stackTrace = Throwable().stackTrace[1]
                printLoger(4, joiningLog(stackTrace, INIO, message))

            }

        }

        @JvmStatic
        fun w(message: String) {
            if (LOGV) {
                val stackTrace = Throwable().stackTrace[1]
                printLoger(5, joiningLog(stackTrace, INIO, message))

            }

        }
        @JvmStatic
        fun e(message: String) {
            if (LOGV) {
                val stackTrace = Throwable().stackTrace[1]
                printLoger(6, joiningLog(stackTrace, INIO, message))

            }

        }

        @JvmStatic
        fun v(tag: String, message: String) {
            if (LOGV) {
                val stackTrace = Throwable().stackTrace[1]
                printLoger(2, tag, joiningLog(stackTrace, INIO, message))

            }

        }

        @JvmStatic
        fun d(tag: String, message: String) {
            if (LOGV) {
                val stackTrace = Throwable().stackTrace[1]
                printLoger(3, tag, joiningLog(stackTrace, INIO, message))
            }
        }

        @JvmStatic
        fun i(tag: String, message: String) {
            if (LOGV) {
                val stackTrace = Throwable().stackTrace[1]
                printLoger(4, tag, joiningLog(stackTrace, INIO, message))
            }
        }

        @JvmStatic
        fun w(tag: String, message: String) {
            if (LOGV) {
                val stackTrace = Throwable().stackTrace[1]
                printLoger(5, tag, joiningLog(stackTrace, INIO, message))
            }
        }

        @JvmStatic
        fun e(tag: String, message: String) {
            if (LOGV) {
                val stackTrace = Throwable().stackTrace[1]
                printLoger(6, tag, joiningLog(stackTrace, INIO, message))
            }
        }

        val joiningLog = { stackTrace: StackTraceElement, inio: String, message: String ->
            "$stackTrace.className$inio$stackTrace.lineNumber$inio$stackTrace.methodName$inio\r\n$message"
        }

        private fun printLoger(priority: Int, tag: String, message: String) {
            when (priority) {
                2 -> Log.v(tag, TEMP + message)
                3 -> Log.d(tag, TEMP + message)
                4 -> Log.i(tag, TEMP + message)
                5 -> Log.w(tag, TEMP + message)
                6 -> Log.e(tag, TEMP + message)
            }
        }


        private fun printLoger(priority: Int, message: String) {
            when (priority) {
                2 -> Log.v(TAG, message)
                3 -> Log.d(TAG, message)
                4 -> Log.i(TAG, message)
                5 -> Log.w(TAG, message)
                6 -> Log.e(TAG, message)
            }
        }
    }
}