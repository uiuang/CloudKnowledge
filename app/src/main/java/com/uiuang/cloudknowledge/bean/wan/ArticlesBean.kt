package com.uiuang.cloudknowledge.bean.wan

class ArticlesBean(
    var apkLink: String? = null,
    var author: String? = null,
    var chapterId: Int = 0,
    var chapterName: String? = null,
    var collect: Boolean = false,
    var courseId: Int = 0,
    var desc: String? = null,
    var envelopePic: String? = null,
    var id: Int = 0,
    var name: String,
    var originId: Int = -1,// 收藏文章列表里面的原始文章id
    var link: String,
    var niceDate: String? = null,
    var origin: String? = null,
    var projectLink: String? = null,
    var publishTime: Long = 0,
    var title: String = "",
    var visible: Int? = 0,
    var zan: Int? = 0,
    var order: Int,
    var fresh: Boolean = false,
    var isShowImage: Boolean = true,// 分类name,
    var navigationName: String? = null, // 可能没有author 有 shareUser
    var shareUser: String? = null
)