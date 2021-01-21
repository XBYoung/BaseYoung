package com.xbyoung.lib.utils

import android.content.Context
import android.content.pm.PackageManager
import android.util.Base64
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*


/**
 * Created by Administrator on 2018/2/27.
 */
class DecUtils {
    companion object {
        /**
         * base64

         * @param str
         * *
         * @return
         */
        @JvmStatic
        fun toBase64(str: String): String {
            val encodedString = Base64.encodeToString(str.toByteArray(), Base64.DEFAULT)

            return encodedString
        }

        //将 BASE64 编码的字符串 s 进行解码
        @JvmStatic
        fun getFromBASE64(s: String?): String? {
            if (s.isNullOrEmpty()) return null
            val info = String(Base64.decode(s.toByteArray(), Base64.DEFAULT))
            return info
        }

        /**
         * 字符串 -> SHA1
         */
        @JvmStatic
        @Throws(NoSuchAlgorithmException::class)
        fun getSHA1(str: String): String {
            val md = MessageDigest.getInstance("SHA-1")
            md.update(str.toByteArray())
            val buf = StringBuffer()
            val bits = md.digest()
            for (i in bits.indices) {
                var a = bits[i].toInt()
                if (a < 0) a += 256
                if (a < 16) buf.append("0")
                buf.append(Integer.toHexString(a))
            }
            return buf.toString()
        }


        fun sHA1(context: Context): String? {
            try {
                val info = context.packageManager.getPackageInfo(
                        context.packageName, PackageManager.GET_SIGNATURES)
                val cert = info.signatures[0].toByteArray()
                val md = MessageDigest.getInstance("SHA1")
                val publicKey = md.digest(cert)
                val hexString = StringBuffer()
                for (i in publicKey.indices) {
                    val appendString = Integer.toHexString(0xFF and publicKey[i].toInt())
                            .toUpperCase(Locale.US)
                    if (appendString.length == 1) {
                        hexString.append("0")
                    }
                    hexString.append(appendString)
                    hexString.append(":")
                }
                val result = hexString.toString()
                return result.substring(0, result.length - 1)
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            } catch (e: NoSuchAlgorithmException) {
                e.printStackTrace()
            }

            return null
        }
    }
}