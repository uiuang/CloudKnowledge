package com.uiuang.cloudknowledge.ui.adapter.wan

import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.uiuang.cloudknowledge.bean.wan.WanAndroidBannerBean
import com.youth.banner.adapter.BannerAdapter


class WanBannerAdapter(datas: MutableList<WanAndroidBannerBean>) :
    BannerAdapter<WanAndroidBannerBean, WanBannerAdapter.BannerViewHolder>(datas) {

    override fun onCreateHolder(parent: ViewGroup?, viewType: Int): BannerViewHolder {
        var imageView = ImageView(parent!!.context)
        //注意，必须设置为match_parent，这个是viewpager2强制要求的
        //注意，必须设置为match_parent，这个是viewpager2强制要求的
        imageView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        return BannerViewHolder(imageView)
    }


    class BannerViewHolder(var imageView: ImageView) : RecyclerView.ViewHolder(imageView)

    override fun onBindView(
        holder: BannerViewHolder?,
        data: WanAndroidBannerBean?,
        position: Int,
        size: Int
    ) {
        Glide.with(holder!!.imageView.context)
            .load(mDatas[position]!!.imagePath)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(holder!!.imageView)
    }
}