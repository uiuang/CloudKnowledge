package com.uiuang.cloudknowledge.app

import androidx.multidex.MultiDex
import com.kingja.loadsir.callback.SuccessCallback
import com.kingja.loadsir.core.LoadSir
import com.tencent.mmkv.MMKV
import com.uiuang.cloudknowledge.weight.loadCallBack.EmptyCallback
import com.uiuang.cloudknowledge.weight.loadCallBack.ErrorCallback
import com.uiuang.cloudknowledge.weight.loadCallBack.LoadingCallback
import com.uiuang.mvvm.base.BaseApp


/**
 * @Title:
 * @Description:
 * @author zsc
 * @date 2020/8/1 22:27
 */
class App : BaseApp() {

    companion object {
        lateinit var instance: App
    }
    override fun onCreate() {
        super.onCreate()
        instance = this
        MultiDex.install(this)
        MMKV.initialize(this.filesDir.absolutePath + "/mmkv")
        //界面加载管理 初始化
        LoadSir.beginBuilder()
            .addCallback(LoadingCallback())//加载
            .addCallback(ErrorCallback())//错误
            .addCallback(EmptyCallback())//空
            .setDefaultCallback(SuccessCallback::class.java)//设置默认加载状态页
            .commit()
    }
}