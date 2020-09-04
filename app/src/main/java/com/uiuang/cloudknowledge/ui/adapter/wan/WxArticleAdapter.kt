package com.uiuang.cloudknowledge.ui.adapter.wan

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.uiuang.cloudknowledge.R
import com.uiuang.cloudknowledge.bean.TabBean
import com.uiuang.cloudknowledge.bean.WanAndroidBannerBean

class WxArticleAdapter(datas: MutableList<TabBean>) : BaseQuickAdapter<TabBean, BaseViewHolder>(R.layout.item_wxarticle) {
    private var id: Int = 0

    override fun convert(holder: BaseViewHolder, item: TabBean) {
        holder.setText(R.id.tv_title, item.name)
        if (item.id == id) {
            holder.setTextColor(R.id.tv_title, R.color.colorPrimary)
            holder.setBackgroundColor(R.id.view_line,R.color.colorPrimary)
        }else{
            holder.setTextColor(R.id.tv_title, R.color.colorBlack333)
            holder.setBackgroundColor(R.id.view_line,R.color.textHint)
        }
    }
    fun setId(id: Int) {
        this.id = id
    }
}