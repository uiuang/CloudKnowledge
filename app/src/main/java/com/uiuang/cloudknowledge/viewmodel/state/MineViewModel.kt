package com.uiuang.cloudknowledge.viewmodel.state

import com.uiuang.cloudknowledge.utils.ColorUtil
import com.uiuang.mvvm.base.viewmodel.BaseViewModel
import com.uiuang.mvvm.callback.databind.IntObservableField
import com.uiuang.mvvm.callback.databind.StringObservableField
import com.uiuang.mvvm.callback.livedata.UnPeekLiveData

class MineViewModel : BaseViewModel() {
    var name = StringObservableField("请先登录~")

    var integral = IntObservableField(0)

    var info = StringObservableField("id：--　排名：-")

    var imageUrl = StringObservableField(ColorUtil.randomImage())

    var testString = UnPeekLiveData<String>()
}