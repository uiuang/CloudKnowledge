package com.uiuang.cloudknowledge.ui.fragment.home

import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.uiuang.cloudknowledge.R
import com.uiuang.cloudknowledge.app.base.BaseFragment
import com.uiuang.cloudknowledge.bean.TabBean
import com.uiuang.cloudknowledge.databinding.FragmentCategoryDetailBinding
import com.uiuang.cloudknowledge.ext.initCategory
import com.uiuang.cloudknowledge.viewmodel.state.HomeViewModel
import com.uiuang.mvvm.ext.nav
import com.uiuang.mvvm.util.dp2px
import com.uiuang.mvvm.util.toHtml
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
//        initFullBar(toolbar,requireActivity())
        arguments?.run {
            childrenId = getInt("id")
            tabBean = getParcelable<TabBean>("tabBean")
        }
        toolbar.run {
            title = tabBean?.name?.toHtml()
            setNavigationIcon(R.drawable.ic_back)
            setNavigationOnClickListener {
                nav().navigateUp()
            }
            tv_category_num.text = "共${tabBean?.children?.size}个子分类"
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
        viewPager.setCurrentItem(initIndex, false)
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

    fun initFullBar(toolbar: Toolbar, activity: FragmentActivity) {
        val params = toolbar.layoutParams
        params.height = getStatusHeight(activity) + getSystemActionBarSize(activity)
        toolbar.layoutParams = params
        toolbar.setPadding(
            toolbar.left,
            toolbar.top + getStatusHeight(activity),
            toolbar.right,
            toolbar.bottom
        )
//        activity.setSupportActionBar(toolbar)
//        val actionBar = activity.supportActionBar
//        actionBar!!.setDisplayHomeAsUpEnabled(true)
//        actionBar.setHomeButtonEnabled(true)
    }

    private fun getSystemActionBarSize(context: Context): Int {
        val tv = TypedValue()
        return if (context.theme.resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            TypedValue.complexToDimensionPixelSize(
                tv.data,
                context.resources.displayMetrics
            )
        } else {
            context.dp2px(48)
        }
    }

    /**
     * 获取状态栏的高度
     *
     * @param context
     * @return
     */
    fun getStatusHeight(context: Context): Int {
        var statusHeight = -1
        try {
            val clazz = Class.forName("com.android.internal.R\$dimen")
            val obj = clazz.newInstance()
            val height = clazz.getField("status_bar_height")[obj].toString().toInt()
            statusHeight =
                context.applicationContext.resources.getDimensionPixelSize(height)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return statusHeight
    }

}
