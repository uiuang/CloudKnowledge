package com.uiuang.cloudknowledge.app.http

import com.uiuang.cloudknowledge.bean.GankApiResponse
import com.uiuang.cloudknowledge.bean.GankIOResultBean
import com.uiuang.cloudknowledge.bean.ApiPagerResponse
import retrofit2.http.GET
import retrofit2.http.Path


/**
 * @Title:
 * @Description:
 * @author zsc
 * @date 2020/8/2 11:54
 */
interface ApiService {
    companion object {
        const val SERVER_URL = "https://wanandroid.com/"
        const val SERVER_URL1 = "https://wanandroid.com/"
        const val API_GANKIO = "https://gank.io/api/"
        const val API_DOUBAN = "Https://api.douban.com/"
        const val API_TING = "https://tingapi.ting.baidu.com/v1/restserver/"
        const val API_GITEE = "https://gitee.com/"
        const val API_WAN_ANDROID = "https://www.wanandroid.com/"
        const val API_QSBK = "http://m2.qiushibaike.com/"
        const val API_MTIME = "https://api-m.mtime.cn/"
        const val API_MTIME_TICKET = "https://ticket-api-m.mtime.cn/"
    }

    @GET("v2/data/category/{category}/type/{type}/page/{page}/count/{count}")
    suspend fun getGankIoData(@Path("category")  category:String, @Path("type")  type:String, @Path("page")  page:Int, @Path("count")  count:Int): GankApiResponse<ApiPagerResponse<ArrayList<GankIOResultBean>>>
}