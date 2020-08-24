package com.uiuang.cloudknowledge.ui.adapter.wan

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.uiuang.cloudknowledge.R
import com.uiuang.cloudknowledge.bean.GankIoDataBean
import com.uiuang.cloudknowledge.bean.HomeListBean

class WanAndroidAdapter(data: MutableList<HomeListBean>) :BaseQuickAdapter<HomeListBean,BaseViewHolder>(R.layout.item_film_coming, data){
    override fun convert(holder: BaseViewHolder, item: HomeListBean) {

    }
}