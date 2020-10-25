package com.uiuang.cloudknowledge.viewmodel.request

import androidx.lifecycle.MutableLiveData
import com.uiuang.cloudknowledge.app.http.getWanAndroidServer
import com.uiuang.cloudknowledge.app.state.CollectUiState
import com.uiuang.cloudknowledge.app.state.ListDataUiState
import com.uiuang.cloudknowledge.bean.ArticlesBean
import com.uiuang.mvvm.base.viewmodel.BaseViewModel
import com.uiuang.mvvm.ext.request

/**
 * 专门为了收藏而写的ViewModel
 */
class RequestCollectViewModel : BaseViewModel() {
    private var pageNo = 0

    //收藏文章
    val collectUiState: MutableLiveData<CollectUiState> = MutableLiveData()

    //收藏网址
    val collectUrlUiState: MutableLiveData<CollectUiState> = MutableLiveData()

    //收藏de文章数据
    var articleDataState: MutableLiveData<ListDataUiState<ArticlesBean>> = MutableLiveData()

    //收藏de网址数据
    var urlDataState: MutableLiveData<ListDataUiState<ArticlesBean>> = MutableLiveData()

    /**
     * 收藏 文章
     * 提醒一下，玩安卓的收藏 和取消收藏 成功后返回的data值为null，所以在CollectRepository中的返回值一定要加上 非空？
     * 不然会出错
     */
    fun collect(id: Int) {
        request({ getWanAndroidServer.collect(id) }, {
            val uiState = CollectUiState(isSuccess = true, collect = true, id = id)
            collectUiState.value = uiState
        }, {
            val uiState =
                CollectUiState(isSuccess = false, collect = false, errorMsg = it.errorMsg, id = id)
            collectUiState.value = uiState
        })
    }

    /**
     * 取消收藏文章
     * 提醒一下，玩安卓的收藏 和取消收藏 成功后返回的data值为null，所以在CollectRepository中的返回值一定要加上 非空？
     * 不然会出错
     */
    fun unCollect(id: Int) {
        request({ getWanAndroidServer.unCollect(id) }, {
            val uiState = CollectUiState(isSuccess = true, collect = false, id = id)
            collectUiState.value = uiState
        }, {
            val uiState =
                CollectUiState(isSuccess = false, collect = true, errorMsg = it.errorMsg, id = id)
            collectUiState.value = uiState
        })
    }

    /**
     * 收藏 文章
     * 提醒一下，玩安卓的收藏 和取消收藏 成功后返回的data值为null，所以在CollectRepository中的返回值一定要加上 非空？
     * 不然会出错
     */
    fun collectUrl(name: String, link: String) {
        request({ getWanAndroidServer.collectUrl(name, link) }, {
            val uiState = CollectUiState(isSuccess = true, collect = true, id = it.id)
            collectUrlUiState.value = uiState
        }, {
            val uiState =
                CollectUiState(isSuccess = false, collect = false, errorMsg = it.errorMsg, id = 0)
            collectUrlUiState.value = uiState
        })
    }

    /**
     * 取消收藏网址
     * 提醒一下，玩安卓的收藏 和取消收藏 成功后返回的data值为null，所以在CollectRepository中的返回值一定要加上 非空？
     * 不然会出错
     */
    fun unCollectUrl(id: Int) {
        request({ getWanAndroidServer.delCollectUrl(id) }, {
            val uiState = CollectUiState(isSuccess = true, collect = false, id = id)
            collectUrlUiState.value = uiState
        }, {
            val uiState =
                CollectUiState(isSuccess = false, collect = true, errorMsg = it.errorMsg, id = id)
            collectUrlUiState.value = uiState
        })
    }

    fun getCollectArticleData(isRefresh: Boolean) {
        if (isRefresh) {
            pageNo = 0
        }
        request({ getWanAndroidServer.getCollectData(pageNo) }, {
            //请求成功
            pageNo++
            val listDataUiState =
                ListDataUiState(
                    isSuccess = true,
                    isRefresh = isRefresh,
                    isEmpty = it.isEmpty(),
                    hasMore = it.hasMore(),
                    isFirstEmpty = isRefresh && it.isEmpty(),
                    listData = it.datas
                )
            articleDataState.value = listDataUiState
        }, {
            //请求失败
            val listDataUiState =
                ListDataUiState(
                    isSuccess = false,
                    errMessage = it.errorMsg,
                    isRefresh = isRefresh,
                    listData = arrayListOf<ArticlesBean>()
                )
            articleDataState.value = listDataUiState
        })
    }

    fun getCollectUrlData() {
        request({ getWanAndroidServer.getCollectUrlData() }, { list ->
            //请求成功
            list.map {
                if (it.order == 0) {
                    it.order = 1
                }
            }
            val listDataUiState =
                ListDataUiState(
                    isRefresh = true,
                    isSuccess = true,
                    hasMore = false,
                    isEmpty = list.isEmpty(),
                    listData = list
                )
            urlDataState.value = listDataUiState
        }, {
            //请求失败
            val listDataUiState =
                ListDataUiState(
                    isSuccess = false,
                    errMessage = it.errorMsg,
                    listData = arrayListOf<ArticlesBean>()
                )
            urlDataState.value = listDataUiState
        })
    }
}