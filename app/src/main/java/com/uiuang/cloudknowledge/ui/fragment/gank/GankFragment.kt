package com.uiuang.cloudknowledge.ui.fragment.gank

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.uiuang.cloudknowledge.R
import com.uiuang.cloudknowledge.app.base.BaseFragment
import com.uiuang.cloudknowledge.databinding.FragmentGankBinding
import com.uiuang.cloudknowledge.ext.bindViewPager2
import com.uiuang.cloudknowledge.ext.init
import com.uiuang.cloudknowledge.viewmodel.state.GankViewModel
import kotlinx.android.synthetic.main.fragment_gank.*


class GankFragment : BaseFragment<GankViewModel, FragmentGankBinding>() {

    //fragment集合
    var fragments: ArrayList<Fragment> = arrayListOf()

    //标题集合
    var mDataList: ArrayList<String> = arrayListOf()


    override fun layoutId(): Int = R.layout.fragment_gank

    override fun initView(savedInstanceState: Bundle?) {
        //初始化 toolbar
        toolbar.run {
            init("干货")
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

        fragments.add(GankHomeFragment.newInstance())
        fragments.add(WelfareFragment.newInstance())
        fragments.add(CustomFragment.newInstance())
        fragments.add(AndroidFragment.newInstance("Android"))
        var stringArray = mActivity.resources.getStringArray(R.array.gank_title)
        mDataList.addAll(stringArray)

        //初始化viewpager2
        view_pager.init(this, fragments)
        //初始化 magic_indicator
        magicIndicator.bindViewPager2(view_pager, mStringList = mDataList)
    }


}