package com.uiuang.cloudknowledge.viewmodel.state

import com.uiuang.mvvm.base.viewmodel.BaseViewModel
import com.uiuang.mvvm.callback.databind.BooleanObservableField
import com.uiuang.mvvm.callback.databind.StringObservableField
import com.uiuang.mvvm.callback.livedata.StringLiveData

class LoginRegisterViewModel : BaseViewModel() {
    //用户名
    var username = StringLiveData()

    //密码(登录注册界面)
    var password = StringObservableField()

    var password2 = StringObservableField()

    //是否显示明文密码（登录注册界面）
    var isShowPwd = BooleanObservableField()

    var isShowPwd2 = BooleanObservableField()
}