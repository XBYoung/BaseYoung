package com.xbyoung.lib.utils

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.AppGlideModule
import okhttp3.OkHttpClient
import java.io.InputStream
import java.security.cert.CertificateException
import java.util.concurrent.TimeUnit
import javax.net.ssl.*

/**
 * Created by Administrator on 2018/2/28.
 */
@GlideModule
internal class RearGlideModule : AppGlideModule() {
    override fun isManifestParsingEnabled(): Boolean {

        return false

    }

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        super.registerComponents(context, glide, registry)
        val client = UnsafeOkHttpClient.unsafeOkHttpClient
        registry.replace(GlideUrl::class.java, InputStream::class.java, OkHttpUrlLoader.Factory(client))
    }



    object UnsafeOkHttpClient {

        val unsafeOkHttpClient: OkHttpClient
            get() {

                try {

                    val trustAllCerts = arrayOf<TrustManager>(

                            object : X509TrustManager {

                                @Throws(CertificateException::class)
                                override fun checkClientTrusted(chain: Array<java.security.cert.X509Certificate>, authType: String) {

                                }

                                @Throws(CertificateException::class)
                                override fun checkServerTrusted(chain: Array<java.security.cert.X509Certificate>, authType: String) {

                                }

                                override fun getAcceptedIssuers(): Array<java.security.cert.X509Certificate> {

                                    return arrayOf()

                                }

                            })

                    val sslContext = SSLContext.getInstance("SSL")

                    sslContext.init(null, trustAllCerts, java.security.SecureRandom())

                    val sslSocketFactory = sslContext.socketFactory

                    val builder = OkHttpClient.Builder()

                    builder.sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)

                    builder.hostnameVerifier(HostnameVerifier { hostname, session -> true })

                    builder.connectTimeout(20, TimeUnit.SECONDS)

                    builder.readTimeout(20, TimeUnit.SECONDS)

                    val okHttpClient = builder.build()

                    return okHttpClient

                } catch (e: Exception) {

                    throw RuntimeException(e)

                }

            }

    }
}