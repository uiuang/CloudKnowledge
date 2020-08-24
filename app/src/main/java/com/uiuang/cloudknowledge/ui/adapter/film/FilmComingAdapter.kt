package com.uiuang.cloudknowledge.ui.adapter.film

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.uiuang.cloudknowledge.R
import com.uiuang.cloudknowledge.bean.ComingFilmBean
import com.uiuang.mvvm.util.dp2px
import com.uiuang.mvvm.util.screenWidth


/**
 * @Title:
 * @Description:
 * @author zsc
 * @date 2020/8/5 22:42
 */
class FilmComingAdapter(context: Context,data: MutableList<ComingFilmBean.MoviesBean>?) :
    BaseQuickAdapter<ComingFilmBean.MoviesBean, BaseViewHolder>(
        R.layout.item_film_coming, data
    ) {
    private var width: Int = 0

    init {
        val px =context.dp2px(36)
        width = (context.screenWidth - px) / 3
    }

    override fun convert(holder: BaseViewHolder, item: ComingFilmBean.MoviesBean) {
        val ivTopPhoto = holder.getView<ImageView>(R.id.iv_top_photo)
        holder.setText(R.id.tv_name, item.title)
        holder.setText(R.id.tv_rate, item.releaseDate)
        setWidthHeight(ivTopPhoto, width, 0.758f)
        Glide.with(ivTopPhoto.context)
            .load(item.image)
            .transition(DrawableTransitionOptions.withCrossFade(500))
            .apply(
                RequestOptions()
                    .placeholder(getDefaultPic(1))
                    .error(getDefaultPic(1))
            )
            .into(ivTopPhoto)
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

    private fun setWidthHeight(view: ImageView, width: Int, bili: Float) {

        val height = (width / bili).toInt()
        val layoutParams = view.layoutParams
        layoutParams.width = width
        layoutParams.height = height
    }
}