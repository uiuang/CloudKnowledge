package com.uiuang.cloudknowledge.ui.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import com.uiuang.cloudknowledge.R
import com.uiuang.cloudknowledge.app.base.BaseActivity
import com.uiuang.cloudknowledge.databinding.ActivitySplashBinding
import com.uiuang.cloudknowledge.ext.loadUrl
import com.uiuang.cloudknowledge.utils.ColorUtil
import com.uiuang.cloudknowledge.utils.toast
import com.uiuang.cloudknowledge.viewmodel.state.SplashViewModel
import kotlinx.android.synthetic.main.activity_splash.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.OnNeverAskAgain
import permissions.dispatcher.OnPermissionDenied
import permissions.dispatcher.RuntimePermissions

@RuntimePermissions
class SplashActivity : BaseActivity<SplashViewModel, ActivitySplashBinding>(),
    CoroutineScope by MainScope() {


    override fun layoutId(): Int = R.layout.activity_splash

    override fun initView(savedInstanceState: Bundle?) {
        // 后台返回时可能启动这个页面 http://blog.csdn.net/jianiuqi/article/details/54091181
        if (intent.flags and Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT != 0) {
            finish()
            return
        }
        imageView.loadUrl(this, ColorUtil.randomWallpaperUrl())
        needPermissionWithPermissionCheck()
    }

    @NeedsPermission(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )
    fun needPermission() {
        toMain()
    }

    @OnNeverAskAgain(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )
    fun openPermission() {
        toMain()
    }

    @OnPermissionDenied(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )
    fun deniedPermission() {
        "拒绝权限部分功能将不能使用，请去设置打开权限".toast()
        toMain()
    }


    @SuppressLint("NeedOnRequestPermissionsResult")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode, grantResults)
    }

    private fun toMain() = launch {
        delay(timeMillis = 2000L)
        startActivity(Intent(this@SplashActivity, MainActivity::class.java))
        overridePendingTransition(R.anim.screen_zoom_in, R.anim.screen_zoom_out)
        finish()
    }


}
