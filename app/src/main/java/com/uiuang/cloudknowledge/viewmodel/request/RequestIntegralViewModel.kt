package com.uiuang.cloudknowledge.viewmodel.request

import androidx.lifecycle.MutableLiveData
import com.uiuang.cloudknowledge.app.http.getWanAndroidServer
import com.uiuang.cloudknowledge.app.state.ListDataUiState
import com.uiuang.cloudknowledge.bean.wan.IntegralBean
import com.uiuang.cloudknowledge.bean.wan.IntegralHistoryBean
import com.uiuang.mvvm.base.viewmodel.BaseViewModel
import com.uiuang.mvvm.ext.request

class RequestIntegralViewModel : BaseViewModel() {
    private var pageNo = 1

    //积分排行数据
    var integralDataState = MutableLiveData<ListDataUiState<IntegralBean>>()

    //获取积分历史数据
    var integralHistoryDataState = MutableLiveData<ListDataUiState<IntegralHistoryBean>>()

    fun getIntegralData(isRefresh: Boolean) {
        if (isRefresh) {
            pageNo = 1
        }
        request({ getWanAndroidServer.getIntegralRank(pageNo) }, {
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
            integralDataState.value = listDataUiState
        }, {
            //请求失败
            val listDataUiState =
                ListDataUiState(
                    isSuccess = false,
                    errMessage = it.errorMsg,
                    isRefresh = isRefresh,
                    listData = arrayListOf<IntegralBean>()
                )
            integralDataState.value = listDataUiState
        })
    }

    fun getIntegralHistoryData(isRefresh: Boolean) {
        if (isRefresh) {
            pageNo = 1
        }
        request({ getWanAndroidServer.getIntegralHistory(pageNo) }, {
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
            integralHistoryDataState.value = listDataUiState
        }, {
            //请求失败
            val listDataUiState =
                ListDataUiState(
                    isSuccess = false,
                    errMessage = it.errorMsg,
                    isRefresh = isRefresh,
                    listData = arrayListOf<IntegralHistoryBean>()
                )
            integralHistoryDataState.value = listDataUiState
        })
    }
}