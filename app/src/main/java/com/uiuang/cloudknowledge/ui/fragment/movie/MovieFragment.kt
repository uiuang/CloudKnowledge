package com.uiuang.cloudknowledge.ui.fragment.movie

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.uiuang.cloudknowledge.R
import com.uiuang.cloudknowledge.app.base.BaseFragment
import com.uiuang.cloudknowledge.databinding.FragmentMovieBinding
import com.uiuang.cloudknowledge.ext.bindViewPager2
import com.uiuang.cloudknowledge.ext.init
import com.uiuang.cloudknowledge.viewmodel.state.HomeViewModel
import kotlinx.android.synthetic.main.fragment_home.*


class MovieFragment : BaseFragment<HomeViewModel, FragmentMovieBinding>() {
    //fragment集合
    var fragments: ArrayList<Fragment> = arrayListOf()

    //标题集合
    var dataList: ArrayList<String> = arrayListOf()
    override fun layoutId(): Int = R.layout.fragment_movie

    override fun initView(savedInstanceState: Bundle?) {
        //初始化 toolbar
        toolbar.run {
            init("电影")
        }

        fragments.add(FilmShowingFragment.newInstance())
        fragments.add(FilmComingFragment.newInstance())
        var stringArray = mActivity.resources.getStringArray(R.array.movie_title)
        dataList.addAll(stringArray)

        //初始化viewpager2
        view_pager.init(this, fragments)
        //初始化 magic_indicator
        magicIndicator.bindViewPager2(view_pager, mStringList = dataList)
    }


}