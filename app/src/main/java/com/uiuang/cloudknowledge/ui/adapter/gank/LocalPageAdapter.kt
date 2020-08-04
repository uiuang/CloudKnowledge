package com.uiuang.cloudknowledge.ui.adapter.gank

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.viewpager.widget.PagerAdapter
import com.github.chrisbanes.photoview.OnPhotoTapListener
import com.github.chrisbanes.photoview.PhotoView
import com.uiuang.cloudknowledge.R
import com.uiuang.cloudknowledge.ui.fragment.sister.BigImageFragment


/**
 * @Title:
 * @Description:
 * @author zsc
 * @date 2020/8/4 21:25
 */
class LocalPageAdapter constructor(var content: Context, private var imageId: Int) : PagerAdapter(),
    OnPhotoTapListener {
    private var listener: BigImageFragment.onBackListener? = null
    override fun getCount(): Int = 1


    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view === obj
    }

    fun setOnBackListener(listener: BigImageFragment.onBackListener) {
        this.listener = listener
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view: View =
            LayoutInflater.from(content).inflate(R.layout.viewpager_very_image, container, false)
        val zoomImageView: PhotoView = view.findViewById(R.id.zoom_image_view)
        val spinner = view.findViewById<ProgressBar>(R.id.loading)
        spinner.visibility = View.GONE
        if (imageId != 0) {
            zoomImageView.setImageResource(imageId)
        }
        zoomImageView.setOnPhotoTapListener(this)
        container.addView(view, 0)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
        val view = obj as View
        container.removeView(view)
    }

    override fun onPhotoTap(view: ImageView?, x: Float, y: Float) {
        listener?.back()
    }


}