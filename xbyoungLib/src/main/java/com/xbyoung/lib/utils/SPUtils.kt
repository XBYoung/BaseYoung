package com.xbyoung.lib.utils

import android.content.Context
import android.content.SharedPreferences

/**
 * Created by Administrator on 2018/2/13.
 */
object SPUtils {
    private lateinit var sp: SharedPreferences

    fun init(context: Context, name: String) {
        sp = context.getSharedPreferences(name, Context.MODE_PRIVATE)
    }

    fun saveString(key: String, value: String) {
        sp.edit().putString(addYan(key), value).apply()
    }

    fun saveBoolean(key: String, value: Boolean) {
        sp.edit().putBoolean(addYan(key), value).apply()
    }

    fun saveInt(key: String, value: Int) {
        sp.edit().putInt(addYan(key), value).apply()
    }


    private fun addYan(key: String): String {
        val formateKey = ByteArray(key.toByteArray().size)
        key.toByteArray().forEachIndexed { index, byte ->
            formateKey[index] = (byte + index % 5).toByte()
        }
        return String(formateKey)
    }

    private fun delYan(key: String): String {
        val formateSecret = ByteArray(key.toByteArray().size)
        key.toByteArray().forEachIndexed { index, byte ->
            formateSecret[index] = (byte - index % 5).toByte()
        }
        return String(formateSecret)
    }

    fun getStringValue(key: String): String {
        return sp.getString(addYan(key), "") ?: ""
    }

    fun getBooleanValue(key: String): Boolean {
        return sp.getBoolean(addYan(key), false)
    }

    fun getIntValue(key: String): Int {
        return sp.getInt(addYan(key), 0)
    }


}