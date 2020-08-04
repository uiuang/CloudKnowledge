package com.uiuang.cloudknowledge.ui.adapter.gank

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.github.chrisbanes.photoview.OnPhotoTapListener
import com.github.chrisbanes.photoview.PhotoView
import com.uiuang.cloudknowledge.R
import com.uiuang.cloudknowledge.bean.GankIOResultBean
import com.uiuang.cloudknowledge.ui.fragment.sister.BigImageFragment
import com.uiuang.mvvm.util.windowManager


/**
 * @Title:
 * @Description:
 * @author zsc
 * @date 2020/8/4 21:43
 */
class ViewPagerAdapter constructor(
    var context: Context,
    var imageList: MutableList<GankIOResultBean>,
    var isLocal: Boolean
) : PagerAdapter(), OnPhotoTapListener {
    private var listener: BigImageFragment.onBackListener? = null


    private var inflater: LayoutInflater? = null

    init {
        inflater = LayoutInflater.from(context)
    }

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view === obj
    }

    fun setOnBackListener(listener: BigImageFragment.onBackListener) {
        this.listener = listener
    }


    override fun getCount(): Int {
        return if (imageList.size == 0) {
            0
        } else imageList.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view: View = inflater!!.inflate(R.layout.viewpager_very_image, container, false)
        val zoomImageView: PhotoView = view.findViewById(R.id.zoom_image_view)
        val progressBar = view.findViewById<ProgressBar>(R.id.loading)
        // 保存网络图片的路径
        // 保存网络图片的路径
        val adapterImageEntity: String? = getItem(position) as String?
        var imageUrl: String? = if (isLocal) {
            "file://$adapterImageEntity"
        } else {
            adapterImageEntity
        }

        progressBar.visibility = View.VISIBLE
        progressBar.isClickable = false

        Glide.with(context).load(imageUrl)
            .transition(DrawableTransitionOptions.withCrossFade(700))
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any,
                    target: Target<Drawable>,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable,
                    model: Any,
                    target: Target<Drawable>,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    progressBar.visibility = View.GONE
                    /**这里应该是加载成功后图片的高,最大为屏幕的高 */
                    val height = resource.intrinsicHeight
                    val wHeight: Int = context.windowManager!!.defaultDisplay.height
                    if (height < wHeight) {
                        zoomImageView.scaleType = ImageView.ScaleType.FIT_CENTER
                    } else {
                        zoomImageView.scaleType = ImageView.ScaleType.CENTER_CROP
                    }
                    return false
                }
            }).into(zoomImageView)

        zoomImageView.setOnPhotoTapListener(this)
        container.addView(view, 0)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
        val view = obj as View
        container.removeView(view)
    }

    private fun getItem(position: Int): Any? {
        return imageList[position].url
    }

    override fun onPhotoTap(view: ImageView?, x: Float, y: Float) {
        listener?.back()
    }

}