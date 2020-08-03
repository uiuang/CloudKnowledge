package com.uiuang.cloudknowledge.ui.fragment.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.uiuang.cloudknowledge.R
import com.uiuang.cloudknowledge.app.base.BaseFragment
import com.uiuang.cloudknowledge.databinding.FragmentHomeBinding
import com.uiuang.cloudknowledge.ext.bindViewPager2
import com.uiuang.cloudknowledge.ext.init
import com.uiuang.cloudknowledge.viewmodel.state.HomeViewModel
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : BaseFragment<HomeViewModel, FragmentHomeBinding>() {
    //fragment集合
    var fragments: ArrayList<Fragment> = arrayListOf()

    //标题集合
    var mDataList: ArrayList<String> = arrayListOf()

    override fun layoutId(): Int = R.layout.fragment_home

    override fun initView(savedInstanceState: Bundle?) {
        //初始化 toolbar
        toolbar.run {
            init("首页")
            inflateMenu(R.menu.home_menu)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.home_search -> {
//                        nav().navigateAction(R.id.action_mainfragment_to_searchFragment)
                    }
                }
                true
            }
        }
        fragments.add(WanHomeFragment.newInstance())
        fragments.add(WanFindFragment.newInstance())
        fragments.add(TreeFragment.newInstance())
        fragments.add(NavigationFragment.newInstance())
        var stringArray = mActivity.resources.getStringArray(R.array.wan_title)
        mDataList.addAll(stringArray)

        //初始化viewpager2
        view_pager.init(this, fragments)
        //初始化 magic_indicator
        magicIndicator.bindViewPager2(view_pager, mStringList = mDataList)
    }


}