package com.uiuang.cloudknowledge.app.http

import com.uiuang.cloudknowledge.bean.*
import retrofit2.http.*


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
    suspend fun getHotFilm(): FilmApiResponse<FilmApiPagerResponse<ArrayList<FilmItemBean>>>

    /**
     * 时光网即将上映电影
     * https://ticket-api-m.mtime.cn//movie/mobilemoviecoming.api?locationId=295
     *moviecomings 日期排序
     * recommends最受关注
     */
    @GET("movie/mobilemoviecoming.api?locationId=561")
    suspend fun getComingFilm(): FilmApiResponse<ComingFilmBean>

    /**
     * 获取电影详情
     * FilmDetailBasicBean 561为武汉地区
     * 295为合肥地区
     *
     * @param movieId 电影bean里的id
     * https://ticket-api-m.mtime.cn/movie/detail.api?locationId=295&movieId=232770
     * movie/timeNewsList.api?movieId=232770
     * 232770
     */
    @GET("movie/detail.api?locationId=561")
    suspend fun getFilmDetail(@Query("movieId") movieId: Int): FilmApiResponse<FilmDetailBean>


    /**
     * 全球票房排行榜
     *https://api-m.mtime.cn
     * /TopList/TopListDetailsByRecommend.api
     * pageIndex
     * type
     * toplistId  北美 2015 内地 2069  香港 2016  台湾 2019 日本 2017 韩国 2018
     * pageSubAreaID
     */

    /**
     * /movie/hotComment.api?movieId=232770
     * 获取电影短评
     * /movie/hotComment201905.api?movieId=232770
     * 获取短评和影评
     * /movie/video/detail.api?vId=77070
     * 视频详情
     * /movie/externalPlayInfos.api?movieId=232770
     * 简单介绍
     * /movie/extendDetail.api?movieId=232770
     * 时光原创
     * /movie/score/getShareImage.api?movieId=232770
     * 分享影评图片
     * /movie/category/video.api?movieId=232770
     * 获取所有的预告片和花絮
     */


    /**--------------------------------------------玩安卓--------------------------------------------*/

    /**
     * 玩安卓轮播图
     */
    @GET("banner/json")
    suspend fun getWanAndroidBanner(): ApiResponse<List<WanAndroidBannerBean>>

    /**
     * 玩安卓，文章列表、知识体系下的文章
     *
     * @param page 页码，从0开始
     * @param cid  体系id
     */
    @GET("article/list/{page}/json")
    suspend fun getHomeList(
        @Path("page") page: Int,
        @Query("cid") cid: Int?
    ): ApiResponse<ApiPagerResponse<ArrayList<ArticlesBean>>>


    /**
     * 玩安卓 搜索
     */
    @FormUrlEncoded
    @POST("article/query/{page}/json")
    suspend fun searchWan(
        @Path("page") page: Int,
        @Field("k") k: String?
    ): ApiResponse<ApiPagerResponse<ArticlesBean>>

    /**
     * 搜索热词
     */
    @GET("hotkey/json")
    suspend fun getHotkey(): ApiResponse<SearchTagBean>

    /**
     * 玩安卓，首页第二tab 项目；列表
     *
     * @param page 页码，从0开始
     */
    @GET("article/listproject/{page}/json")
    suspend fun getProjectList(@Path("page") page: Int): ApiResponse<ApiPagerResponse<ArrayList<ArticlesBean>>>

    /**
     *  体系数据
     */
    @GET("/tree/json")
    suspend fun getTreeList(): ApiResponse<MutableList<TabBean>>

    /**
     * 获取项目tab
     */
    @GET("/project/tree/json")
    suspend fun getProjectTabList(): ApiResponse<MutableList<TabBean>>

    /**
     * 获取公众号列表
     */
    @GET("wxarticle/chapters/json")
    suspend fun getAccountTabList(): ApiResponse<MutableList<ChildrenBean>>

    /**
     * 单个公众号数据
     *
     * @param page 1开始
     */
    @GET("wxarticle/list/{id}/{page}/json")
    suspend fun getAccountList(
        @Path("id") id: Int,
        @Path("page") page: Int
    ): ApiResponse<ApiPagerResponse<ArrayList<ArticlesBean>>>

    /**
     * 导航数据
     */
    @GET("navi/json")
    suspend fun getNavJson(): ApiResponse<MutableList<NavJsonBean>>

    /**
     * 获取项目列表
     */
    @GET("/project/list/{pageNum}/json")
    suspend fun getProjectList(@Path("pageNum") pageNum: Int, @Query("cid") cid: Int)
            : ApiResponse<ArticlesBean>





    /**
     * 获取体系文章列表
     */
    @GET("/article/list/{pageNum}/json")
    suspend fun getSystemArticle(@Path("pageNum") pageNum: Int, @Query("cid") cid: Int)
            : ApiResponse<ArticlesBean>

    /**
     * 获取项目tab
     */
    @POST("/article/query/{pageNum}/json")
    suspend fun search(@Path("pageNum") pageNum: Int, @Query("k") k: String)
            : ApiResponse<ArticlesBean>

    /**
     * 收藏
     */
    @POST("/lg/collect/{id}/json")
    suspend fun collect(@Path("id") id: Int): ApiResponse<Any>

    /**
     * 取消收藏
     */
    @POST("/lg/uncollect_originId/{id}/json")
    suspend fun unCollect(@Path("id") id: Int): ApiResponse<Any>

    /**
     * 登录
     */
    @FormUrlEncoded
    @POST("/user/login")
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): ApiResponse<UserInfo>

    @GET("/user/logout/json")
    suspend fun logout(): ApiResponse<Any>

    /**
     * 注册
     */
    @POST("/user/register")
    suspend fun register(
        @Query("username") username: String,
        @Query("password") password: String,
        @Query("repassword") repassword: String
    ): ApiResponse<Any>

    /**--------------------------------------------干货集中营--------------------------------------------*/

    /**--------------------------------------------干货集中营-------------------------------------------- */
    /**
     * 每日数据： http://gank.io/api/day/年/月/日
     * eg:http://gank.io/api/day/2015/08/06
     */
    @GET("day/{year}/{month}/{day}")
   suspend fun getGankIoDay(
        @Path("year") year: String?,
        @Path("month") month: String?,
        @Path("day") day: String?
    ): ApiResponse<ArrayList<GankIOResultBean>>

    /**
     * 干货集中营轮播图
     */
    @GET("v2/banners")
    suspend fun getGankBanner(): ApiResponse<ArrayList<GankIOResultBean>>

    /**
     * 干货集中营本周最热
     */
    @GET("v2/hot/views/category/GanHuo/count/20")
    suspend fun getGankHot(): ApiResponse<ArrayList<GankIOResultBean>>

    /**
     * 干货集中营 搜索
     * // @GET("search/query/{keyWord}/category/{type}/count/20/page/{p}")
     * https://gank.io/api/v2/search/<search>/category/<category>/type/<type>/page/<page>/count/<count>
    </count></page></type></category></search> */
    @GET("v2/search/{keyWord}/category/GanHuo/type/{type}/page/{p}/count/20")
    fun searchGank(
        @Path("p") p: Int,
        @Path("type") type: String?,
        @Path("keyWord") keyWord: String?
    ): ApiResponse<ArrayList<GankIOResultBean>>


    /**
     * 分类数据: http://gank.io/api/data/数据类型/请求个数/第几页
     * 数据类型： 福利 | Android | iOS | 休息视频 | 拓展资源 | 前端 | all
     * 请求个数： 数字，大于0
     * 第几页：数字，大于0
     * eg: http://gank.io/api/data/Android/10/1
     * // 分类api
     * https://gank.io/api/v2/categories/<category_type>
     * https://gank.io/api/v2/categories/Article
     * // 分类数据api
     * https://gank.io/api/v2/data/category/<category>/type/<type>/page/<page>/count/<count>
     * // 旧：@GET("data/{type}/{pre_page}/{page}")
    </count></page></type></category></category_type> */
    @GET("v2/data/category/{category}/type/{type}/page/{page}/count/{count}")
    suspend fun getGankIoData(
        @Path("category") category: String,
        @Path("type") type: String,
        @Path("page") page: Int,
        @Path("count") count: Int
    ): GankApiResponse<ArrayList<GankIOResultBean>>

}