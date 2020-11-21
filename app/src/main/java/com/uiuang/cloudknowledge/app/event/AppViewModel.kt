package com.uiuang.cloudknowledge.app.event

import com.uiuang.cloudknowledge.bean.wan.UserInfo
import com.uiuang.cloudknowledge.utils.CacheUtil
import com.uiuang.cloudknowledge.utils.SettingUtil
import com.uiuang.mvvm.base.appContext
import com.uiuang.mvvm.base.viewmodel.BaseViewModel
import com.uiuang.mvvm.callback.livedata.UnPeekLiveData


/**
 * @Title:
 * @Description:
 * @author zsc
 * @date 2020/8/1 22:57
 */
class AppViewModel : BaseViewModel() {
    //App的账户信息
    var userinfo = UnPeekLiveData<UserInfo>()

    //App主题颜色 中大型项目不推荐以这种方式改变主题颜色，比较繁琐耦合，且容易有遗漏某些控件没有设置主题色
    var appColor = UnPeekLiveData<Int>()

    //App 列表动画
    var appAnimation = UnPeekLiveData<Int>()

    var findPosition = UnPeekLiveData<Int>()

    init {
        //默认值保存的账户信息，没有登陆过则为null
        userinfo.value = CacheUtil.getUser()
        //默认值颜色
        appColor.value = SettingUtil.getColor(appContext)
        //初始化列表动画
        appAnimation.value = SettingUtil.getListMode()

        findPosition.value = -1
    }
}