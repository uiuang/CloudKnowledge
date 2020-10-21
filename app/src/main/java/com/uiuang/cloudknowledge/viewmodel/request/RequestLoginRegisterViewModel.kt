package com.uiuang.cloudknowledge.viewmodel.request

import androidx.lifecycle.MutableLiveData
import com.uiuang.cloudknowledge.app.http.getWanAndroidServer
import com.uiuang.cloudknowledge.bean.ApiResponse
import com.uiuang.cloudknowledge.bean.UserInfo
import com.uiuang.mvvm.base.viewmodel.BaseViewModel
import com.uiuang.mvvm.ext.request
import com.uiuang.mvvm.network.AppException
import com.uiuang.mvvm.state.ResultState

class RequestLoginRegisterViewModel : BaseViewModel() {
    var loginResult = MutableLiveData<ResultState<UserInfo>>()


    fun loginReq(username: String, password: String) {
        //1.这种是在 Activity/Fragment的监听回调中拿到已脱壳的数据（项目有基类的可以用）
        request(
            { getWanAndroidServer.login(username, password) }//请求体
            , loginResult,//请求的返回结果，请求成功与否都会改变该值，在Activity或fragment中监听回调结果，具体可看loginActivity中的回调
            true,//是否显示等待框，，默认false不显示 可以默认不传
            "正在登录中..."//等待框内容，可以默认不填请求网络中...
        )

    }

    fun registerAndLogin(username: String, password: String) {
        request(
            { register(username, password) }
            , loginResult,
            true,
            "正在注册中..."
        )
    }

    /**
     * 注册并登陆
     */
    suspend fun register(username: String, password: String): ApiResponse<UserInfo> {
        val registerData = getWanAndroidServer.register(username, password, password)
        //判断注册结果 注册成功，调用登录接口
        if (registerData.isSuccess()) {
            return getWanAndroidServer.login(username, password)
        } else {
            //抛出错误异常
            throw AppException(registerData.errorCode, registerData.errorMsg)
        }
    }

}