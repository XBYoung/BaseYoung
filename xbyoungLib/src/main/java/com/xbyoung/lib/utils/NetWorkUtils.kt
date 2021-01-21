package com.xbyoung.lib.utils

/**
 * Created by Administrator on 2018/4/13.
 */

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import android.telephony.TelephonyManager
import com.xbyoung.lib.XBYoungLib
import java.io.IOException
import java.net.Inet4Address
import java.net.NetworkInterface
import java.net.SocketException

/**
 * 网络工具类
 * Created by alic on 16-4-8.
 */
object NetWorkUtils {
    /**
     * 判断是否有网络连接

     * @param context
     * *
     * @return
     */
    fun isNetworkConnected(): Boolean {
        if (XBYoungLib.getApp() != null) {
            // 获取手机所有连接管理对象(包括对wi-fi,net等连接的管理)
            val manager = XBYoungLib.getApp()
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            // 获取NetworkInfo对象
            val networkInfo = manager.activeNetworkInfo
            //判断NetworkInfo对象是否为空
            if (networkInfo != null)
                return networkInfo.isAvailable
        }
        return false
    }

    /**
     * 判断WIFI网络是否可用

     * @param context
     * *
     * @return
     */
    fun isWifiConnected(): Boolean {
        if (XBYoungLib.getApp() != null) {
            // 获取手机所有连接管理对象(包括对wi-fi,net等连接的管理)
            val manager = XBYoungLib.getApp()
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            // 获取NetworkInfo对象
            val networkInfo = manager.activeNetworkInfo
            //判断NetworkInfo对象是否为空 并且类型是否为WIFI
            if (networkInfo != null && networkInfo.type == ConnectivityManager.TYPE_WIFI)
                return networkInfo.isAvailable
        }
        return false
    }

    /**
     * 判断MOBILE网络是否可用

     * @param context
     * *
     * @return
     */
    fun isMobileConnected(): Boolean {
        if (XBYoungLib.getApp() != null) {
            //获取手机所有连接管理对象(包括对wi-fi,net等连接的管理)
            val manager = XBYoungLib.getApp()
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            //获取NetworkInfo对象
            val networkInfo = manager.activeNetworkInfo
            //判断NetworkInfo对象是否为空 并且类型是否为MOBILE
            if (networkInfo != null && networkInfo.type == ConnectivityManager.TYPE_MOBILE)
                return networkInfo.isAvailable
        }
        return false
    }

    /**
     * 获取当前网络连接的类型信息
     * 原生

     * @param context
     * *
     * @return
     */
    fun getConnectedType(): Int {
        if (XBYoungLib.getApp() != null) {
            //获取手机所有连接管理对象
            val manager = XBYoungLib.getApp()
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            //获取NetworkInfo对象
            val networkInfo = manager.activeNetworkInfo
            if (networkInfo != null && networkInfo.isAvailable) {
                //返回NetworkInfo的类型
                return networkInfo.type
            }
        }
        return -1
    }

    /**
     * 获取当前的网络状态 ：没有网络-0：WIFI网络1：4G网络-4：3G网络-3：2G网络-2
     * 自定义

     * @param context
     * *
     * @return
     */
    fun getAPNType(): Int {
        //结果返回值
        var netType = 0
        //获取手机所有连接管理对象
        val manager =
                XBYoungLib.getApp().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        //获取NetworkInfo对象
        val networkInfo = manager.activeNetworkInfo ?: return netType
        //NetworkInfo对象为空 则代表没有网络
        //否则 NetworkInfo对象不为空 则获取该networkInfo的类型
        val nType = networkInfo.type
        if (nType == ConnectivityManager.TYPE_WIFI) {
            //WIFI
            netType = 1
        } else if (nType == ConnectivityManager.TYPE_MOBILE) {
            val nSubType = networkInfo.subtype
            val telephonyManager =
                    XBYoungLib.getApp().getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            //3G   联通的3G为UMTS或HSDPA 电信的3G为EVDO
            netType =
                if (nSubType == TelephonyManager.NETWORK_TYPE_LTE && !telephonyManager.isNetworkRoaming) {
                    4
                } else if (nSubType == TelephonyManager.NETWORK_TYPE_UMTS
                    || nSubType == TelephonyManager.NETWORK_TYPE_HSDPA
                    || nSubType == TelephonyManager.NETWORK_TYPE_EVDO_0 && !telephonyManager.isNetworkRoaming
                ) {
                    3
                    //2G 移动和联通的2G为GPRS或EGDE，电信的2G为CDMA
                } else if (nSubType == TelephonyManager.NETWORK_TYPE_GPRS
                    || nSubType == TelephonyManager.NETWORK_TYPE_EDGE
                    || nSubType == TelephonyManager.NETWORK_TYPE_CDMA && !telephonyManager.isNetworkRoaming
                ) {
                    2
                } else {
                    2
                }
        }
        return netType
    }

    fun getWIIFip(): String {
        val wifiManager =
                XBYoungLib.getApp().applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val dhcpInfo = wifiManager.dhcpInfo
        val ip = dhcpInfo.serverAddress
        //此处获取ip为整数类型，需要进行转换
        return intToIp(ip)
        // return "192.168.1.112";
    }

    private fun intToIp(i: Int): String {
        return ((i and 0xFF).toString() + "." + (i shr 8 and 0xFF) + "." + (i shr 16 and 0xFF) + "."
                + (i shr 24 and 0xFF))
    }

    fun getWifiName(): String {
        val wifiManager =
                XBYoungLib.getApp().applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val info = wifiManager.connectionInfo
        return info.ssid.replace("\"", "") ?: "获取WIFI名称失败"
    }

    fun getIPAddress(): String? {
        @SuppressLint("WrongConstant")
        val info = (XBYoungLib.getApp()
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).activeNetworkInfo
        if (info != null && info.isConnected) {
            if (info.type == ConnectivityManager.TYPE_MOBILE) {//当前使用2G/3G/4G网络
                try {
                    //Enumeration<NetworkInterface> en=NetworkInterface.getNetworkInterfaces();
                    val en = NetworkInterface.getNetworkInterfaces()
                    en?.let {
                        while (en.hasMoreElements()) {
                            val intf = en.nextElement()
                            val enumIpAddr = intf.inetAddresses
                            while (enumIpAddr.hasMoreElements()) {
                                val inetAddress = enumIpAddr.nextElement()
                                if (!inetAddress.isLoopbackAddress && inetAddress is Inet4Address) {
                                    return inetAddress.getHostAddress()
                                }
                            }
                        }
                    }
                } catch (e: SocketException) {
                    e.printStackTrace()
                }

            } else if (info.type == ConnectivityManager.TYPE_WIFI) {//当前使用无线网络
                val wifiManager =
                        XBYoungLib.getApp().applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
                val wifiInfo = wifiManager.connectionInfo
                val ipAddress = intIp2StringIp(wifiInfo.ipAddress)//得到IPV4地址
                return ipAddress
            }
        } else {
            //当前无网络连接,请在设置中打开网络
        }
        return ""
    }

    /**
     * 将得到的int类型的IP转换为String类型

     * @param ip
     * *
     * @return
     */
    fun intIp2StringIp(ip: Int): String {
        return (ip and 0xFF).toString() + "." +
                (ip shr 8 and 0xFF) + "." +
                (ip shr 16 and 0xFF) + "." +
                (ip shr 24 and 0xFF)
    }

    fun pingIPAddress(ipAddress: String): Boolean {
        try {
            //-c 1是指ping的次数为1次，-w 3是指超时时间为3s
            val process = Runtime.getRuntime()
                .exec("ping -c 1 -w 3 $ipAddress")
            //status为0表示ping成功
            val status = process.waitFor()
            if (status == 0) {
                return true
            }
        } catch (e: InterruptedException) {
            e.printStackTrace()
            return false
        } catch (e: IOException) {
            e.printStackTrace()
            return false
        }
        return false
    }
}

