package com.uiuang.cloudknowledge.viewmodel.request

import androidx.lifecycle.MutableLiveData
import com.uiuang.cloudknowledge.app.http.getWanAndroidServer
import com.uiuang.cloudknowledge.app.state.ListDataUiState
import com.uiuang.cloudknowledge.bean.IntegralBean
import com.uiuang.mvvm.base.viewmodel.BaseViewModel
import com.uiuang.mvvm.ext.request
import com.uiuang.mvvm.network.AppException
import com.uiuang.mvvm.state.ResultState

class RequestMineViewModel : BaseViewModel() {
    var integralBean = MutableLiveData<ResultState<IntegralBean>>()

    fun getIntegral() {
        request({ getWanAndroidServer.getIntegral() }, integralBean)
    }
}