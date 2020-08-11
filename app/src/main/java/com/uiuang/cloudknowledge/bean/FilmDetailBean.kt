package com.uiuang.cloudknowledge.bean

data class FilmDetailBean(
    val advertisement: AdvertisementBean,
    val basic: Basic,
    val boxOffice: BoxOffice,
    val live: Live,
    val playState: String,
    val playlist: List<PlayListBean>,//在线观影
    val related: Related
){

    data class PlayListBean(
        val isOpenByBrowser: Boolean = false,
        val payRule: String,
        val picUrl: String,
        val playSourceName: String,
        val playUrl: String,
        val playUrlH5: String,
        val playUrlPC: String,
        val sourceId: String
    )


    data class AdvertisementBean(
        val advList: List<Any>,
        val count: Int,
        val error: String,
        val success: Boolean
    )


    data class Basic(
        val actors: MutableList<Actor>,
        val attitude: Int,
        val award: Award,
        val bigImage: String,
        val broadcastDes: String,
        val commentSpecial: String,
        val community: Community,
        val director: Director,
        val eggDesc: String,
        val episodeCnt: String,
        val festivals: List<Any>,
        val hasSeenCount: Int,
        val hasSeenCountShow: String,
        val hotRanking: Int,
        val img: String,
        val is3D: Boolean,
        val isDMAX: Boolean,
        val isEggHunt: Boolean,
        val isFavorite: Int,
        val isFilter: Boolean,
        val isIMAX: Boolean,
        val isIMAX3D: Boolean,
        val isTicket: Boolean,
        val message: String,
        val mins: String,
        val movieId: Int,
        val movieStatus: Int,
        val name: String,
        val nameEn: String,
        val overallRating: String,
        val personCount: Int,
        val quizGame: QuizGame,
        val ratingCount: Int,
        val ratingCountShow: String,
        val releaseArea: String,
        val releaseDate: String,
        val sensitiveStatus: Boolean,
        val showCinemaCount: Int,
        val showDay: Int,
        val showtimeCount: Int,
        val stageImg: StageImg,
        val story: String,//简介
        val style: Style,
        val summary: String,
        val totalNominateAward: Int,
        val totalWinAward: Int,
        val type: List<String>,//类型
        val url: String,
        val userComment: String,
        val userCommentId: Int,
        val userImg: String,
        val userName: String,
        val userRating: Int,
        val video: Video,
        val wantToSeeCount: Int,
        val wantToSeeCountShow: String,
        val wantToSeeNumberShow: String,
        val year: String
    ){
        /**
         * 演员表
         */
        data class Actor(
            val actorId: Int,
            val img: String,
            val name: String,
            val nameEn: String,
            val roleImg: String,
            val roleName: String
        )

        data class Award(
            val awardList: List<Any>,
            val totalNominateAward: Int,//提名
            val totalWinAward: Int//获奖
        )

        class Community(
        )

        data class Director(
            val directorId: Int,
            val img: String,
            val name: String,
            val nameEn: String
        )

        class QuizGame(
        )

        /**
         * 剧照
         */
        data class StageImg(
            val count: Int,
            val list: List<Any>
        )

        data class Style(
            val isLeadPage: Int,
            val leadImg: String,
            val leadUrl: String
        )

        data class Video(
            val count: Int,
            val hightUrl: String,
            val img: String,
            val title: String,
            val url: String,
            val videoId: Int,
            val videoSourceType: Int
        )
    }

    /**
     * 累计票房
     */
    data class BoxOffice(
        val movieId: Int,
        val ranking: Int,//今日票房排名
        val todayBox: String,
        val todayBoxDes: String,//今日实时
        val todayBoxDesUnit: String,
        val totalBox: String,//累计票房
        val totalBoxDes: String,
        val totalBoxUnit: String
    )

    data class Live(
        val count: Int,
        val img: String,
        val isSubscribe: Boolean,
        val liveId: Int,
        val playNumTag: String,
        val playTag: String,
        val status: Int,
        val title: String
    )

    data class Related(
        val goodsCount: Int,
        val goodsList: List<Any>,
        val relateId: Int,
        val relatedUrl: String,
        val type: Int
    )


}



