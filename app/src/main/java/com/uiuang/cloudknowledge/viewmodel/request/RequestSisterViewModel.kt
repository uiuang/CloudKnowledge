package com.uiuang.cloudknowledge.viewmodel.request

import androidx.lifecycle.MutableLiveData
import com.uiuang.cloudknowledge.app.http.getGankIOServer
import com.uiuang.cloudknowledge.app.state.ListDataUiState
import com.uiuang.cloudknowledge.bean.GankIOResultBean
import com.uiuang.cloudknowledge.bean.GankIoDataBean
import com.uiuang.mvvm.base.viewmodel.BaseViewModel
import com.uiuang.mvvm.ext.request


/**
 * @Title:
 * @Description:
 * @author zsc
 * @date 2020/8/2 21:46
 */
class RequestSisterViewModel : BaseViewModel() {
    //页码，体系 广场的页码是从0开始的
    private var pageNo = 1
    var plazaDataState: MutableLiveData<ListDataUiState<GankIOResultBean>> = MutableLiveData()

    /**
     * 获取广场数据
     */
    fun getPlazaData(isRefresh: Boolean) {
        if (isRefresh) {
            pageNo = 1
        }
        request({ getGankIOServer.getGankIoData("Girl", "Girl",pageNo, 20) }, {
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
            plazaDataState.postValue(listDataUiState)
        }, {
            //请求失败
            val listDataUiState =
                ListDataUiState(
                    isSuccess = false,
                    errMessage = it.errorMsg,
                    isRefresh = isRefresh,
                    listData = arrayListOf<GankIOResultBean>()
                )
            plazaDataState.postValue(listDataUiState)
        })
    }
}