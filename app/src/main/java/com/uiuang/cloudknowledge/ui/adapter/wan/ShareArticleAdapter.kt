package com.uiuang.cloudknowledge.ui.adapter.wan

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.uiuang.cloudknowledge.R
import com.uiuang.cloudknowledge.bean.wan.ArticlesBean

class ShareArticleAdapter(data: MutableList<ArticlesBean>) :
    BaseQuickAdapter<ArticlesBean, BaseViewHolder>(R.layout.item_share_article, data) {
    override fun convert(holder: BaseViewHolder, item: ArticlesBean) {
        item.run {
            holder.setText(R.id.item_share_title, title)
            holder.setText(R.id.item_share_date, niceDate)
        }
    }
}