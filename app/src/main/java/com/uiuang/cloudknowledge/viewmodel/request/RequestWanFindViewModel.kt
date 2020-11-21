package com.uiuang.cloudknowledge.viewmodel.request

import androidx.lifecycle.MutableLiveData
import com.uiuang.cloudknowledge.app.http.getWanAndroidServer
import com.uiuang.cloudknowledge.app.state.ListDataUiState
import com.uiuang.cloudknowledge.bean.wan.ArticlesBean
import com.uiuang.cloudknowledge.bean.wan.ChildrenBean
import com.uiuang.cloudknowledge.bean.wan.TabBean
import com.uiuang.cloudknowledge.utils.SettingUtil
import com.uiuang.mvvm.base.viewmodel.BaseViewModel
import com.uiuang.mvvm.ext.request

class RequestWanFindViewModel : BaseViewModel() {
    var pageNo: Int = 1

    var dataTitle: MutableLiveData<MutableList<ChildrenBean>> = MutableLiveData()

    var articlesBean: MutableLiveData<ListDataUiState<ArticlesBean>> = MutableLiveData()

    fun getWxArticle() {
        request({ getWanAndroidServer.getAccountTabList() }, {
            if (it.size > 0)
                dataTitle.value = it
            else dataTitle.value = null
        }, {
            dataTitle.value = null
        })
    }

    fun getWxArticleDetail(isRefresh: Boolean, id: Int) {
        if (isRefresh) {
            pageNo = 1
        }
        request({ getWanAndroidServer.getAccountList(id, pageNo) }, {
            pageNo++
            val listDataUiState =
                ListDataUiState(
                    isSuccess = true,
                    isRefresh = isRefresh,
                    isEmpty = it.isEmpty(),
                    hasMore = it.hasMore(),
                    isFirstEmpty = isRefresh && it.isEmpty(),
                    listData = it.datas
                )
            articlesBean.postValue(listDataUiState)
        }, {
            //请求失败
            val listDataUiState =
                ListDataUiState(
                    isSuccess = false,
                    errMessage = it.errorMsg,
                    isRefresh = isRefresh,
                    listData = arrayListOf<ArticlesBean>()
                )
            articlesBean.postValue(listDataUiState)
        })
    }

    fun handleCustomData(treeBean: MutableList<TabBean>?, position: Int): Boolean =
        if (!treeBean.isNullOrEmpty()) {
            SettingUtil.setFindPosition(position)
            pageNo = 1
            dataTitle.value = treeBean[position].children
            true
        } else {
            SettingUtil.removeFindPosition()
            false
        }

}