package com.uiuang.cloudknowledge.ui.adapter.wan

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.uiuang.cloudknowledge.R
import com.uiuang.cloudknowledge.bean.ChildrenBean
import com.uiuang.cloudknowledge.utils.ColorUtil
import com.uiuang.cloudknowledge.utils.DataUtil


/**
 * @Title:
 * @Description:
 * @author zsc
 * @date 2020/9/5 20:57
 */
class FlexBoxTreeAdapter : BaseQuickAdapter<ChildrenBean, BaseViewHolder>(R.layout.item_tree_tag) {
    override fun convert(holder: BaseViewHolder, item: ChildrenBean) {
        holder.setText(R.id.tv_tree_tag, DataUtil.getHtmlString(item.name))
        holder.setTextColor(R.id.tv_tree_tag, ColorUtil.randomColor())
    }
}