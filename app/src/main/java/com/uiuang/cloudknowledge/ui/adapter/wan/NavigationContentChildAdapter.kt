package com.uiuang.cloudknowledge.ui.adapter.wan

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.uiuang.cloudknowledge.R
import com.uiuang.cloudknowledge.bean.ArticlesBean
import com.uiuang.cloudknowledge.utils.ColorUtil
import com.uiuang.mvvm.util.toHtml


/**
 * @Title:
 * @Description:
 * @author zsc
 * @date 2020/9/6 20:58
 */
class NavigationContentChildAdapter(data: MutableList<ArticlesBean>?) :
    BaseQuickAdapter<ArticlesBean, BaseViewHolder>(
        R.layout.item_navigation_content_grid, data
    ) {
    override fun convert(holder: BaseViewHolder, item: ArticlesBean) {
        holder.setText(R.id.tv_nav_tag, item.title?.toHtml())
        holder.setTextColor(R.id.tv_nav_tag, ColorUtil.randomColor())
    }
}