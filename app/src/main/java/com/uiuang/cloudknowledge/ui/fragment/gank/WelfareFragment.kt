package com.uiuang.cloudknowledge.ui.fragment.gank

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.gson.GsonBuilder
import com.kingja.loadsir.core.LoadService
import com.uiuang.cloudknowledge.R
import com.uiuang.cloudknowledge.app.base.BaseFragment
import com.uiuang.cloudknowledge.bean.gank.GankIOResultBean
import com.uiuang.cloudknowledge.databinding.IncludeListBinding
import com.uiuang.cloudknowledge.ext.*
import com.uiuang.cloudknowledge.ui.adapter.gank.WelfareAdapter
import com.uiuang.cloudknowledge.utils.ACache
import com.uiuang.cloudknowledge.utils.GsonExclusionStrategy
import com.uiuang.cloudknowledge.viewmodel.request.RequestSisterViewModel
import com.uiuang.cloudknowledge.viewmodel.state.HomeViewModel
import com.uiuang.cloudknowledge.weight.recyclerview.DefineLoadMoreView
import com.uiuang.cloudknowledge.weight.recyclerview.GridSpaceItemDecoration
import com.uiuang.mvvm.ext.nav
import com.uiuang.mvvm.ext.navigateAction
import com.yanzhenjie.recyclerview.SwipeRecyclerView
import kotlinx.android.synthetic.main.include_list.*
import kotlinx.android.synthetic.main.include_recyclerview.*


class WelfareFragment : BaseFragment<HomeViewModel, IncludeListBinding>() {
    //界面状态管理者
    private lateinit var loadSir: LoadService<Any>

    //请求ViewModel
    private val requestSisterViewModel: RequestSisterViewModel by viewModels()

    //recyclerview的底部加载view 因为要在首页动态改变他的颜色，所以加了他这个字段
    private lateinit var footView: DefineLoadMoreView

    private val welfareAdapter: WelfareAdapter by lazy {
        WelfareAdapter(arrayListOf())
    }

    companion object {
        @JvmStatic
        fun newInstance() = WelfareFragment()
    }

    override fun layoutId(): Int = R.layout.include_list

    override fun initView(savedInstanceState: Bundle?) {
        //状态页配置
        loadSir = loadServiceInit(swipeRefresh) {
            //点击重试时触发的操作
            loadSir.showLoading()
            requestSisterViewModel.getPlazaData(true)
        }
        //初始化recyclerView
        recyclerView.init(
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL),
            welfareAdapter
        ).let {
            it.addItemDecoration(GridSpaceItemDecoration(12))
            footView = it.initFooter(SwipeRecyclerView.LoadMoreListener {
                requestSisterViewModel.getPlazaData(false)
            })
            //初始化FloatingActionButton
            it.initFloatBtn(floatBtn)
        }
        //初始化 SwipeRefreshLayout
        swipeRefresh.init {
            //触发刷新监听时请求数据
            requestSisterViewModel.getPlazaData(true)
        }
        welfareAdapter.run {
            setOnItemClickListener { _, _, position ->
                val data: MutableList<GankIOResultBean> = welfareAdapter.data
                nav().navigateAction(R.id.action_mainFragment_to_bigImageFragment, Bundle().apply {
                    putInt("position", position)
                })
                val gson = GsonBuilder().addSerializationExclusionStrategy(GsonExclusionStrategy()).create()
                ACache[mActivity].put("ImageItemsBean", gson.toJson(data))

            }

        }
    }

    override fun createObserver() {
        super.createObserver()
        requestSisterViewModel.sisterDataState.observe(viewLifecycleOwner, Observer {
            //设值 新写了个拓展函数，搞死了这个恶心的重复代码
            loadListData(it, welfareAdapter, loadSir, recyclerView, swipeRefresh)
        })
        appViewModel.appColor.observe(viewLifecycleOwner, Observer {
            //监听全局的主题颜色改变
            setUiTheme(it, floatBtn, swipeRefresh, loadSir, footView)
        })
    }

    override fun lazyLoadData() {
        //设置界面 加载中
        loadSir.showLoading()
        requestSisterViewModel.getPlazaData(true)
    }
}