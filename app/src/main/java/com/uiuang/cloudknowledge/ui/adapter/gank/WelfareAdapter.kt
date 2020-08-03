package com.uiuang.cloudknowledge.ui.adapter.gank

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.uiuang.cloudknowledge.R
import com.uiuang.cloudknowledge.bean.GankIOResultBean
import com.uiuang.mvvm.util.screenWidth


/**
 * @Title:
 * @Description:
 * @author zsc
 * @date 2020/8/3 19:18
 */
class WelfareAdapter(data: MutableList<GankIOResultBean>?) :
    BaseQuickAdapter<GankIOResultBean, BaseViewHolder>(
        R.layout.item_welfare, data) {
    override fun convert(holder: BaseViewHolder, item: GankIOResultBean) {
        var width: Int = (context.screenWidth - 36) / 2
        var ivWelfare = holder.getView<ImageView>(R.id.iv_welfare)
        setWidthHeight(ivWelfare, width, 852 / 1280f)
//        Glide.with(context).load(item.url)
//            .transition(DrawableTransitionOptions.withCrossFade(500))
//            .into(ivWelfare)
        Glide.with(ivWelfare.context)
            .load(item.url)
            .transition(DrawableTransitionOptions.withCrossFade(500))
            .apply(
                RequestOptions()
                    .placeholder(getDefaultPic(1))
                    .error(getDefaultPic(1))
            )
            .into(ivWelfare)
    }

    private fun setWidthHeight(view: ImageView, width: Int, bili: Float) {

        val height = (width / bili).toInt()
        val layoutParams = view.layoutParams
        layoutParams.width = width
        layoutParams.height = height
    }

    private fun getDefaultPic(type: Int): Int {
        when (type) {
            0 -> return R.drawable.img_default_movie
            1 -> return R.drawable.img_default_meizi
            2 -> return R.drawable.img_default_book
            3 -> return R.drawable.shape_bg_loading
        }
        return R.drawable.img_default_meizi
    }
}