package com.uiuang.cloudknowledge.ui.adapter.wan

import android.view.View
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.uiuang.cloudknowledge.R
import com.uiuang.cloudknowledge.bean.ChildrenBean
import com.uiuang.cloudknowledge.bean.TabBean
import com.uiuang.cloudknowledge.utils.DataUtil
import com.uiuang.cloudknowledge.utils.SettingUtil
import com.uiuang.cloudknowledge.utils.toast


/**
 * @Title:
 * @Description:
 * @author zsc
 * @date 2020/9/5 20:25
 */
class TreeAdapter : BaseQuickAdapter<TabBean, BaseViewHolder>(R.layout.item_tree) {
    private var isSelect: Boolean = false
    private var selectedPosition: Int = 0


    override fun convert(holder: BaseViewHolder, item: TabBean) {
        val rvTree: RecyclerView = holder.getView<RecyclerView>(R.id.rlv_tree)
        val flexBoxTreeAdapter: FlexBoxTreeAdapter = FlexBoxTreeAdapter()
        val manager = FlexboxLayoutManager(context, FlexDirection.ROW, FlexWrap.WRAP)
        rvTree.layoutManager = manager
        rvTree.itemAnimator = DefaultItemAnimator()
        rvTree.adapter = flexBoxTreeAdapter
        var name: String? = DataUtil.getHtmlString(item.name)
        if (isSelect) {
            rvTree.visibility = View.GONE
            if (selectedPosition == holder.adapterPosition) {
                name = "$name     ★★★"
                holder.setTextColorRes(R.id.tv_tree_title, R.color.colorTheme)
            } else {
                holder.setTextColorRes(R.id.tv_tree_title, R.color.colorBlack333)
            }
        } else {
            holder.setTextColorRes(R.id.tv_tree_title, R.color.colorBlack333)
            rvTree.visibility = View.VISIBLE
            flexBoxTreeAdapter.setNewInstance(item.children)
        }
        flexBoxTreeAdapter.setOnItemClickListener { _, _, position ->
            val data: MutableList<ChildrenBean> = flexBoxTreeAdapter.data
            val childrenBean: ChildrenBean = data[position]
            val id: Int = childrenBean.id
            childrenBean.name?.toast()
        }
        holder.setText(R.id.tv_tree_title, name)
    }


    fun setSelect(isSelect: Boolean) {
        this.isSelect = isSelect
        if (isSelect) {
            selectedPosition = SettingUtil.getFindPosition().plus(1)
        }
    }

    fun isSelect(): Boolean = isSelect

    fun getSelectedPosition() = selectedPosition.minus(1)
}