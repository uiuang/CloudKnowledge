package com.uiuang.cloudknowledge.ui.fragment.mine

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.uiuang.cloudknowledge.R
import com.uiuang.cloudknowledge.app.base.BaseFragment
import com.uiuang.cloudknowledge.bean.IntegralBean
import com.uiuang.cloudknowledge.databinding.FragmentMineBinding
import com.uiuang.cloudknowledge.ext.init
import com.uiuang.cloudknowledge.ext.jumpByLogin
import com.uiuang.cloudknowledge.utils.joinQQGroup
import com.uiuang.cloudknowledge.utils.toast
import com.uiuang.cloudknowledge.viewmodel.request.RequestMineViewModel
import com.uiuang.cloudknowledge.viewmodel.state.MineViewModel
import com.uiuang.mvvm.ext.nav
import com.uiuang.mvvm.ext.navigateAction
import com.uiuang.mvvm.ext.parseState
import com.uiuang.mvvm.ext.view.notNull
import kotlinx.android.synthetic.main.fragment_mine.*


class MineFragment : BaseFragment<MineViewModel, FragmentMineBinding>() {
    private var rank: IntegralBean? = null
    private val requestMineViewModel: RequestMineViewModel by viewModels()
    override fun layoutId(): Int = R.layout.fragment_mine

    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.click = ProxyClick()
        mDatabind.vm = mViewModel

        mine_swipe.init {
            requestMineViewModel.getIntegral()
        }
    }

    override fun createObserver() {
        requestMineViewModel.integralBean.observe(viewLifecycleOwner, Observer { resultState ->
            mine_swipe.isRefreshing = false
            parseState(resultState, {
                rank = it
                mViewModel.info.set("id：${it.userId}　排名：${it.rank}")
                mViewModel.name.set(it.username)
                mViewModel.integral.set(it.coinCount)
            }, {
                it.errorMsg.toast()
            })
        })
        appViewModel.run {
            appColor.observe(viewLifecycleOwner, Observer {

            })
            userinfo.observe(viewLifecycleOwner, Observer { userInfo ->
                userInfo.notNull({
                    mine_swipe.isRefreshing = true
                    mViewModel.name.set(if (it.nickname.isEmpty()) it.username else it.nickname)
                    requestMineViewModel.getIntegral()
                }, {
                    mViewModel.name.set("请先登录~")
                    mViewModel.info.set("id：--　排名：--")
                    mViewModel.integral.set(0)
                })
            })
        }
    }

    inner class ProxyClick {
        /** 登录 */
        fun login() {
            nav().jumpByLogin {}
        }

        /** 收藏 */
        fun collect() {
            nav().jumpByLogin {
                it.navigateAction(R.id.action_mainFragment_to_collectFragment)
            }
        }

        /** 积分 */
        fun integral() {
            nav().jumpByLogin {
                it.navigateAction(
                    R.id.action_mainFragment_to_integralFragment,Bundle().apply {
                        putParcelable("integral", rank)
                    }
                )
            }
        }

        /** 文章 */
        fun article() {
            nav().jumpByLogin {
                it.navigateAction(R.id.action_mainFragment_to_articleFragment)
            }
        }

        fun todo() {
            nav().jumpByLogin {
                it.navigateAction(R.id.action_mainFragment_to_todoListFragment)
            }
        }

        /** 玩Android开源网站 */
        fun about() {

        }

        fun home() {
            nav().navigateAction(R.id.action_global_webViewFragment, Bundle())
        }

        /** 加入我们 */
        fun join() {
            ("9n4i5sHt4189d4DvbotKiCHy-5jZtD4D").joinQQGroup(requireActivity())
        }

        /** 设置*/
        fun setting() {
            nav().navigateAction(R.id.action_mainFragment_to_settingFragment)
        }

        /**demo*/
        fun demo() {
//            nav().navigateAction(R.id.action_mainfragment_to_demoFragment)
        }
    }

    override fun lazyLoadData() {
        appViewModel.userinfo.value?.run {
            mine_swipe.isRefreshing = true
            requestMineViewModel.getIntegral()
        }
    }
}