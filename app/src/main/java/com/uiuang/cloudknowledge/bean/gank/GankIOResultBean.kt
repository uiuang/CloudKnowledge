package com.uiuang.cloudknowledge.bean.gank


/**
 * @Title:
 * @Description:
 * @author zsc
 * @date 2020/8/2 21:50
 */

data class GankIOResultBean(
    var createdAt: String? = null,
    var desc: String? = null,
    var publishedAt: String? = null,
    var source: String? = null,
    var type: String? = null,
    var category: String? = null,
    var url: String = "",
    var used: Boolean = false,
    var author: String? = null,
    var images: List<String>? = null,
    var image: String = "",
    var title: String = ""
)