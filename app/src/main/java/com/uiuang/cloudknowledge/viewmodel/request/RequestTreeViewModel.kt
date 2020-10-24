package com.uiuang.cloudknowledge.viewmodel.request

import androidx.lifecycle.MutableLiveData
import com.uiuang.cloudknowledge.app.http.getWanAndroidServer
import com.uiuang.cloudknowledge.app.state.ListDataUiState
import com.uiuang.cloudknowledge.bean.TabBean
import com.uiuang.mvvm.base.viewmodel.BaseViewModel
import com.uiuang.mvvm.ext.request


/**
 * @Title:
 * @Description:
 * @author zsc
 * @date 2020/9/5 19:36
 */
class RequestTreeViewModel : BaseViewModel() {
    var tabBean: MutableLiveData<ListDataUiState<TabBean>> = MutableLiveData()
    fun getTree() {
        request({ getWanAndroidServer.getTreeList() }, {
            val listDataUiState =
                ListDataUiState(
                    isSuccess = true,
                    isRefresh = true,
                    isEmpty = it.isEmpty(),
                    hasMore = true,
                    isFirstEmpty = it.isEmpty(),
                    listData = it
                )
            tabBean.postValue(listDataUiState)
        }, {
            val listDataUiState =
                ListDataUiState(
                    isSuccess = false,
                    errMessage = it.errorMsg,
                    isRefresh = true,
                    listData = arrayListOf<TabBean>()
                )
            tabBean.postValue(listDataUiState)
        })
    }
}