package com.xbyoung.lib.model

/**
 * @author: Young
 * @date: 2021/1/19
 */

data class ErrorResult(var errorCode: String, var errorMsg: String):Throwable()
data class ResponseTag(val tag:String ="")
