package com.xbyoung.lib.extension

import com.google.gson.Gson

/**
@author: Young <(￣︶￣)>
Create: 2019-07-18
 */

inline fun Any.toJson() = Gson().toJson(this)

inline fun <reified T> fromJson(info: String): T {
    return Gson().fromJson(info, T::class.java)
}


