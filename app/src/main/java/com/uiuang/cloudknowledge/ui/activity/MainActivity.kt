package com.uiuang.cloudknowledge.ui.activity

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.uiuang.cloudknowledge.R
import com.uiuang.cloudknowledge.app.base.BaseActivity
import com.uiuang.cloudknowledge.databinding.ActivityMainBinding
import com.uiuang.cloudknowledge.utils.StatusBarUtil
import com.uiuang.cloudknowledge.utils.toast
import com.uiuang.cloudknowledge.viewmodel.state.MainViewModel
import com.uiuang.mvvm.network.manager.NetState


class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>() {
    var exitTime = 0L


    override fun layoutId(): Int = R.layout.activity_main
    override fun initView(savedInstanceState: Bundle?) {

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                var nav = Navigation.findNavController(this@MainActivity, R.id.host_fragment)
                if (nav.currentDestination != null && nav.currentDestination!!.id != R.id.mainFragment) {
                    nav.navigateUp()
                } else {
                    //是主页
                    if (System.currentTimeMillis() - exitTime > 2000) {
                        "再按一次退出程序".toast()
                        exitTime = System.currentTimeMillis()
                    } else {
                        finish()
                    }
                }
            }

        })

        appViewModel.appColor.value?.let {
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
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
            "我特么终于有网了啊!".toast()
        } else {
            "我特么怎么断网了!".toast()
        }
    }
}