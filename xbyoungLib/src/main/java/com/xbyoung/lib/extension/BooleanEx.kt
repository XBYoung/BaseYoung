package com.xbyoung.lib.extension
/**
@author: Young <(￣︶￣)>
Create: 2019-07-10
 */

sealed class BooleanEx<out T>

object Else : BooleanEx<Nothing>()
class Yes<T>(val yes: T) : BooleanEx<T>()

inline fun <T> Boolean.yes(yes: () -> T): BooleanEx<T> = when {
    this -> Yes(yes())
    else -> Else
}

inline fun <T> BooleanEx<T>.no(doNo: () -> T): T =
        when (this) {
            is Yes -> this.yes
            is Else -> doNo()
        }