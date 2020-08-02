package com.uiuang.cloudknowledge.ui.activity

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import com.uiuang.cloudknowledge.R
import com.uiuang.cloudknowledge.app.base.BaseActivity
import com.uiuang.cloudknowledge.databinding.ActivityMainBinding
import com.uiuang.cloudknowledge.viewmodel.state.MainViewModel
import com.uiuang.mvvm.network.manager.NetState
import com.uiuang.cloudknowledge.utils.StatusBarUtil


class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>() {


    override fun layoutId(): Int = R.layout.activity_main
    override fun initView(savedInstanceState: Bundle?) {

        appViewModel.appColor.value?.let {
            supportActionBar?.setBackgroundDrawable(ColorDrawable(it))
            StatusBarUtil.setColor(this, it, 0)
        }
    }

    override fun createObserver() {
        appViewModel.appColor.observe(this, Observer {
            supportActionBar?.setBackgroundDrawable(ColorDrawable(it))
            StatusBarUtil.setColor(this, it, 0)
        })
    }

    /**
     * 示例，在Activity/Fragment中如果想监听网络变化，可重写onNetworkStateChanged该方法
     */
    override fun onNetworkStateChanged(netState: NetState) {
        super.onNetworkStateChanged(netState)
        if (netState.isSuccess) {
            Toast.makeText(applicationContext, "我特么终于有网了啊!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(applicationContext, "我特么怎么断网了!", Toast.LENGTH_SHORT).show()
        }
    }
}