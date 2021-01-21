package com.xbyoung.lib.utils

import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.util.DisplayMetrics
import android.view.WindowManager



/**
 * Created by Administrator on 2018/2/22.
 */
object DisplayUtils {
    var screenHeight = 0
    var screenWidth = 0
    fun init(context: Context){
        screenHeight = getScreenHeight(context)
        screenWidth = getScreenSize(context)[0]
    }


    @JvmStatic
    fun hideKeybord(context: Context) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS)
    }

  
    
    @JvmStatic
    val getScreenHeight: (Context) -> Int = { getScreenSize(it)[1] }
    
    @JvmStatic
    val getScreenWidth: (Context) -> Int = { getScreenSize(it)[0] }
    
    fun getScreenSize(context: Context): IntArray {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val outMetrics = DisplayMetrics()
        wm.defaultDisplay.getMetrics(outMetrics)
        return intArrayOf(outMetrics.widthPixels, outMetrics.heightPixels)
    }




    fun getStatusBarHeight(context: Context): Int {
        var result = 0
        val resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId)
        }
        return result
    }

    /**
     * dp -> px
     */
    @JvmStatic
    fun dp2px(context: Context, dpValue: Float): Int = ((context.resources.displayMetrics.density * dpValue) + 0.5f).toInt()



}