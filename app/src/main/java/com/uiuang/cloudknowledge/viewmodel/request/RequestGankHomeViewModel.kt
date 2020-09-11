package com.uiuang.cloudknowledge.viewmodel.request

import androidx.lifecycle.MutableLiveData
import com.uiuang.cloudknowledge.app.http.getGankIOServer
import com.uiuang.cloudknowledge.app.state.ListDataUiState
import com.uiuang.cloudknowledge.bean.GankIOResultBean
import com.uiuang.cloudknowledge.utils.TimeUtil
import com.uiuang.mvvm.base.viewmodel.BaseViewModel
import com.uiuang.mvvm.ext.request

class RequestGankHomeViewModel : BaseViewModel() {
    var isShowLoading = MutableLiveData<Boolean>()
    var contentData: MutableLiveData<ListDataUiState<GankIOResultBean>> = MutableLiveData()
    var bannerData: MutableLiveData<ListDataUiState<GankIOResultBean>> = MutableLiveData()

    fun getData() {
        getBanner()
        getContentData()
    }

    private fun getBanner() {
        isShowLoading.postValue(true)
        request({ getGankIOServer.getGankBanner() }, {
            val listDataUiState =
                ListDataUiState(
                    isSuccess = true,
                    listData = it
                )
            bannerData.postValue(listDataUiState)
        }, {
//            bannerData.postValue(arrayListOf())
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

    /**
     * 获取当天日期
     */
    fun getTodayTime(): ArrayList<String> {
        val data: String = TimeUtil.getData()
        val split = data.split("-").toTypedArray()
        val year = split[0]
        val month = split[1]
        val day = split[2]
        val list = ArrayList<String>()
        list.add(year)
        list.add(month)
        list.add(day)
        return list
    }

}