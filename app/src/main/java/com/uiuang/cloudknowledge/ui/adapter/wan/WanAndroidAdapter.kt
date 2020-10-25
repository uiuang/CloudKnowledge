package com.uiuang.cloudknowledge.ui.adapter.wan

import android.text.Html
import android.text.TextUtils
import android.widget.CheckBox
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.uiuang.cloudknowledge.R
import com.uiuang.cloudknowledge.bean.ArticlesBean
import com.uiuang.cloudknowledge.utils.DataUtil
import com.uiuang.cloudknowledge.weight.customview.CollectView

class WanAndroidAdapter(data: MutableList<ArticlesBean>) :
    BaseQuickAdapter<ArticlesBean, BaseViewHolder>(R.layout.item_wan_android, data) {

    private var collectAction: (item: ArticlesBean, v: CollectView, position: Int) -> Unit =
        { _: ArticlesBean, _: CollectView, _: Int -> }

    /**
     * 是我的收藏页进来的，全部是收藏状态。bean里面没有返回isCollect信息
     */
    var isCollectList = false

    /**
     * 不显示类别信息
     */
    var isNoShowChapterName = false

    /**
     * 不显示作者名字
     */
    var isNoShowAuthorName = false

    /**
     * 列表中是否显示图片
     */
    private var isNoImage = false

    override fun convert(holder: BaseViewHolder, item: ArticlesBean) {
        val ivImage = holder.getView<ImageView>(R.id.iv_image)
        if (!TextUtils.isEmpty(item.envelopePic) && !isNoImage) {
            holder.setGone(R.id.iv_image, false)
            Glide.with(ivImage.context)
                .load(item.envelopePic)
                .transition(DrawableTransitionOptions.withCrossFade(500))
                .apply(
                    RequestOptions()
                        .placeholder(getDefaultPic(1))
                        .error(getDefaultPic(1))
                )
                .into(ivImage)

        } else {
            holder.setGone(R.id.iv_image, true)
        }
        holder.setGone(R.id.iv_new, !item.fresh)
        holder.setText(
            R.id.tv_shareUser,
            DataUtil.getHomeAuthor(item.fresh, item.author, item.shareUser)
        )
        holder.setGone(R.id.tv_shareUser, isNoShowAuthorName)

        if (TextUtils.isEmpty(item.chapterName))
            holder.setText(R.id.tv_tag_name, "暂无标签")
        else
            holder.setText(R.id.tv_tag_name, Html.fromHtml(item.chapterName))
        holder.setGone(R.id.tv_tag_name, isNoShowChapterName)

        holder.setText(R.id.tv_time, item.niceDate)
        holder.setText(R.id.tv_title, Html.fromHtml(item.title))
        holder.getView<CollectView>(R.id.item_home_collect).isChecked = item.collect
        holder.getView<CollectView>(R.id.item_home_collect)
            .setOnCollectViewClickListener(object : CollectView.OnCollectViewClickListener {
                override fun onClick(v: CollectView) {
                    collectAction.invoke(item, v, holder.adapterPosition)
                }
            })
    }

    fun setCollectList() {
        isCollectList = true
    }

    fun setNoShowChapterName() {
        isNoShowChapterName = true
    }

    fun setNoShowAuthorName() {
        isNoShowAuthorName = true
    }

    fun setCollectClick(inputCollectAction: (item: ArticlesBean, v: CollectView, position: Int) -> Unit) {
        this.collectAction = inputCollectAction
    }


    fun setNoImage(isNoImage: Boolean) {
        this.isNoImage = isNoImage
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