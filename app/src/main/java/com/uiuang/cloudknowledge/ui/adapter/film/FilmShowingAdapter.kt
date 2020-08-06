package com.uiuang.cloudknowledge.ui.adapter.film

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.uiuang.cloudknowledge.R
import com.uiuang.cloudknowledge.bean.FilmItemBean


/**
 * @Title:
 * @Description:
 * @author zsc
 * @date 2020/8/5 22:07
 */
class FilmShowingAdapter(data: MutableList<FilmItemBean>?) :
    BaseQuickAdapter<FilmItemBean, BaseViewHolder>(
        R.layout.item_film_showing, data
    ) {
    override fun convert(holder: BaseViewHolder, item: FilmItemBean) {
        var iv_one_photo: ImageView = holder.getView<ImageView>(R.id.iv_one_photo)
        holder.setText(R.id.tv_one_title, item.tCn)
        holder.setText(R.id.tv_one_directors, item.dN)
        holder.setText(R.id.tv_one_casts, item.actors)
        holder.setText(
            R.id.tv_one_genres,
            "${context.resources.getString(R.string.string_type)}${item.movieType}")
        holder.setText(
            R.id.tv_one_rating_rate,
            context.resources.getString(R.string.string_rating) + item.r
        )


        Glide.with(iv_one_photo.context)
            .load(item.img)
            .transition(DrawableTransitionOptions.withCrossFade(500))
            .apply(
                RequestOptions()
                    .placeholder(getDefaultPic(0))
                    .error(getDefaultPic(0))
            )
            .into(iv_one_photo)
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