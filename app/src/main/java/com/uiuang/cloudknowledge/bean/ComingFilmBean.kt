package com.uiuang.cloudknowledge.bean

import java.util.*
import kotlin.collections.ArrayList

/**
 * @author jingbin
 */
class ComingFilmBean {
    var attention: ArrayList<MoviecomingsBean>? = null
    var moviecomings: ArrayList<MoviecomingsBean>? = null

    class AttentionBean {
        /**
         * actor1 : 乔什·盖德
         * actor2 : 丹尼斯·奎德
         * director : 盖尔·曼库索
         * id : 257987
         * image : http://img5.mtime.cn/mt/2019/05/09/154533.34451761_1280X720X2.jpg
         * isFilter : false
         * isTicket : true
         * isVideo : true
         * locationName : 美国
         * rDay : 17
         * rMonth : 5
         * rYear : 2019
         * releaseDate : 5月17日上映
         * title : 一条狗的使命2
         * type : 冒险 / 喜剧 / 家庭
         * videoCount : 3
         * videos : [{"hightUrl":"","image":"http://img5.mtime.cn/mg/2019/01/16/165549.62375299.jpg","length":42,"title":"一条狗的使命2 回忆杀版预告","url":"http://vfx.mtime.cn/Video/2019/01/16/mp4/190116170107467170.mp4","videoId":73436},{"hightUrl":"","image":"http://img5.mtime.cn/mg/2019/01/30/114104.33664273.jpg","length":169,"title":"一条狗的使命2 正式预告","url":"http://vfx.mtime.cn/Video/2019/01/30/mp4/190130114131977438.mp4","videoId":73617},{"hightUrl":"","image":"http://img5.mtime.cn/mg/2019/01/30/115000.20867247.jpg","length":172,"title":"一条狗的使命2 中字正式预告","url":"http://vfx.mtime.cn/Video/2019/01/30/mp4/190130115053930472.mp4","videoId":73619}]
         * wantedCount : 78
         */
        var actor1: String? = null
        var actor2: String? = null
        var director: String? = null
        var id = 0
        var image: String? = null
        var isVideo:Boolean = false

        var locationName: String? = null
        var rDay = 0
        var rMonth = 0
        var rYear = 0
        var releaseDate: String? = null
        var title: String? = null
        var type: String? = null
        var videoCount = 0
        var wantedCount = 0
        var videos: List<VideosBean>? = null



    }

    class MoviecomingsBean {
        /**
         * actor1 : 阿努克·斯戴芬
         * actor2 : 布鲁诺·甘茨
         * director : 阿兰·葛斯彭纳
         * id : 224984
         * image : http://img5.mtime.cn/mt/2019/05/10/174455.88180422_1280X720X2.jpg
         * isFilter : false
         * isTicket : true
         * isVideo : true
         * locationName : 德国
         * rDay : 16
         * rMonth : 5
         * rYear : 2019
         * releaseDate : 5月16日上映
         * title : 海蒂和爷爷
         * type : 家庭 / 儿童
         * videoCount : 3
         * videos : [{"hightUrl":"","image":"http://img5.mtime.cn/mg/2019/05/10/154325.59927342.jpg","length":103,"title":"海蒂和爷爷 \u201c飘零\u201d版预告","url":"http://vfx.mtime.cn/Video/2019/05/10/mp4/190510173319321869.mp4","videoId":74671},{"hightUrl":"","image":"http://img5.mtime.cn/mg/2019/04/25/092522.71052778.jpg","length":111,"title":"海蒂和爷爷 定档预告","url":"http://vfx.mtime.cn/Video/2019/04/25/mp4/190425092604550352.mp4","videoId":74509},{"hightUrl":"","image":"http://img5.mtime.cn/mg/2017/09/21/163748.34327337.jpg","length":86,"title":"海蒂与爷爷 美国预告片","url":"http://vfx.mtime.cn/Video/2017/09/21/mp4/170921163944703745.mp4","videoId":67742}]
         * wantedCount : 184
         */
        var actor1: String? = null
        var actor2: String? = null
        var director: String? = null
        var id = 0
        var image: String? = null
        var isFilter:Boolean = false
        var isTicket :Boolean= false
        var isVideo :Boolean= false
        var locationName: String? = null
        var rDay = 0
        var rMonth = 0
        var rYear = 0
        var releaseDate: String? = null
        var title: String? = null
        var type: String? = null
        var videoCount = 0
        var wantedCount = 0
        var videos: List<VideosBean>? = null



        override fun equals(o: Any?): Boolean {
            if (this === o) {
                return true
            }
            if (o == null || javaClass != o.javaClass) {
                return false
            }
            val bean = o as MoviecomingsBean
            return id == bean.id
        }

        override fun hashCode(): Int {
            return Objects.hash(id)
        }
    }

    class VideosBean {
        /**
         * hightUrl :
         * image : http://img5.mtime.cn/mg/2019/01/16/165549.62375299.jpg
         * length : 42
         * title : 一条狗的使命2 回忆杀版预告
         * url : http://vfx.mtime.cn/Video/2019/01/16/mp4/190116170107467170.mp4
         * videoId : 73436
         */
        var hightUrl: String? = null
        var image: String? = null
        var length = 0
        var title: String? = null
        var url: String? = null
        var videoId = 0

    }
}