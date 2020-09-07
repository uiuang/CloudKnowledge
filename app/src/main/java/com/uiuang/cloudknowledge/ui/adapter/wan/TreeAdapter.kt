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
    private var navigationAction: (item: TabBean, id: Int, view: View) -> Unit =
        { _: TabBean, _: Int, _: View -> }

    private var isSelect: Boolean = false
    private var selectedPosition: Int = 0


    override fun convert(holder: BaseViewHolder, item: TabBean) {
        val flexBoxTreeAdapter: FlexBoxTreeAdapter by lazy {
            FlexBoxTreeAdapter()
        }
        var name: String? = DataUtil.getHtmlString(item.name)
        holder.getView<RecyclerView>(R.id.rlv_tree).run {
            val manager: FlexboxLayoutManager by lazy {
                FlexboxLayoutManager(context, FlexDirection.ROW, FlexWrap.WRAP)
            }
            layoutManager = manager
            adapter = flexBoxTreeAdapter
            if (isSelect) {
                visibility = View.GONE
                if (selectedPosition == holder.adapterPosition) {
                    name = "$name     ★★★"
                    holder.setTextColorRes(R.id.tv_tree_title, R.color.colorTheme)
                } else {
                    holder.setTextColorRes(R.id.tv_tree_title, R.color.colorBlack333)
                }
            } else {
                holder.setTextColorRes(R.id.tv_tree_title, R.color.colorBlack333)
                visibility = View.VISIBLE
                flexBoxTreeAdapter.setNewInstance(item.children)
            }
        }

        flexBoxTreeAdapter.setOnItemClickListener { _, view, position ->
            val data: MutableList<ChildrenBean> = flexBoxTreeAdapter.data
            val childrenBean: ChildrenBean = data[position]
            navigationAction.invoke(item,childrenBean.id, view)
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

    fun setNavigationAction(inputNavigationAction: (item: TabBean, id: Int, view: View) -> Unit) {
        this.navigationAction = inputNavigationAction
    }
}