package com.uiuang.cloudknowledge.ext

import android.content.Context
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.bumptech.glide.request.RequestOptions
import com.uiuang.cloudknowledge.R
import com.uiuang.mvvm.util.dp2px
import jp.wasabeef.glide.transformations.BlurTransformation

/**
 * des 图片加载扩展方法
 * @author zs
 * @data 2020/6/26
 */


/**
 * 通过url加载
 */
fun ImageView.loadUrl(context: Context, url: String) {
    Glide.with(context)
        .load(url)
        .transition(withCrossFade())
        .into(this)
}

fun ImageView.displayEspImage(context: Context, url: String, type: Int) {
    Glide.with(context)
        .load(url)
        .transition(withCrossFade(500))
        .placeholder(getDefaultPic(type))
        .error(getDefaultPic(type))
        .into(this)
}

fun getDefaultPic(type: Int): Int {
    when (type) {
        0 -> return R.drawable.img_default_movie
        1 -> return R.drawable.img_default_meizi
        2 -> return R.drawable.img_default_book
        3 -> return R.drawable.shape_bg_loading
        else -> {
        }
    }
    return R.drawable.img_default_meizi
}

fun ImageView.displayGif(context: Context, url: String) {
    Glide.with(context) //                .asBitmap()
        .load(url)
        .override(context.dp2px(60), context.dp2px(80))
        .placeholder(R.drawable.shape_bg_loading)
        .error(R.drawable.shape_bg_loading)
        .into(this)
}


/**
 * 通过uri加载
 */
fun ImageView.loadUri(context: Context, uri: Uri) {
    Glide.with(context)
        .load(uri)
        .transition(withCrossFade())
        .into(this)
}

/**
 * 高斯模糊加渐入渐出
 */
fun ImageView.loadBlurTrans(context: Context, uri: Uri, radius: Int) {
    Glide.with(context)
        .load(uri)
        .thumbnail(0.1f).skipMemoryCache(false).diskCacheStrategy(DiskCacheStrategy.RESOURCE)
        .apply(RequestOptions.bitmapTransform(BlurTransformation(radius, 8)))
        .transition(withCrossFade(400))
        .into(this)
}


/**
 * 圆形图片
 */
fun ImageView.loadCircle(context: Context, uri: Uri) {
    Glide.with(context)
        .load(uri)
        .apply(RequestOptions.bitmapTransform(CircleCrop()))
        .into(this)
}

/**
 * 圆形图片
 */
fun ImageView.loadRadius(context: Context, url: String, radius: Int) {
    Glide.with(context)
        .load(url)
        .centerCrop()
//        .error(R.drawable.ic_launcher)
        .transition(withCrossFade())
//        .transform(GlideRoundTransform(context,radius))
        .into(this)
}

fun ImageView.imageUrl(
    url: String?,
    imageWidthDp: Int,
    imageHeightDp: Int
) {

    Glide.with(this.context)
        .load(url)
        .override(
            this.context.dp2px(imageWidthDp),
            this.context.dp2px(imageHeightDp))
                .transition(withCrossFade(500))
                .placeholder(getMusicDefaultPic(4))
                .centerCrop()
                .error(getDefaultPic(0))
                .into(this)
}

  fun getMusicDefaultPic(imgNumber: Int): Int {
    when (imgNumber) {
//        1 -> return R.drawable.img_two_bi_one
//        2 -> return R.drawable.img_four_bi_three
//        3 -> return R.drawable.img_one_bi_one
        4 -> return R.drawable.shape_bg_loading
        else -> {
        }
    }
    return R.drawable.shape_bg_loading
}


