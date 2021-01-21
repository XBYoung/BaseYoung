package com.xbyoung.lib.manager

import okhttp3.OkHttpClient
import retrofit2.Retrofit

/**
 * @author:Young
 * @date: 2021/1/21
 */
object ApiFactory {
    /**
     * T: api接口
     * clientBuilder http相关配置
     * retrofitBuilder retrofit相关配置，拦截器，解析器，baseUrl等
     */
    inline fun <reified T> newApi(clientBuilder: OkHttpClient.Builder, retrofitBuilder: Retrofit.Builder): T {
        return retrofitBuilder
                .client(clientBuilder.build())
                .build()
                .create(T::class.java)
    }
}