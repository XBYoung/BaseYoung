package com.xbyoung.lib.extension

import com.google.gson.Gson
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.lang.Exception
import java.net.ConnectException
import java.text.ParseException
import kotlin.coroutines.resumeWithException

/**
@author: Young <(￣︶￣)>
Create: 2019-07-11
 */


/**
 * 半封装
 */
suspend fun <T> Call<T>.createCallBack() = suspendCancellableCoroutine<T> {
    this.enqueue(CoroutineCallback<T>(it))

}


suspend fun <T> (suspend () -> T).tryNetWork(afterError: (ErrorResult) -> T) {
    runCatching {
        this.invoke()
    }.onFailure {
        it.printStackTrace()
        val errorResult = when (it) {
            is ErrorResult -> it
            is ConnectException -> defaultError()
            is HttpException -> {
                when (it.code()) {
                    in 500 until Int.MAX_VALUE -> {
                        ErrorResult(it.code().toString(), "服务器内部异常")
                    }
                    in 400 until 500 -> {
                        ErrorResult(it.code().toString(), "请求异常")
                    }
                    else -> defaultError()
                }
            }
            is ParseException -> defaultError()
            else -> defaultError()
        }
        afterError(errorResult)
    }
}

class CoroutineCallback<T>(private val continuation: CancellableContinuation<T>) : Callback<T> {
    override fun onFailure(call: Call<T>, throwable: Throwable) {
        if (continuation.isCancelled) return
        continuation.resumeWithException(throwable)
    }

    override fun onResponse(call: Call<T>, response: Response<T>) {
        continuation.resumeWith(runCatching {
            if (response.isSuccessful) {
                response.body() ?: throw NullPointerException("Response body is null: $response")
            } else {
                val errorStr = response.errorBody()?.string()
                if (errorStr.isNullOrEmpty()) {
                    throw defaultError()
                } else {
                    val errorResult = try {
                        Gson().fromJson(errorStr, ErrorResult::class.java)
                    } catch (e: Exception) {
                        defaultError()
                    }
                    throw errorResult
                }
            }
        })
    }
}

data class ErrorResult(
    var errorCode: String,
    var errorMsg: String

) : Exception()

fun defaultError(): ErrorResult {
    return ErrorResult(ERROR_CODE, "网络异常")
}

/**
 * 默认错误码
 */
const val ERROR_CODE = "10000"



