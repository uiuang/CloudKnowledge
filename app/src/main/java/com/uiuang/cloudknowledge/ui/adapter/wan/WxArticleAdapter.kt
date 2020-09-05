package com.uiuang.cloudknowledge.ui.adapter.wan

import android.os.Build
import androidx.annotation.RequiresApi
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.uiuang.cloudknowledge.R
import com.uiuang.cloudknowledge.bean.ChildrenBean
import com.uiuang.cloudknowledge.bean.TabBean
import com.uiuang.cloudknowledge.bean.WanAndroidBannerBean

class WxArticleAdapter(datas: MutableList<ChildrenBean>) :
    BaseQuickAdapter<ChildrenBean, BaseViewHolder>(R.layout.item_wxarticle) {
    private var id: Int = 0

    @RequiresApi(Build.VERSION_CODES.M)
    override fun convert(holder: BaseViewHolder, item: ChildrenBean) {
        holder.setText(R.id.tv_title, item.name)
        if (item.id == this.id) {
            holder.setTextColorRes(R.id.tv_title, R.color.colorPrimary)
            holder.setBackgroundColor(
                R.id.view_line,
                context.resources.getColor(R.color.colorPrimary, null)
            )
        } else {
            holder.setTextColorRes(R.id.tv_title, R.color.colorBlack333)
            holder.setBackgroundColor(
                R.id.view_line,
                context.resources.getColor(R.color.textHint, null)
            )

        }
    }

    fun setId(id: Int) {
        this.id = id
    }
}