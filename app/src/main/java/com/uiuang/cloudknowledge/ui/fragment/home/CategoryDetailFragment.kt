package com.uiuang.cloudknowledge.ui.fragment.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.uiuang.cloudknowledge.R
import com.uiuang.cloudknowledge.app.base.BaseFragment
import com.uiuang.cloudknowledge.bean.TabBean
import com.uiuang.cloudknowledge.databinding.FragmentCategoryDetailBinding
import com.uiuang.cloudknowledge.ext.initCategory
import com.uiuang.cloudknowledge.ext.initClose
import com.uiuang.cloudknowledge.viewmodel.state.HomeViewModel
import com.uiuang.mvvm.ext.nav
import jp.wasabeef.glide.transformations.BlurTransformation
import kotlinx.android.synthetic.main.fragment_category_detail.*

/**
 * A simple [Fragment] subclass.
 */
class CategoryDetailFragment : BaseFragment<HomeViewModel, FragmentCategoryDetailBinding>() {
    private var childrenId: Int = 0
    private var tabBean: TabBean? = null
    private var fragments: ArrayList<Fragment> = arrayListOf()
    private var titles: ArrayList<String> = arrayListOf()


    override fun layoutId(): Int = R.layout.fragment_category_detail

    override fun initView(savedInstanceState: Bundle?) {
        arguments?.run {
            childrenId = getInt("id")
            tabBean = getParcelable<TabBean>("tabBean")
        }
        toolbar.run {
            tabBean?.name?.let {
                initClose(it) {
                    nav().navigateUp()
                }
            }
        }
        tabBean?.defaultUrl?.loadImage()
//        val children: MutableList<ChildrenBean>? = tabBean?.children
        val children = tabBean?.children
        var initIndex: Int = 0
        children?.forEachIndexed { index, childrenBean ->
            if (children[index].id == childrenId) initIndex = index
            fragments.add(
                CategoryArticleFragment.newInstance(
                    childrenBean.id,
                    childrenBean.name,
                    children[index].id == childrenId
                )
            )
            titles.add(childrenBean.name)
        }
        viewPager.initCategory(
            this, fragments, tabLayout,
            titles, true
        )
        viewPager.setCurrentItem(initIndex,false)
    }

    private fun String.loadImage() {
        // "23":模糊度；"4":图片缩放4倍后再进行模糊
        Glide.with(requireActivity())
            .load(this)
            .transition(DrawableTransitionOptions.withCrossFade())
            .error(R.drawable.stackblur_default)
            .placeholder(R.drawable.stackblur_default)
            .transition(DrawableTransitionOptions.withCrossFade(500))
            .transform(BlurTransformation(50, 8))
            .into(blur)
    }

}
