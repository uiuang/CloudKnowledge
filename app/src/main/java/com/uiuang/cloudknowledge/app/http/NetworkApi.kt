package com.uiuang.cloudknowledge.app.http

import android.content.Context
import android.text.TextUtils
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.google.gson.GsonBuilder
import com.uiuang.cloudknowledge.BuildConfig
import com.uiuang.cloudknowledge.app.App
import com.uiuang.mvvm.network.BaseNetworkApi
import com.uiuang.mvvm.network.CoroutineCallAdapterFactory
import com.uiuang.mvvm.network.interceptor.CacheInterceptor
import com.uiuang.mvvm.util.NetworkUtil
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import org.apache.commons.lang.StringUtils
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.IOException
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.*
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager


/**
 * @Title:
 * @Description:
 * @author zsc
 * @date 2020/8/2 11:59
 */

val apiService: ApiService by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
    NetworkApi.INSTANCE.getApi(ApiService::class.java, ApiService.SERVER_URL)
}

val getDouBanService: ApiService by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
    NetworkApi.INSTANCE.getApi(ApiService::class.java, ApiService.API_DOUBAN)
}
val getTingServer: ApiService by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
    NetworkApi.INSTANCE.getApi(ApiService::class.java, ApiService.API_TING)
}

val getGankIOServer: ApiService by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
    NetworkApi.INSTANCE.getApi(ApiService::class.java, ApiService.API_GANKIO)
}
val getGiteeServer: ApiService by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
    NetworkApi.INSTANCE.getApi(ApiService::class.java, ApiService.API_GITEE)
}

val getWanAndroidServer: ApiService by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
    NetworkApi.INSTANCE.getApi(ApiService::class.java, ApiService.API_WAN_ANDROID)
}
val getQSBKServer: ApiService by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
    NetworkApi.INSTANCE.getApi(ApiService::class.java, ApiService.API_QSBK)
}

val getMtimeServer: ApiService by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
    NetworkApi.INSTANCE.getApi(ApiService::class.java, ApiService.API_MTIME)
}

val getMtimeTicketServer: ApiService by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
    NetworkApi.INSTANCE.getApi(ApiService::class.java, ApiService.API_MTIME_TICKET)
}

class NetworkApi : BaseNetworkApi() {

    companion object {
        val INSTANCE: NetworkApi by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            NetworkApi()
        }
    }

    /**
     * 实现重写父类的setHttpClientBuilder方法，
     * 在这里可以添加拦截器，可以对 OkHttpClient.Builder 做任意操作
     */
    override fun setHttpClientBuilder(builder: OkHttpClient.Builder): OkHttpClient.Builder {
        builder.apply {
            //设置缓存配置 缓存最大10M
            val sslContext = SSLContext.getInstance("TLS")
            sslContext.init(null, trustAllCerts, SecureRandom())
            //cache url
            val httpCacheDirectory = File(App.instance.cacheDir, "responses")
            // 50 MiB
            val cacheSize = 50 * 1024 * 1024
            val cache = Cache(httpCacheDirectory, cacheSize.toLong())
            // Create an ssl socket factory with our all-trusting manager
            val sslSocketFactory = sslContext.socketFactory
//            val okBuilder = OkHttpClient.Builder()
            sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
            readTimeout(30, TimeUnit.SECONDS)
            connectTimeout(30, TimeUnit.SECONDS)
            writeTimeout(30, TimeUnit.SECONDS)
            addInterceptor(HttpHeadInterceptor())
            // 持久化cookie
            addInterceptor(ReceivedCookiesInterceptor(App.instance))
            addInterceptor(AddCookiesInterceptor(App.instance))
            // 添加缓存，无网访问时会拿缓存,只会缓存get请求
            addInterceptor(AddCacheInterceptor(App.instance))
            cache(cache)
            addInterceptor(
                HttpLoggingInterceptor()
                    .setLevel(if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE)
            )
            hostnameVerifier { hostname, session -> true }
            build()
        }
        return builder
    }

    /**
     * 实现重写父类的setRetrofitBuilder方法，
     * 在这里可以对Retrofit.Builder做任意操作，比如添加GSON解析器，protobuf等
     */
    override fun setRetrofitBuilder(builder: Retrofit.Builder): Retrofit.Builder {
        return builder.apply {
            addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            addCallAdapterFactory(CoroutineCallAdapterFactory())
        }
    }

//    val cookieJar: PersistentCookieJar by lazy {
//        PersistentCookieJar(SetCookieCache(), SharedPrefsCookiePersistor(appContext))
//    }


    var trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
            @Throws(CertificateException::class)
            override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {
            }

            @Throws(CertificateException::class)
            override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
            }

            override fun getAcceptedIssuers(): Array<X509Certificate> {
                return arrayOf()
            }
        })

    private class AddCookiesInterceptor internal constructor(private val context: Context) : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            val builder = chain.request().newBuilder()
            val sharedPreferences = context.getSharedPreferences("config", Context.MODE_PRIVATE)
            val cookie = sharedPreferences.getString("cookie", "")
            builder.addHeader("Cookie", cookie)
            return chain.proceed(builder.build())
        }

    }

    private class HttpHeadInterceptor : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request()
            val builder = request.newBuilder()
            builder.addHeader("Accept", "application/json;versions=1")
            if (NetworkUtil.isNetworkAvailable(App.instance)) {
                val maxAge = 60
                builder.addHeader("Cache-Control", "public, max-age=$maxAge")
            } else {
                val maxStale = 60 * 60 * 24 * 28
                builder.addHeader("Cache-Control", "public, only-if-cached, max-stale=$maxStale")
            }
            return chain.proceed(builder.build())
        }
    }

    private class AddCacheInterceptor internal constructor(private val context: Context) :
        Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            val cacheBuilder = CacheControl.Builder()
            cacheBuilder.maxAge(0, TimeUnit.SECONDS)
            cacheBuilder.maxStale(365, TimeUnit.DAYS)
            val cacheControl = cacheBuilder.build()
            var request = chain.request()
            if (!NetworkUtil.isNetworkAvailable(context)) {
                request = request.newBuilder()
                    .cacheControl(cacheControl)
                    .build()
            }
            val originalResponse = chain.proceed(request)
            return if (NetworkUtil.isNetworkAvailable(context)) {
                // read from cache
                val maxAge = 0
                originalResponse.newBuilder()
                    .removeHeader("Pragma")
                    .header("Cache-Control", "public ,max-age=$maxAge")
                    .build()
            } else {
                // tolerate 4-weeks stale
                val maxStale = 60 * 60 * 24 * 28
                originalResponse.newBuilder()
                    .removeHeader("Pragma")
                    .header("Cache-Control", "public, only-if-cached, max-stale=$maxStale")
                    .build()
            }
        }

    }

    private class ReceivedCookiesInterceptor internal constructor(private val context: Context) :
        Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            val originalResponse = chain.proceed(chain.request())
            //这里获取请求返回的cookie
            if (!originalResponse.headers("Set-Cookie").isEmpty()) {
                val d =
                    originalResponse.headers("Set-Cookie")
                //                Log.e("jing", "------------得到的 cookies:" + d.toString());

                // 返回cookie
                if (!TextUtils.isEmpty(d.toString())) {
                    val sharedPreferences =
                        context.getSharedPreferences("config", Context.MODE_PRIVATE)
                    val editorConfig = sharedPreferences.edit()
                    val oldCookie = sharedPreferences.getString("cookie", "")
                    val stringStringHashMap = HashMap<String, String>()

                    // 之前存过cookie
                    if (!TextUtils.isEmpty(oldCookie)) {
                        val substring = oldCookie!!.split(";".toRegex()).toTypedArray()
                        for (aSubstring in substring) {
                            if (aSubstring.contains("=")) {
                                val split = aSubstring.split("=".toRegex()).toTypedArray()
                                stringStringHashMap[split[0]] = split[1]
                            } else {
                                stringStringHashMap[aSubstring] = ""
                            }
                        }
                    }
                    val join: String = StringUtils.join(d, ";")
                    val split = join.split(";".toRegex()).toTypedArray()

                    // 存到Map里
                    for (aSplit in split) {
                        val split1 = aSplit.split("=".toRegex()).toTypedArray()
                        if (split1.size == 2) {
                            stringStringHashMap[split1[0]] = split1[1]
                        } else {
                            stringStringHashMap[split1[0]] = ""
                        }
                    }

                    // 取出来
                    val stringBuilder = StringBuilder()
                    if (stringStringHashMap.size > 0) {
                        for (key in stringStringHashMap.keys) {
                            stringBuilder.append(key)
                            val value = stringStringHashMap[key]
                            if (!TextUtils.isEmpty(value)) {
                                stringBuilder.append("=")
                                stringBuilder.append(value)
                            }
                            stringBuilder.append(";")
                        }
                    }
                    editorConfig.putString("cookie", stringBuilder.toString())
                    editorConfig.apply()
                    //                    Log.e("jing", "------------处理后的 cookies:" + stringBuilder.toString());
                }
            }
            return originalResponse
        }

    }

}
