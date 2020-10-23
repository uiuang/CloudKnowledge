package com.uiuang.cloudknowledge.ui.fragment.mine

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.kingja.loadsir.core.LoadService
import com.uiuang.cloudknowledge.R
import com.uiuang.cloudknowledge.app.base.BaseFragment
import com.uiuang.cloudknowledge.databinding.FragmentIntegralHistoryBinding
import com.uiuang.cloudknowledge.ext.*
import com.uiuang.cloudknowledge.ui.adapter.wan.IntegralHistoryAdapter
import com.uiuang.cloudknowledge.viewmodel.request.RequestIntegralViewModel
import com.uiuang.cloudknowledge.viewmodel.state.IntegralViewModel
import com.uiuang.cloudknowledge.weight.recyclerview.SpaceItemDecoration
import com.uiuang.mvvm.ext.nav
import com.uiuang.mvvm.util.dp2px
import com.yanzhenjie.recyclerview.SwipeRecyclerView
import kotlinx.android.synthetic.main.include_list.*
import kotlinx.android.synthetic.main.include_recyclerview.*
import kotlinx.android.synthetic.main.include_toolbar.*

/**
 * 我的获取历史
 *
 */
class IntegralHistoryFragment : BaseFragment<IntegralViewModel, FragmentIntegralHistoryBinding>() {
    //适配器
    private val integralAdapter: IntegralHistoryAdapter by lazy { IntegralHistoryAdapter(arrayListOf()) }

    //界面状态管理者
    private lateinit var loadSir: LoadService<Any>

    //请求的ViewModel /** */
    private val requestIntegralViewModel: RequestIntegralViewModel by viewModels()

    override fun layoutId(): Int = R.layout.fragment_integral_history

    override fun initView(savedInstanceState: Bundle?) {
        toolbar.initClose("积分记录") {
            nav().navigateUp()
        }
        //状态页配置
        loadSir = loadServiceInit(swipeRefresh) {
            //点击重试时触发的操作
            loadSir.showLoading()
            requestIntegralViewModel.getIntegralHistoryData(true)
        }
        //初始化recyclerView
        recyclerView.init(LinearLayoutManager(context), integralAdapter).let {
            it.addItemDecoration(SpaceItemDecoration(0, requireContext().dp2px(8)))
            it.initFooter(SwipeRecyclerView.LoadMoreListener {
                //触发加载更多时请求数据
                requestIntegralViewModel.getIntegralHistoryData(false)
            })
            //初始化FloatingActionButton
            it.initFloatBtn(floatBtn)
        }
        //初始化 SwipeRefreshLayout
        swipeRefresh.init {
            //触发刷新监听时请求数据
            requestIntegralViewModel.getIntegralHistoryData(true)
        }
    }
    override fun lazyLoadData() {
        //设置界面 加载中
        loadSir.showLoading()
        requestIntegralViewModel.getIntegralHistoryData(true)
    }

    override fun createObserver() {
        requestIntegralViewModel.integralHistoryDataState.observe(viewLifecycleOwner, Observer {
            //设值 新写了个拓展函数，搞死了这个恶心的重复代码
            loadListData(it, integralAdapter, loadSir, recyclerView,swipeRefresh)
        })
    }

}