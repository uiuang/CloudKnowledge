package com.uiuang.cloudknowledge.viewmodel.request

import androidx.lifecycle.MutableLiveData
import com.uiuang.cloudknowledge.app.http.getWanAndroidServer
import com.uiuang.cloudknowledge.app.state.ListDataUiState
import com.uiuang.cloudknowledge.bean.ArticlesBean
import com.uiuang.cloudknowledge.bean.WanAndroidBannerBean
import com.uiuang.mvvm.base.viewmodel.BaseViewModel
import com.uiuang.mvvm.ext.request

class RequestWanHomeViewModel : BaseViewModel() {
    private var pageNo = 1
    private var countNo = 20
    var wanAndroidBannerBean: MutableLiveData<ListDataUiState<WanAndroidBannerBean>> =
        MutableLiveData()
    var articlesBean: MutableLiveData<ListDataUiState<ArticlesBean>> = MutableLiveData()
    fun getWanAndroidBanner() {
        request({ getWanAndroidServer.getWanAndroidBanner() }, {
            val listDataUiState =
                ListDataUiState(
                    isSuccess = true,
                    listData = it
                )
            wanAndroidBannerBean.postValue(listDataUiState)
        }, {

        })
    }

    fun getHomeArticleList(isRefresh: Boolean, cid: Int?) {
        if (isRefresh) {
            pageNo = 0
        }
        request({ getWanAndroidServer.getHomeList(pageNo, cid) }, {
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
            articlesBean.postValue(listDataUiState)
        }, {
            //请求失败
            val listDataUiState =
                ListDataUiState(
                    isSuccess = false,
                    errMessage = it.errorMsg,
                    isRefresh = isRefresh,
                    listData = arrayListOf<ArticlesBean>()
                )
            articlesBean.postValue(listDataUiState)
        })
    }

    fun getHomeProjectList(isRefresh: Boolean) {
        if (isRefresh) {
            pageNo = 0
        }
        request({ getWanAndroidServer.getProjectList(pageNo) }, {
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
            articlesBean.postValue(listDataUiState)
        }, {
            //请求失败
            val listDataUiState =
                ListDataUiState(
                    isSuccess = false,
                    errMessage = it.errorMsg,
                    isRefresh = isRefresh,
                    listData = arrayListOf<ArticlesBean>()
                )
            articlesBean.postValue(listDataUiState)
        })
    }
}