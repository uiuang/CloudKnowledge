@file:Suppress("DEPRECATION")

package com.uiuang.mvvm.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.telephony.TelephonyManager
import java.io.IOException
import java.net.HttpURLConnection
import java.net.NetworkInterface
import java.net.SocketException
import java.net.URL


/**
 * @Title:
 * @Description:
 * @author zsc
 * @date 2020/8/1 17:12
 */
object NetworkUtil {
    var url = "http://www.baidu.com"

    /**
     * NetworkAvailable
     */
    var NET_CNNT_BAIDU_OK = 1

    /**
     * no NetworkAvailable
     */
    var NET_CNNT_BAIDU_TIMEOUT = 2

    /**
     * Net no ready
     */
    var NET_NOT_PREPARE = 3

    /**
     * net error
     */
    var NET_ERROR = 4

    /**
     * TIMEOUT
     */
    private val TIMEOUT = 3000

    /**
     * check NetworkAvailable
     *
     * @param context
     * @return
     */
    fun isNetworkAvailable(context: Context): Boolean {
        val manager =
            context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (manager == null) {
            return false
        }
        val info = manager.activeNetworkInfo
        return null != info && info.isAvailable
    }

    /**
     * getLocalIpAddress
     *
     * @return
     */
    fun getLocalIpAddress(): String? {
        var ret = ""
        try {
            val en = NetworkInterface.getNetworkInterfaces()
            while (en.hasMoreElements()) {
                val networkInterface = en.nextElement()
                val inetAddr = networkInterface.inetAddresses
                while (inetAddr.hasMoreElements()) {
                    val inetAddress = inetAddr.nextElement()
                    if (!inetAddress.isLoopbackAddress) {
                        ret = inetAddress.hostAddress.toString()
                    }
                }
            }
        } catch (ex: SocketException) {
            ex.printStackTrace()
        }
        return ret
    }

    /**
     * 返回当前网络状态
     *
     * @param context
     * @return
     */
    fun getNetState(context: Context): Int {
        try {
            val connectivity = context
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (connectivity != null) {
                val networkInfo = connectivity.activeNetworkInfo
                if (networkInfo != null) {
                    return if (networkInfo.isAvailable && networkInfo.isConnected) {
                        if (!connectionNetwork()) {
                            NetworkUtil.NET_CNNT_BAIDU_TIMEOUT
                        } else {
                            NetworkUtil.NET_CNNT_BAIDU_OK
                        }
                    } else {
                        NetworkUtil.NET_NOT_PREPARE
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return NetworkUtil.NET_ERROR
    }

    /**
     * ping "http://www.baidu.com"
     *
     * @return
     */
    private fun connectionNetwork(): Boolean {
        var result = false
        var httpUrl: HttpURLConnection? = null
        try {
            httpUrl = URL(NetworkUtil.url)
                .openConnection() as HttpURLConnection
            httpUrl.connectTimeout = NetworkUtil.TIMEOUT
            httpUrl.connect()
            result = true
        } catch (e: IOException) {
        } finally {
            httpUrl?.disconnect()
            httpUrl = null
        }
        return result
    }

    /**
     * check is3G
     *
     * @param context
     * @return boolean
     */
    fun is3G(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetInfo = connectivityManager.activeNetworkInfo
        return (activeNetInfo != null && activeNetInfo.type == ConnectivityManager.TYPE_MOBILE)
    }

    /**
     * isWifi
     *
     * @param context
     * @return boolean
     */
    fun isWifi(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetInfo = connectivityManager.activeNetworkInfo
        return (activeNetInfo != null && activeNetInfo.type == ConnectivityManager.TYPE_WIFI)
    }

    /**
     * is2G
     *
     * @param context
     * @return boolean
     */
    fun is2G(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetInfo = connectivityManager.activeNetworkInfo
        return (activeNetInfo != null
                && (activeNetInfo.subtype == TelephonyManager.NETWORK_TYPE_EDGE
                || activeNetInfo.subtype == TelephonyManager.NETWORK_TYPE_GPRS
                || activeNetInfo.subtype == TelephonyManager.NETWORK_TYPE_CDMA))
    }

    /**
     * is wifi on
     */
    fun isWifiEnabled(context: Context): Boolean {
        val mgrConn = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val mgrTel = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return mgrConn.activeNetworkInfo != null
                && mgrConn.activeNetworkInfo?.state == NetworkInfo.State.CONNECTED
                || mgrTel.networkType == TelephonyManager.NETWORK_TYPE_UMTS
    }


}