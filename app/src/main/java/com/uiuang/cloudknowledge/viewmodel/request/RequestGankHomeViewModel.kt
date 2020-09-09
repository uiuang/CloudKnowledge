package com.uiuang.cloudknowledge.viewmodel.request

import androidx.lifecycle.MutableLiveData
import com.uiuang.cloudknowledge.app.http.getGankIOServer
import com.uiuang.cloudknowledge.app.state.ListDataUiState
import com.uiuang.cloudknowledge.bean.ComingFilmBean
import com.uiuang.cloudknowledge.bean.GankIOResultBean
import com.uiuang.cloudknowledge.bean.GankIoDataBean
import com.uiuang.mvvm.base.viewmodel.BaseViewModel
import com.uiuang.mvvm.ext.request

class RequestGankHomeViewModel : BaseViewModel() {
    var isShowLoading = MutableLiveData<Boolean>()
    var contentData: MutableLiveData<ListDataUiState<GankIOResultBean>> = MutableLiveData()
    var bannerData: MutableLiveData<ArrayList<GankIOResultBean>> = MutableLiveData()

    fun getData() {
        getBanner()
        getContentData()
    }

    private fun getBanner() {
        isShowLoading.postValue(true)
        request({ getGankIOServer.getGankBanner() }, {
            bannerData.postValue(it)
        }, {
            bannerData.postValue(arrayListOf())
        })

    }

    private fun getContentData() {
        request({ getGankIOServer.getGankHot() }, {
            val listDataUiState =
                ListDataUiState(
                    isSuccess = true,
                    listData = it
                )
            contentData.postValue(listDataUiState)
            isShowLoading.postValue(false)
        }, {
            val listDataUiState =
                ListDataUiState(
                    isSuccess = false,
                    errMessage = it.errorMsg,
                    listData = arrayListOf<GankIOResultBean>()
                )
            contentData.postValue(listDataUiState)
            isShowLoading.postValue(false)
        })
    }
}