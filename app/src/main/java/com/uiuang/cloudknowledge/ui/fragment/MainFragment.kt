package com.uiuang.cloudknowledge.ui.fragment

import android.os.Bundle
import androidx.lifecycle.Observer
import com.uiuang.cloudknowledge.R
import com.uiuang.cloudknowledge.databinding.FragmentMainBinding
import com.uiuang.cloudknowledge.viewmodel.state.MainViewModel
import com.uiuang.cloudknowledge.app.base.BaseFragment
import com.uiuang.cloudknowledge.ext.init
import com.uiuang.cloudknowledge.ext.initMain
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : BaseFragment<MainViewModel, FragmentMainBinding>() {


    override fun layoutId(): Int = R.layout.fragment_main

    override fun initView(savedInstanceState: Bundle?) {
        //初始化viewpager2
        mainViewpager.initMain(this)
//        //初始化 bottombar
        mainBottom.init{
            when (it) {
                R.id.menu_main -> mainViewpager.setCurrentItem(0, false)
                R.id.menu_project -> mainViewpager.setCurrentItem(1, false)
                R.id.menu_system -> mainViewpager.setCurrentItem(2, false)
                R.id.menu_public -> mainViewpager.setCurrentItem(3, false)
                R.id.menu_mine -> mainViewpager.setCurrentItem(4, false)
            }
        }
    }

    override fun createObserver() {
        appViewModel.appColor.observe(viewLifecycleOwner, Observer {
//            setUiTheme(it, mainBottom)
        })

    }

}