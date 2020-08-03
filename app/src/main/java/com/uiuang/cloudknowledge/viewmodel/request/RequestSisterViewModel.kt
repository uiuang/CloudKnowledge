package com.uiuang.cloudknowledge.viewmodel.request

import androidx.lifecycle.MutableLiveData
import com.uiuang.cloudknowledge.app.http.getGankIOServer
import com.uiuang.cloudknowledge.app.state.ListDataUiState
import com.uiuang.cloudknowledge.bean.GankIOResultBean
import com.uiuang.mvvm.base.viewmodel.BaseViewModel
import com.uiuang.mvvm.ext.request


/**
 * @Title:妹子
 * @Description:专门为了Gank的妹子列表而写的ViewModel
 * @author zsc
 * @date 2020/8/2 21:46
 */
class RequestSisterViewModel : BaseViewModel() {
    //页码，是从1开始的 GankIO的妹子福利
    private var pageNo = 1
    private var countNo = 20
    //妹子福利数据
    var sisterDataState: MutableLiveData<ListDataUiState<GankIOResultBean>> = MutableLiveData()

    /**
     * 获取广场数据
     */
    fun getPlazaData(isRefresh: Boolean) {
        if (isRefresh) {
            pageNo = 1
        }
        request({ getGankIOServer.getGankIoData("Girl", "Girl",pageNo, countNo) }, {
            //请求成功
            pageNo++
            var size = it.size
            var over: Boolean= size == countNo
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