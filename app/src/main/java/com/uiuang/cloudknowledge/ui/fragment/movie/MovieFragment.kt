package com.uiuang.cloudknowledge.ui.fragment.movie

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.uiuang.cloudknowledge.R
import com.uiuang.cloudknowledge.app.base.BaseFragment
import com.uiuang.cloudknowledge.databinding.FragmentMovieBinding
import com.uiuang.cloudknowledge.ext.bindViewPager2
import com.uiuang.cloudknowledge.ext.init
import com.uiuang.cloudknowledge.ext.setUiTheme
import com.uiuang.cloudknowledge.viewmodel.state.HomeViewModel
import kotlinx.android.synthetic.main.fragment_movie.*


class MovieFragment : BaseFragment<HomeViewModel, FragmentMovieBinding>() {
    //fragment集合
    private var fragments: ArrayList<Fragment> = arrayListOf()

    //标题集合
    private var dataList: ArrayList<String> = arrayListOf()
    override fun layoutId(): Int = R.layout.fragment_movie

    override fun initView(savedInstanceState: Bundle?) {
        //初始化 toolbar
        toolbar.run {
            init("电影")
        }

        fragments.add(FilmShowingFragment.newInstance())
        fragments.add(FilmComingFragment.newInstance())
        val stringArray = mActivity.resources.getStringArray(R.array.movie_title)
        dataList.addAll(stringArray)

        //初始化viewpager2
        view_pager.init(this, fragments)
        //初始化 magic_indicator
        magicIndicator.bindViewPager2(view_pager, mStringList = dataList)
    }

    override fun createObserver() {
        appViewModel.appColor.observe(viewLifecycleOwner, Observer {
            //监听全局的主题颜色改变
            setUiTheme(it, toolbar)
        })
    }


}