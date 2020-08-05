package com.uiuang.cloudknowledge.app.http

import com.uiuang.cloudknowledge.bean.*
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


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
//        https://ticket-api-m.mtime.cn
    }

    @GET("v2/data/category/{category}/type/{type}/page/{page}/count/{count}")
    suspend fun getGankIoData(@Path("category")  category:String, @Path("type")  type:String, @Path("page")  page:Int, @Path("count")  count:Int): GankApiResponse<ArrayList<GankIOResultBean>>

    /**--------------------------------------------时光网--------------------------------------------*/

    /**
     * 时光网热映电影LocationMovieShowtimes
     *
     * https://ticket-api-m.mtime.cn/showing/movies.api?locationId=295 正在热映
     *
     * movie/mobilemoviecoming.api?locationId=295
     * showing/movies.api?locationId=295
     */
    @GET("showing/movies.api?locationId=561")
    suspend fun getHotFilm():FilmApiResponse<FilmApiPagerResponse<ArrayList<FilmItemBean>>>

    /**
     * 时光网即将上映电影
     * https://ticket-api-m.mtime.cn//movie/mobilemoviecoming.api?locationId=295
     *moviecomings 日期排序
     * recommends最受关注
     */
    @GET("movie/mobilemoviecoming.api?locationId=561")
    suspend fun getComingFilm(): FilmApiResponse<ArrayList<ComingFilmBean.MoviecomingsBean>>

    /**
     * 获取电影详情
     * FilmDetailBasicBean 561为武汉地区
     * 295为合肥地区
     *
     * @param movieId 电影bean里的id
     * https://ticket-api-m.mtime.cn/movie/detail.api?locationId=295&movieId=232770
     * 232770
     */
    @GET("movie/detail.api?locationId=561")
    suspend fun getFilmDetail(@Query("movieId")  movieId:Int):FilmDetailBean
}