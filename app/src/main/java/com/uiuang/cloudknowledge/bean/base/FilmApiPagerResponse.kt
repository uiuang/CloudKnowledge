package com.uiuang.cloudknowledge.bean.base

import java.io.Serializable

/**
 *  分页数据的基类
 */
data class FilmApiPagerResponse<T>(
    var ms: T,
    var lid: Int,
    var curPage: Int,
    var offset: Int,
    var over: Boolean,
    var pageCount: Int,
    var size: Int,
    var totalComingMovie: Int
) : Serializable {
    /**
     * 数据是否为空
     */
    fun isEmpty(): Boolean {

        return (ms as List<*>).size == 0
    }

    /**
     * 是否为刷新
     */
    fun isRefresh(): Boolean {
        //wanandroid 第一页该字段都为0
        return offset == 0
    }

    /**
     * 是否还有更多数据
     */
    fun hasMore(): Boolean {
        return !over
    }
}