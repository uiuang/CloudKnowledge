package com.uiuang.cloudknowledge.viewmodel.state

import com.uiuang.mvvm.base.viewmodel.BaseViewModel
import com.uiuang.mvvm.callback.databind.StringObservableField


/**
 * 作者　: hegaojian
 * 时间　: 2020/3/11
 * 描述　:
 */
class ArticleViewModel : BaseViewModel() {

    //分享文章标题
    var shareTitle = StringObservableField()

    //分享文章网址
    var shareUrl = StringObservableField()

    //分享文章的人
    var shareName = StringObservableField()

}