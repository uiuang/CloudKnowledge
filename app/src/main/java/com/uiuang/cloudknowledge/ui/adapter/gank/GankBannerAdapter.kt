package com.uiuang.cloudknowledge.ui.adapter.gank

import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.uiuang.cloudknowledge.bean.GankIOResultBean
import com.uiuang.cloudknowledge.ext.loadUrl
import com.youth.banner.adapter.BannerAdapter

class GankBannerAdapter(data: MutableList<GankIOResultBean>) :
    BannerAdapter<GankIOResultBean, GankBannerAdapter.BannerViewHolder>(
        data
    ) {

    class BannerViewHolder(var imageView: ImageView) : RecyclerView.ViewHolder(imageView)

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

    override fun onBindView(
        holder: BannerViewHolder?,
        data: GankIOResultBean?,
        position: Int,
        size: Int
    ) {
        holder!!.imageView.loadUrl(holder.imageView.context, data!!.image)

    }

}