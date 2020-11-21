package com.uiuang.cloudknowledge.ui.adapter.wan

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.uiuang.cloudknowledge.R
import com.uiuang.cloudknowledge.bean.wan.ArticlesBean
import com.uiuang.cloudknowledge.ext.setAdapterAnimation
import com.uiuang.cloudknowledge.utils.SettingUtil
import com.uiuang.cloudknowledge.weight.customview.CollectView
import com.uiuang.mvvm.util.toHtml


class CollectUrlAdapter(data: ArrayList<ArticlesBean>) :
    BaseQuickAdapter<ArticlesBean, BaseViewHolder>(
        R.layout.item_collect_url, data
    ) {

    private var collectAction: (item: ArticlesBean, v: CollectView, position: Int) -> Unit =
        { _: ArticlesBean, _: CollectView, _: Int -> }

    init {
        setAdapterAnimation(SettingUtil.getListMode())
    }

    override fun convert(holder: BaseViewHolder, item: ArticlesBean) {
        //赋值
        item.run {
            holder.setText(R.id.item_collect_url_name, name.toHtml())
            holder.setText(R.id.item_collect_url_link, link)
            holder.getView<CollectView>(R.id.item_collect_url_collect).isChecked = true
        }
        holder.getView<CollectView>(R.id.item_collect_url_collect)
            .setOnCollectViewClickListener(object : CollectView.OnCollectViewClickListener {
                override fun onClick(v: CollectView) {
                    collectAction.invoke(item, v, holder.adapterPosition)
                }
            })
    }

    fun setCollectClick(inputCollectAction: (item: ArticlesBean, v: CollectView, position: Int) -> Unit) {
        this.collectAction = inputCollectAction
    }
}