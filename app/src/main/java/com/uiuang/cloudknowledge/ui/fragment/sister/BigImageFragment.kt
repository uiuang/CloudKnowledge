package com.uiuang.cloudknowledge.ui.fragment.sister

import android.os.Bundle
import android.view.View
import androidx.viewpager.widget.ViewPager
import com.google.gson.Gson
import com.uiuang.cloudknowledge.R
import com.uiuang.cloudknowledge.app.base.BaseFragment
import com.uiuang.cloudknowledge.bean.gank.GankIOResultBean
import com.uiuang.cloudknowledge.databinding.FragmentBigImageBinding
import com.uiuang.cloudknowledge.ui.adapter.gank.LocalPageAdapter
import com.uiuang.cloudknowledge.ui.adapter.gank.ViewPagerAdapter
import com.uiuang.cloudknowledge.utils.ACache
import com.uiuang.cloudknowledge.utils.toast
import com.uiuang.cloudknowledge.viewmodel.state.HomeViewModel
import com.uiuang.mvvm.ext.nav
import com.uiuang.mvvm.util.NetworkUtil
import kotlinx.android.synthetic.main.fragment_big_image.*


/**
 * 查看大图
 */
class BigImageFragment : BaseFragment<HomeViewModel, FragmentBigImageBinding>() {

    private var page: Int = 0
    private var listBeans: MutableList<GankIOResultBean> = arrayListOf()

    companion object {
        @JvmStatic
        fun newInstance() = BigImageFragment()
    }

    override fun layoutId(): Int = R.layout.fragment_big_image

    override fun initView(savedInstanceState: Bundle?) {
        var select: Int = requireArguments().getInt("select")
        var position: Int = requireArguments().getInt("position")
        var imageId: Int = requireArguments().getInt("id")
        var isLocal = requireArguments().getBoolean("isLocal")
        var isApp = requireArguments().getBoolean("isApp")
        var json = ACache[mActivity].getAsString("ImageItemsBean")
        if (isApp) {
            var localPageAdapter: LocalPageAdapter = LocalPageAdapter(mActivity, imageId)
            very_image_viewpager.adapter = localPageAdapter
            very_image_viewpager.isEnabled = false
            localPageAdapter.setOnBackListener(object : OnBackListener {
                override fun back() {
                    nav().navigateUp()
                }
            })
        } else {
            listBeans = Gson().fromJson<Array<GankIOResultBean>>(
                json,
                Array<GankIOResultBean>::class.java
            ).toMutableList()

            if (isLocal) {
                tv_save_big_image.visibility = View.GONE
            }
            val adapter: ViewPagerAdapter = ViewPagerAdapter(mActivity, listBeans, isLocal)
            very_image_viewpager.adapter = adapter
            very_image_viewpager.currentItem = position
            very_image_viewpager.addOnPageChangeListener(listener)
            page = position
            very_image_viewpager.isEnabled = false
            adapter.setOnBackListener(object : OnBackListener {
                override fun back() {
                    nav().navigateUp()
                }
            })
            if (select == 2) {
                very_image_viewpager_text.text = "${position.plus(1)}/${listBeans.size}"

            }
        }

        tv_save_big_image.setOnClickListener {
            if (!NetworkUtil.isNetworkAvailable(requireActivity())) {
                "当前网络不可用，请检查你的网络设置".toast()
                return@setOnClickListener
            }
            if (isApp) {
                // 本地图片
                "图片已存在".toast()
            } else {
                // 网络图片
//                RxSaveImage.saveImageToGallery(this, imageList.get(page), imageTitles.get(page))
            }
        }


    }

    private var listener: ViewPager.OnPageChangeListener = object : ViewPager.OnPageChangeListener {
        override fun onPageScrollStateChanged(state: Int) {

        }

        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
        }

        override fun onPageSelected(position: Int) {
            very_image_viewpager_text.text = "${position.plus(1)}/${listBeans.size}"
            page = position
        }

    }


    interface OnBackListener {
        fun back()
    }
}