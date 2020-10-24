package com.uiuang.cloudknowledge.ui.fragment.mine

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.uiuang.cloudknowledge.R
import com.uiuang.cloudknowledge.app.base.BaseFragment
import com.uiuang.cloudknowledge.databinding.FragmentCollectBinding
import com.uiuang.cloudknowledge.ext.bindViewPager2Collect
import com.uiuang.cloudknowledge.ext.init
import com.uiuang.cloudknowledge.ext.initClose
import com.uiuang.cloudknowledge.viewmodel.state.CollectViewModel
import com.uiuang.mvvm.ext.nav
import kotlinx.android.synthetic.main.fragment_collect.*
import kotlinx.android.synthetic.main.include_toolbar.*


class CollectFragment : BaseFragment<CollectViewModel, FragmentCollectBinding>() {


    var titleData = arrayListOf("文章", "网址")

    private var fragments: ArrayList<Fragment> = arrayListOf()

    init {
        fragments.add(CollectArticleFragment())
        fragments.add(CollectUrlFragment())
    }

    override fun layoutId(): Int = R.layout.fragment_collect

    override fun initView(savedInstanceState: Bundle?) {
        //初始化时设置顶部主题颜色
        appViewModel.appColor.value?.let { collect_viewpager_linear.setBackgroundColor(it) }
        //初始化viewpager2
        collect_view_pager.init(this, fragments)
        //初始化 magic_indicator
        collect_magic_indicator.bindViewPager2Collect(collect_view_pager, mStringList = titleData)
        toolbar.initClose() {
            nav().navigateUp()
        }
    }

}