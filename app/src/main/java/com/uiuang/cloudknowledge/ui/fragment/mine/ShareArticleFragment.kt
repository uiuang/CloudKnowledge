package com.uiuang.cloudknowledge.ui.fragment.mine

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.WhichButton
import com.afollestad.materialdialogs.actions.getActionButton
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.uiuang.cloudknowledge.R
import com.uiuang.cloudknowledge.app.base.BaseFragment
import com.uiuang.cloudknowledge.databinding.FragmentShareArticleBinding
import com.uiuang.cloudknowledge.ext.initClose
import com.uiuang.cloudknowledge.ext.showMessage
import com.uiuang.cloudknowledge.utils.SettingUtil
import com.uiuang.cloudknowledge.utils.toast
import com.uiuang.cloudknowledge.viewmodel.request.RequestArticleViewModel
import com.uiuang.cloudknowledge.viewmodel.state.ArticleViewModel
import com.uiuang.mvvm.ext.nav
import com.uiuang.mvvm.ext.parseState
import com.uiuang.mvvm.ext.view.clickNoRepeat
import kotlinx.android.synthetic.main.fragment_share_article.*
import kotlinx.android.synthetic.main.include_toolbar.*

/**
 * 分享文章页面
 */
class ShareArticleFragment : BaseFragment<ArticleViewModel, FragmentShareArticleBinding>() {

    private val requestViewModel: RequestArticleViewModel by viewModels()

    override fun layoutId(): Int = R.layout.fragment_share_article
    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.vm = mViewModel

        appViewModel.userinfo.value?.let {
            if (it.nickname.isEmpty()) mViewModel.shareName.set(it.username) else mViewModel.shareName.set(
                it.nickname
            )
        }
        appViewModel.appColor.value?.let { SettingUtil.setShapeColor(share_submit, it) }

        toolbar.run {
            initClose("分享文章") {
                nav().navigateUp()
            }
            inflateMenu(R.menu.share_menu)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.share_rule -> {
                        activity?.let { activity ->
                            MaterialDialog(activity, BottomSheet())
                                .lifecycleOwner(viewLifecycleOwner)
                                .show {
                                    title(text = "温馨提示")
                                    customView(
                                        R.layout.dialog_share_rule,
                                        scrollable = true,
                                        horizontalPadding = true
                                    )
                                    positiveButton(text = "知道了")
                                    cornerRadius(16f)
                                    getActionButton(WhichButton.POSITIVE).updateTextColor(
                                        SettingUtil.getColor(activity)
                                    )
                                    getActionButton(WhichButton.NEGATIVE).updateTextColor(
                                        SettingUtil.getColor(activity)
                                    )
                                }
                        }

                    }
                }
                true
            }
        }
        share_submit.clickNoRepeat {
            when {
                mViewModel.shareTitle.get().isEmpty() -> {
                    "请填写文章标题".toast()
                }
                mViewModel.shareUrl.get().isEmpty() -> {
                    "请填写文章链接".toast()
                }
                else -> {
                    showMessage("确定分享吗？", positiveButtonText = "分享", positiveAction = {
                        requestViewModel.addArticle(
                            mViewModel.shareTitle.get(),
                            mViewModel.shareUrl.get()
                        )
                    }, negativeButtonText = "取消")
                }
            }
        }
    }

    override fun createObserver() {
        requestViewModel.addData.observe(viewLifecycleOwner, Observer { resultState ->
            parseState(resultState, {
                eventViewModel.shareArticleEvent.value = true
                nav().navigateUp()
            }, {
                it.errorMsg.toast()
            })
        })
    }


}