package com.uiuang.cloudknowledge.viewmodel.state

import com.uiuang.mvvm.base.viewmodel.BaseViewModel


/**
 * @Title:
 * @Description:
 * @author zsc
 * @date 2020/9/3 20:26
 */
class WebViewModel : BaseViewModel() {
    //是否收藏
    var collect = false

    //收藏的Id
    var articleId = 0

    //标题
    var showTitle: String = ""

    //文章的网络访问路径
    var url: String = ""

    //需要收藏的类型 具体参数说明请看 CollectType 枚举类
    var collectType = 0
}