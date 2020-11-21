package com.uiuang.cloudknowledge.ui.fragment.mine

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.kingja.loadsir.core.LoadService
import com.uiuang.cloudknowledge.R
import com.uiuang.cloudknowledge.app.base.BaseFragment
import com.uiuang.cloudknowledge.bean.wan.IntegralBean
import com.uiuang.cloudknowledge.databinding.FragmentIntegralBinding
import com.uiuang.cloudknowledge.ext.*
import com.uiuang.cloudknowledge.ui.adapter.wan.IntegralAdapter
import com.uiuang.cloudknowledge.viewmodel.request.RequestIntegralViewModel
import com.uiuang.cloudknowledge.viewmodel.state.IntegralViewModel
import com.uiuang.cloudknowledge.weight.recyclerview.SpaceItemDecoration
import com.uiuang.mvvm.ext.nav
import com.uiuang.mvvm.ext.navigateAction
import com.uiuang.mvvm.ext.view.gone
import com.uiuang.mvvm.ext.view.notNull
import com.uiuang.mvvm.util.dp2px
import com.yanzhenjie.recyclerview.SwipeRecyclerView
import kotlinx.android.synthetic.main.fragment_integral.*
import kotlinx.android.synthetic.main.include_list.*
import kotlinx.android.synthetic.main.include_recyclerview.*
import kotlinx.android.synthetic.main.include_toolbar.*

/**
 *
 */
class IntegralFragment : BaseFragment<IntegralViewModel, FragmentIntegralBinding>() {

    private var rank: IntegralBean? = null

    //适配器
    private lateinit var integralAdapter: IntegralAdapter

    //界面状态管理者
    private lateinit var loadSir: LoadService<Any>

    //请求的ViewModel
    private val requestIntegralViewModel: RequestIntegralViewModel by viewModels()

    override fun layoutId(): Int = R.layout.fragment_integral

    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.vm = mViewModel
        rank = arguments?.getParcelable("integral")
        rank.notNull({
            mViewModel.integralBean.set(rank)
        }, {
            integral_cardview.gone()
        })
        integralAdapter = IntegralAdapter(arrayListOf(), rank?.rank ?: -1)
        toolbar.run {
            inflateMenu(R.menu.integral_menu)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.integral_rule -> {
                        val url = "https://www.wanandroid.com/blog/show/2653"
                        val title = "积分规则"
//                        WebViewFragment.openDetail(it.actionView,"","积分规则")
                        nav().navigateAction(R.id.action_global_webViewFragment, Bundle().apply {
                            putString("url", url)
                            putBoolean("isTitleFix", false)
                            putString("title", if (title.isNullOrEmpty()) url else title)
                        })
                    }

                    R.id.integral_history -> {
                        nav().navigateAction(R.id.action_integralFragment_to_integralHistoryFragment)
                    }
                }
                true
            }
            initClose("积分排行") {
                nav().navigateUp()
            }
        }
        //状态页配置
        loadSir = loadServiceInit(swipeRefresh) {
            //点击重试时触发的操作
            loadSir.showLoading()
            requestIntegralViewModel.getIntegralData(true)
        }
        //初始化recyclerView
        recyclerView.init(LinearLayoutManager(context), integralAdapter).let {
            it.addItemDecoration(SpaceItemDecoration(0, requireContext().dp2px(8)))
            it.initFooter(SwipeRecyclerView.LoadMoreListener {
                //触发加载更多时请求数据
                requestIntegralViewModel.getIntegralData(false)
            })
            //初始化FloatingActionButton
            it.initFloatBtn(floatBtn)
        }
        //初始化 SwipeRefreshLayout
        swipeRefresh.init {
            //触发刷新监听时请求数据
            requestIntegralViewModel.getIntegralData(true)
        }
        appViewModel.appColor.value?.let {
            setUiTheme(
                it,
                integral_merank, integral_mename, integral_mecount
            )
        }
    }

    override fun lazyLoadData() {
        //设置界面 加载中
        loadSir.showLoading()
        requestIntegralViewModel.getIntegralData(true)
    }

    override fun createObserver() {
        requestIntegralViewModel.integralDataState.observe(viewLifecycleOwner, Observer {
            //设值 新写了个拓展函数，搞死了这个恶心的重复代码
            loadListData(it, integralAdapter, loadSir, recyclerView, swipeRefresh)
        })
    }


}