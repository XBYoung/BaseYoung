package com.xbyoung.lib

import android.app.Application
import java.lang.NullPointerException

/**
 * @author: cmiot_maco
 * @date: 2021/1/19
 */
object XBYoungLib {
    private var app : Application? = null
    var isDebug = true
    fun injectApp(targetApp:Application){
        app = targetApp
    }

    fun getApp():Application{
        app?.let {
            return it
        }
        throw NullPointerException("app is null")
    }
}