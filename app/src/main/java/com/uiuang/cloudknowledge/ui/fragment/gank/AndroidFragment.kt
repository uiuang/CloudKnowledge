package com.uiuang.cloudknowledge.ui.fragment.gank

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.kingja.loadsir.core.LoadService
import com.uiuang.cloudknowledge.R
import com.uiuang.cloudknowledge.app.base.BaseFragment
import com.uiuang.cloudknowledge.app.http.Constants.TYPE
import com.uiuang.cloudknowledge.bean.wan.WebBean
import com.uiuang.cloudknowledge.data.enums.CollectType
import com.uiuang.cloudknowledge.databinding.FragmentAndroidBinding
import com.uiuang.cloudknowledge.ext.*
import com.uiuang.cloudknowledge.ui.adapter.gank.GankAndroidAdapter
import com.uiuang.cloudknowledge.viewmodel.request.RequestGankViewModel
import com.uiuang.cloudknowledge.viewmodel.state.HomeViewModel
import com.uiuang.cloudknowledge.weight.recyclerview.DefineLoadMoreView
import com.uiuang.mvvm.ext.nav
import com.uiuang.mvvm.ext.navigateAction
import com.yanzhenjie.recyclerview.SwipeRecyclerView
import kotlinx.android.synthetic.main.include_list.*
import kotlinx.android.synthetic.main.include_recyclerview.*


class AndroidFragment : BaseFragment<HomeViewModel, FragmentAndroidBinding>() {
    private var type: String? = null

    //界面状态管理者
    private lateinit var loadSir: LoadService<Any>

    //请求ViewModel
    private val requestGankViewModel: RequestGankViewModel by viewModels()

    //recyclerview的底部加载view 因为要在首页动态改变他的颜色，所以加了他这个字段
    private lateinit var footView: DefineLoadMoreView

    private val gankAndroidAdapter: GankAndroidAdapter by lazy {
        GankAndroidAdapter()
    }

    companion object {
        @JvmStatic
        fun newInstance(type: String) = AndroidFragment().apply {
            arguments = Bundle().apply {
                putString(TYPE, type)
            }
        }
    }

    override fun layoutId(): Int = R.layout.include_list

    override fun initView(savedInstanceState: Bundle?) {
        arguments?.let {
            type = it.getString(TYPE)
        }

        //状态页配置
        loadSir = loadServiceInit(swipeRefresh) {
            //点击重试时触发的操作
            loadSir.showLoading()
            requestGankViewModel.loadGankData(true, type)
        }
        //初始化recyclerView
        recyclerView.init(
            LinearLayoutManager(requireActivity()),
            gankAndroidAdapter
        ).let {
//            it.addItemDecoration(GridSpaceItemDecoration(12))
            footView = it.initFooter(SwipeRecyclerView.LoadMoreListener {
                requestGankViewModel.loadGankData(false, type)
            })
            //初始化FloatingActionButton
            it.initFloatBtn(floatBtn)
        }
        //初始化 SwipeRefreshLayout
        swipeRefresh.init {
            //触发刷新监听时请求数据
            requestGankViewModel.loadGankData(true, type)
        }
        gankAndroidAdapter.setOnItemClickListener { _, _, position ->
            val item = gankAndroidAdapter.getItem(position)
            nav().navigateAction(R.id.action_global_webViewFragment, Bundle().apply {
                val webBean = WebBean(
                    0,
                    false,
                    item.title,
                    item.url,
                    CollectType.Url.type
                )
                putParcelable("webBean", webBean)
            })
        }
    }

    override fun createObserver() {
        requestGankViewModel.sisterDataState.observe(viewLifecycleOwner, Observer {
            //设值 新写了个拓展函数，搞死了这个恶心的重复代码
            loadListData(it, gankAndroidAdapter, loadSir, recyclerView, swipeRefresh)
        })
        appViewModel.appColor.observe(viewLifecycleOwner, Observer {
            //监听全局的主题颜色改变
            setUiTheme(it, floatBtn, swipeRefresh, loadSir, footView)
        })
    }

    override fun lazyLoadData() {
        //设置界面 加载中
        loadSir.showLoading()
        requestGankViewModel.loadGankData(true, type)
    }


}