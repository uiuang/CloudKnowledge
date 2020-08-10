package com.uiuang.cloudknowledge.bean

/**
 * @author jingbin
 */
data class ComingFilmBean(
    var totalMovieComings: Int = 0,
    var recommends: RecommendsBean,
    var moviecomings: ArrayList<MoviesBean> = arrayListOf()
)

data class RecommendsBean(
    var recommendTitle: String = "",
    var movies: ArrayList<MoviesBean> = arrayListOf()
)


data class MoviesBean (

    var actors: String? = null,
    var movieId: Int = 0,
    var image: String? = null,
    var isFilter: Boolean = false,
    var isTicket: Boolean = false,
    var isVideo: Boolean = false,
    var isWantSee: Boolean = false,
    var locationName: String? = null,
    var rDay: Int = 0,
    var rMonth: Int = 0,
    var rYear: Int = 0,
    var releaseDate: String? = null,
    var title: String? = null,
    var type: String? = null,
    var videoCount: Int = 0,
    var wantedCount: Int = 0

)

data class VideosBean(
    /**
     * hightUrl :
     * image : http://img5.mtime.cn/mg/2019/01/16/165549.62375299.jpg
     * length : 42
     * title : 一条狗的使命2 回忆杀版预告
     * url : http://vfx.mtime.cn/Video/2019/01/16/mp4/190116170107467170.mp4
     * videoId : 73436
     */
    var hightUrl: String? = null,
    var image: String? = null,
    var length: Int = 0,
    var title: String? = null,
    var url: String? = null,
    var videoId: Int = 0

)

