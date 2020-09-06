package com.uiuang.cloudknowledge.bean


/**
 * @Title:
 * @Description:
 * @author zsc
 * @date 2020/9/6 11:03
 */
class NavJsonBean(
    var selected: Boolean = false,
    var cid: Int = 0,
    var name: String? = null,
    var articles: MutableList<ArticlesBean>? = null
) {
}