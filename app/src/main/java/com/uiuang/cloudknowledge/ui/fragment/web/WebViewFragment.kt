package com.uiuang.cloudknowledge.ui.fragment.web

import android.content.DialogInterface
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.webkit.WebView
import android.webkit.WebView.HitTestResult
import android.widget.LinearLayout
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.just.agentweb.AgentWeb
import com.just.agentweb.WebChromeClient
import com.uiuang.cloudknowledge.R
import com.uiuang.cloudknowledge.app.base.BaseFragment
import com.uiuang.cloudknowledge.app.http.Constants
import com.uiuang.cloudknowledge.bean.CollectBus
import com.uiuang.cloudknowledge.bean.wan.WebBean
import com.uiuang.cloudknowledge.data.enums.CollectType
import com.uiuang.cloudknowledge.databinding.FragmentWebViewBinding
import com.uiuang.cloudknowledge.ext.hideSoftKeyboard
import com.uiuang.cloudknowledge.ext.initClose
import com.uiuang.cloudknowledge.ext.jumpByLogin
import com.uiuang.cloudknowledge.utils.*
import com.uiuang.cloudknowledge.viewmodel.request.RequestCollectViewModel
import com.uiuang.cloudknowledge.viewmodel.state.WebViewModel
import com.uiuang.mvvm.base.appContext
import com.uiuang.mvvm.ext.nav
import com.uiuang.mvvm.ext.navigateAction
import com.uiuang.mvvm.util.ShareUtils
import com.uiuang.mvvm.util.toHtml
import kotlinx.android.synthetic.main.fragment_web_view.*
import kotlinx.android.synthetic.main.include_toolbar.*


/**
 * A simple [Fragment] subclass.
 */
class WebViewFragment : BaseFragment<WebViewModel, FragmentWebViewBinding>() {


    private var agentWeb: AgentWeb? = null
    private var preWeb: AgentWeb.PreAgentWeb? = null
    private val requestCollectViewModel: RequestCollectViewModel by viewModels()

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
            val webBean = getParcelable<WebBean>("webBean")
            webBean?.let {
                mViewModel.articleId = it.id
                mViewModel.showTitle = it.title
                mViewModel.collect = it.collect
                mViewModel.url = it.url
                mViewModel.collectType = it.collectType
            }
        }

        toolbar.run {
            mActivity.setSupportActionBar(this)
            initClose(mViewModel.showTitle) {
                hideSoftKeyboard(activity)
                if (agentWeb!!.webCreator.webView.canGoBack()) {
                    agentWeb!!.webCreator.webView.goBack()
                } else {
                    nav().navigateUp()
                }
            }
            overflowIcon = ContextCompat.getDrawable(requireActivity(), R.drawable.actionbar_more)
        }
        agentWeb?.let { web ->
            if (web.webCreator.webView.canGoBack()) {
                web.webCreator.webView.goBack()
            } else {
                nav().navigateUp()
            }
        }
        //加载网页
        preWeb = AgentWeb.with(this)
            .setAgentWebParent(webcontent, LinearLayout.LayoutParams(-1, -1))
            .useDefaultIndicator()
            .createAgentWeb()
            .ready()

    }


    override fun lazyLoadData() {

//加载网页
        agentWeb = preWeb?.go(mViewModel.url)
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
//        agentWeb!!.webCreator.webView.webChromeClient = object : WebChromeClient() {
//            override fun onReceivedTitle(view: WebView?, title: String) {
//                if (!TextUtils.isEmpty(title)) {
//                    mTitle = title
//                    tv_gun_title.text = title
//                }
//                super.onReceivedTitle(view, title)
//            }
//        }
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
                    "${mViewModel.showTitle} " + agentWeb?.webCreator?.webView?.url + " ( 分享自云知 " + Constants.DOWNLOAD_URL + " )"
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
                if (CacheUtil.isLogin()) {
                    if (mViewModel.collect) {
                        if (mViewModel.collectType == CollectType.Url.type) {
                            //取消收藏网址
                            requestCollectViewModel.unCollectUrl(mViewModel.articleId)
                        } else {
                            requestCollectViewModel.unCollect(mViewModel.articleId)
                        }
                    } else {
                        if (mViewModel.collectType == CollectType.Url.type) {
                            //收藏网址
                            requestCollectViewModel.collectUrl(mViewModel.showTitle, mViewModel.url)
                        } else {
                            //收藏文章
                            requestCollectViewModel.collect(mViewModel.articleId)
                        }
                    }
                } else {
                    nav().navigateAction(R.id.action_global_loginFragment)
                }
            }              // 添加到收藏

            else -> {
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        //如果收藏了，右上角的图标相对应改变
        context?.let {
            if (mViewModel.collect) {
                menu.findItem(R.id.actionbar_collect).title =
                    resources.getString(R.string.actionbar_webview_collected)
            } else {
                menu.findItem(R.id.actionbar_collect).title =
                    resources.getString(R.string.actionbar_webview_collect)
            }
        }
        return super.onPrepareOptionsMenu(menu)
    }

    override fun createObserver() {
        requestCollectViewModel.collectUiState.observe(viewLifecycleOwner, Observer {
            if (it.isSuccess) {
                mViewModel.collect = it.collect
                eventViewModel.collectEvent.value = CollectBus(it.id, it.collect)
                //刷新一下menu
                mActivity.window?.invalidatePanelMenu(Window.FEATURE_OPTIONS_PANEL)
                mActivity.invalidateOptionsMenu()
                if (it.collect) "文章收藏成功".toast() else "文章取消收藏成功".toast()
            } else it.errorMsg.toast()
        })
        requestCollectViewModel.collectUrlUiState.observe(viewLifecycleOwner, Observer {
            if (it.isSuccess) {
                eventViewModel.collectEvent.value = CollectBus(it.id, it.collect)
                mViewModel.collect = it.collect
                //刷新一下menu
                mActivity.window?.invalidatePanelMenu(Window.FEATURE_OPTIONS_PANEL)
                mActivity.invalidateOptionsMenu()
                if (it.collect) "Url收藏成功".toast() else "url取消收藏成功".toast()
            } else it.errorMsg.toast()
        })
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
