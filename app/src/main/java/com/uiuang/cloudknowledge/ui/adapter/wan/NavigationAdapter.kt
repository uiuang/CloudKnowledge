package com.uiuang.cloudknowledge.ui.adapter.wan

import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.uiuang.cloudknowledge.R
import com.uiuang.cloudknowledge.bean.wan.NavJsonBean
import com.uiuang.mvvm.util.toHtml


/**
 * @Title:
 * @Description:
 * @author zsc
 * @date 2020/9/6 11:26
 */
class NavigationAdapter : BaseQuickAdapter<NavJsonBean, BaseViewHolder>(R.layout.item_navigation) {
    override fun convert(holder: BaseViewHolder, item: NavJsonBean) {
        val tvTitle: TextView = holder.getView<TextView>(R.id.tv_title)
        tvTitle.isSelected = item.selected
        tvTitle.text = item.name?.toHtml()

    }
}