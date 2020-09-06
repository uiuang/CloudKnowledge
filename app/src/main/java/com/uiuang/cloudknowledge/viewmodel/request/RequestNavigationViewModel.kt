package com.uiuang.cloudknowledge.viewmodel.request

import androidx.lifecycle.MutableLiveData
import com.uiuang.cloudknowledge.app.http.getWanAndroidServer
import com.uiuang.cloudknowledge.app.state.ListDataUiState
import com.uiuang.cloudknowledge.bean.NavJsonBean
import com.uiuang.cloudknowledge.bean.TabBean
import com.uiuang.mvvm.base.viewmodel.BaseViewModel
import com.uiuang.mvvm.ext.request


/**
 * @Title:
 * @Description:
 * @author zsc
 * @date 2020/9/6 10:58
 */
class RequestNavigationViewModel : BaseViewModel() {
    var navJsonBeanList: MutableLiveData<MutableList<NavJsonBean>> = MutableLiveData()

    var navJsonBean: MutableLiveData<ListDataUiState<NavJsonBean>> = MutableLiveData()

    fun getNavigationJson() {
        request({ getWanAndroidServer.getNavJson()},{
            navJsonBeanList.postValue(it)
            val listDataUiState =
                ListDataUiState(
                    isSuccess = true,
                    listData = it
                )
            navJsonBean.postValue(listDataUiState)
        },{
            navJsonBeanList.postValue(arrayListOf<NavJsonBean>())
            val listDataUiState =
                ListDataUiState(
                    isSuccess = false,
                    errMessage = it.errorMsg,
                    listData = arrayListOf<NavJsonBean>()
                )
            navJsonBean.postValue(listDataUiState)
        })
    }
}