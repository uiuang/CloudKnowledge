package com.uiuang.cloudknowledge.ui.adapter.gank

import androidx.lifecycle.MutableLiveData
import com.afollestad.materialdialogs.bottomsheets.BasicGridItem
import com.afollestad.materialdialogs.list.getItemSelector
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.uiuang.cloudknowledge.R


/**
 * @Title:
 * @Description:
 * @author zsc
 * @date 2020/9/15 22:13
 */
class ListIconDialogAdapter(data: MutableList<BasicGridItem>) :BaseQuickAdapter<BasicGridItem,BaseViewHolder>(
    R.layout.item_icon_layout,data
) {
    override fun convert(holder: BaseViewHolder, item: BasicGridItem) {

//        holder.itemView.background = dialog.getItemSelector()
        item.configureTitle(holder.getView(R.id.md_grid_title))
        item.populateIcon(holder.getView(R.id.md_grid_icon))
    }
}