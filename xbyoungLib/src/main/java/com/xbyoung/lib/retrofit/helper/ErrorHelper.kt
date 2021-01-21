package com.xbyoung.lib.retrofit.helper

import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.google.gson.stream.MalformedJsonException
import com.xbyoung.lib.model.ErrorResult
import com.xbyoung.lib.retrofit.NoTokenException
import retrofit2.HttpException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

object ErrorHelper {
    val PARSE_ERROR_CODE = "parseError"
    fun fromThrowable(e: Throwable?): ErrorResult {
        var errorResult = ErrorResult("","")
        when (e) {
            is ErrorResult -> {
                //自定义错误直接返回
                return e
            }
            is HttpException -> {
                e.response()?.let { response ->
                    val code = e.code()
                    response.errorBody()?.let { errorBody ->
                        runCatching {
                            val errorEntityStr = errorBody.string()
                            errorResult = Gson().fromJson(
                                    errorEntityStr,
                                    ErrorResult::class.java
                                )
                        }.onFailure {
                            it.printStackTrace()
                        }
                    }
                    if (errorResult.errorMsg.isNullOrEmpty()) {
                        var errorMsg = if (code in 400..499) {
                            "客户端请求异常，服务不可用"
                        } else if (code >= 500) {
                            if (e.message!!.contains("Privoxy")) {
                                "代理服务器请求异常"
                            } else {
                                "服务器异常"
                            }
                        } else {
                            "网络异常"
                        }
                        errorResult.errorMsg = errorMsg
                    }
                }
            }

            else -> {

                val errorMsg = when (e) {
                    is NoTokenException -> {
                        "您尚未登录"
                    }
                    is SocketTimeoutException -> {
                        "网络连接超时"
                    }
                    is SocketException -> {
                        "网络异常"
                    }
                    is JsonParseException -> {
                        errorResult.errorCode = PARSE_ERROR_CODE
                        "数据解析异常"
                    }
                    is MalformedJsonException -> {
                        errorResult.errorCode = PARSE_ERROR_CODE
                        "数据解析异常"
                    }
                    is UnknownHostException -> {
                        "网络异常"
                    }

                    else -> {
                        "未知异常"
                    }
                }
                errorResult.errorMsg = errorMsg
            }
        }
        return errorResult
    }
}