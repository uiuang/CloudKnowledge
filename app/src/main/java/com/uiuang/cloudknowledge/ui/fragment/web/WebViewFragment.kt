package com.uiuang.cloudknowledge.ui.fragment.web

import android.Manifest
import android.content.DialogInterface
import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.webkit.WebView
import android.webkit.WebView.HitTestResult
import android.widget.LinearLayout
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.just.agentweb.AgentWeb
import com.just.agentweb.WebChromeClient
import com.uiuang.cloudknowledge.R
import com.uiuang.cloudknowledge.app.base.BaseFragment
import com.uiuang.cloudknowledge.app.http.Constants
import com.uiuang.cloudknowledge.databinding.FragmentWebViewBinding
import com.uiuang.cloudknowledge.ext.hideSoftKeyboard
import com.uiuang.cloudknowledge.utils.*
import com.uiuang.cloudknowledge.viewmodel.state.WebViewModel
import com.uiuang.mvvm.base.appContext
import com.uiuang.mvvm.ext.nav
import com.uiuang.mvvm.ext.navigateAction
import com.uiuang.mvvm.util.toHtml
import kotlinx.android.synthetic.main.fragment_web_view.*


/**
 * A simple [Fragment] subclass.
 */
class WebViewFragment : BaseFragment<WebViewModel, FragmentWebViewBinding>() {

    private var url: String = ""
    private var mTitle: String = ""
    private var isTitleFix: Boolean = false
    private var agentWeb: AgentWeb? = null

    companion object {
        fun openDetail(view: View, url: String?, title: String?, isTitleFix: Boolean = false) {
            if (!url.isNullOrEmpty()) {
                nav(view).navigateAction(R.id.action_global_webViewFragment, Bundle().apply {
                    putString("url", url)
                    putBoolean("isTitleFix", isTitleFix)
                    putString("title", if (title.isNullOrEmpty()) url else title)
                })
            }
        }
    }

    override fun layoutId(): Int = R.layout.fragment_web_view

    override fun initView(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        arguments?.run {
            url = getString("url").toString()
            mTitle = getString("title").toString()
            isTitleFix = getBoolean("isTitleFix")
        }

        title_tool_bar.run {
            mActivity.setSupportActionBar(this)
            setBackgroundColor(SettingUtil.getColor(appContext))
            overflowIcon = ContextCompat.getDrawable(requireActivity(), R.drawable.actionbar_more)
            tv_gun_title.text = mTitle.toHtml()
            setNavigationIcon(R.drawable.ic_back)
            setNavigationOnClickListener {
                if (agentWeb!!.webCreator.webView.canGoBack()) {
                    agentWeb!!.webCreator.webView.goBack()
                } else {
                    nav().navigateUp()
                }
            }
            hideSoftKeyboard(activity)
            agentWeb?.let { web ->
                if (web.webCreator.webView.canGoBack()) {
                    web.webCreator.webView.goBack()
                } else {
                    nav().navigateUp()
                }
            }


        }

    }


    override fun lazyLoadData() {
        //加载网页
        agentWeb = AgentWeb.with(this)
            .setAgentWebParent(webcontent, LinearLayout.LayoutParams(-1, -1))
            .useDefaultIndicator()
            .createAgentWeb()
            .ready()
            .go(url)

        requireActivity().onBackPressedDispatcher.addCallback(this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    agentWeb?.let { web ->
                        if (web.webCreator.webView.canGoBack()) {
                            web.webCreator.webView.goBack()
                        } else {
                            nav().navigateUp()
                        }
                    }
                }
            })
        agentWeb!!.webCreator.webView.webChromeClient = object : WebChromeClient() {
            override fun onReceivedTitle(view: WebView?, title: String) {
                if (!TextUtils.isEmpty(title)) {
                    mTitle = title
                    tv_gun_title.text = title
                }
                super.onReceivedTitle(view, title)
            }
        }
        agentWeb!!.webCreator.webView.setOnLongClickListener {
            handleLongImage()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.webview_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.actionbar_share -> {
                // 分享到
                val shareText =
                    "$mTitle " + agentWeb?.webCreator?.webView?.url + " ( 分享自云知 " + Constants.DOWNLOAD_URL + " )"
                ShareUtils.share(requireActivity(), shareText)
            }
            R.id.actionbar_cope -> {
                // 复制链接
                (agentWeb!!.webCreator.webView.url).copy()
                "已复制到剪贴板".toast()
            }
            R.id.actionbar_open ->                 // 打开链接
                (agentWeb!!.webCreator.webView.url).openLink(requireActivity())
//                BaseTools.openLink(this@WebViewActivity, byWebView.getWebView().getUrl())
            R.id.actionbar_webview_refresh ->                 // 刷新页面
                //刷新网页
                agentWeb?.urlLoader?.reload()
            R.id.actionbar_collect -> {
                //                if (UserUtil.isLogin(byWebView.getWebView().getContext())) {
//                    if (SPUtils.getBoolean(Constants.IS_FIRST_COLLECTURL, true)) {
//                        DialogBuild.show(byWebView.getWebView(),
//                            "网址不同于文章，相同网址可多次进行收藏，且不会显示收藏状态。",
//                            "知道了",
//                            DialogInterface.OnClickListener { dialog: DialogInterface?, which: Int ->
//                                SPUtils.putBoolean(Constants.IS_FIRST_COLLECTURL, false)
//                                collectUrl()
//                            } as DialogInterface.OnClickListener
//                        )
//                    } else {
//                        collectUrl()
//                    }
//                }
            }              // 添加到收藏

            else -> {
            }
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * 长按图片事件处理
     */
    private fun handleLongImage(): Boolean {
        val hitTestResult: HitTestResult = agentWeb!!.webCreator.webView.hitTestResult
        // 如果是图片类型或者是带有图片链接的类型
        if (hitTestResult.type == HitTestResult.IMAGE_TYPE || hitTestResult.type == HitTestResult.SRC_IMAGE_ANCHOR_TYPE
        ) {
            // 弹出保存图片的对话框
            AlertDialog.Builder(requireActivity())
                .setItems(
                    arrayOf("发送给朋友", "保存到相册"),
                    DialogInterface.OnClickListener { dialog, which -> //获取图片
                        val picUrl = hitTestResult.extra
                        when (which) {
                            0 -> ShareUtils.shareNetImage(requireActivity(), picUrl)
                            1 -> {
                                //TODO 保存到相册
//                                if (!PermissionHandler.isHandlePermission(
//                                        this@WebViewActivity,
//                                        Manifest.permission.WRITE_EXTERNAL_STORAGE
//                                    )
//                                ) {
//                                    return@OnClickListener
//                                }
//                                RxSaveImage.saveImageToGallery(
//                                    this@WebViewActivity,
//                                    picUrl,
//                                    picUrl
//                                )
                            }
                            else -> {
                            }
                        }
                    })
                .show()
            return true
        }
        return false
    }

    override fun onPause() {
        agentWeb?.webLifeCycle?.onPause()
        super.onPause()
    }

    override fun onResume() {
        agentWeb?.webLifeCycle?.onResume()
        super.onResume()
    }

    override fun onDestroy() {
        agentWeb?.webLifeCycle?.onDestroy()
        mActivity.setSupportActionBar(null)
        super.onDestroy()
    }

}
