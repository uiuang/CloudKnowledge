package com.uiuang.cloudknowledge.viewmodel.request

import androidx.lifecycle.MutableLiveData
import com.uiuang.cloudknowledge.app.http.getGankIOServer
import com.uiuang.cloudknowledge.app.state.ListDataUiState
import com.uiuang.cloudknowledge.bean.gank.GankIOResultBean
import com.uiuang.mvvm.base.viewmodel.BaseViewModel
import com.uiuang.mvvm.ext.request

class RequestGankViewModel : BaseViewModel() {
    private var pageNo = 1
    private var countNo = 20
    private var category: String = "GanHuo"


    var sisterDataState: MutableLiveData<ListDataUiState<GankIOResultBean>> = MutableLiveData()

    fun loadGankData(isRefresh: Boolean, mType: String?) {
        if (isRefresh) {
            pageNo = 1
        }
        request({ getGankIOServer.getGankIoData(category, mType, pageNo, countNo) }, {
            pageNo++
            val size = it.size
            val over: Boolean = size == countNo
            val listDataUiState =
                ListDataUiState(
                    isSuccess = true,
                    isRefresh = isRefresh,
                    isEmpty = it.isEmpty(),
                    hasMore = over,
                    isFirstEmpty = isRefresh && it.isEmpty(),
                    listData = it
                )
            sisterDataState.postValue(listDataUiState)
        }, {
            //请求失败
            val listDataUiState =
                ListDataUiState(
                    isSuccess = false,
                    errMessage = it.errorMsg,
                    isRefresh = isRefresh,
                    listData = arrayListOf<GankIOResultBean>()
                )
            sisterDataState.postValue(listDataUiState)
        })


    }
}