package com.uiuang.cloudknowledge.ui.adapter.gank

import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.uiuang.cloudknowledge.R
import com.uiuang.cloudknowledge.bean.GankIOResultBean
import com.uiuang.cloudknowledge.ext.displayEspImage
import com.uiuang.cloudknowledge.ext.displayGif
import com.uiuang.cloudknowledge.ext.loadUrl

class GankAndroidAdapter() :BaseQuickAdapter<GankIOResultBean,BaseViewHolder>(
    R.layout.item_gank_android
) {
    var isAll: Boolean = false

    fun setAllType(isAllType: Boolean) {
        this.isAll = isAllType
    }

    override fun convert(holder: BaseViewHolder, item: GankIOResultBean) {
        if (isAll && "福利" == item.type) {
            holder.setGone(R.id.iv_all_welfare,false)
            holder.setGone(R.id.ll_welfare_other,true)
            holder.getView<ImageView>(R.id.iv_all_welfare).displayEspImage(context, item.url, 1)
        }else{
            holder.setGone(R.id.iv_all_welfare,true)
            holder.setGone(R.id.ll_welfare_other,false)
        }

        if (isAll) {
            holder.setGone(R.id.tv_content_type, true)
            holder.setText(R.id.tv_content_type, item.type.toString())
        }else{
            holder.setGone(R.id.tv_content_type, false)
        }
        if (!item.images.isNullOrEmpty()&&!item.images!![0].isBlank()) {
            holder.setGone(R.id.iv_android_pic,false)
            holder.getView<ImageView>(R.id.iv_android_pic).displayGif(context, item.images!!.get(0))
        }else holder.setGone(R.id.iv_android_pic,true)

        holder.setText(R.id.tv_android_des, item.desc)
        holder.setText(R.id.tv_android_who,if (item.author!!.isBlank())"佚名" else item.author)
        holder.setText(R.id.tv_android_time,if (item.createdAt.isNullOrEmpty())"" else item.createdAt)
    }
}