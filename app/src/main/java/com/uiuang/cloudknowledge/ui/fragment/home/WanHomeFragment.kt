package com.uiuang.cloudknowledge.ui.fragment.home

import android.os.Bundle
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.kingja.loadsir.core.LoadService
import com.uiuang.cloudknowledge.R
import com.uiuang.cloudknowledge.app.base.BaseFragment
import com.uiuang.cloudknowledge.databinding.FragmentWanHomeBinding
import com.uiuang.cloudknowledge.databinding.HeaderWanAndroidBinding
import com.uiuang.cloudknowledge.ext.*
import com.uiuang.cloudknowledge.ui.adapter.wan.WanAndroidAdapter
import com.uiuang.cloudknowledge.ui.adapter.wan.WanBannerAdapter
import com.uiuang.cloudknowledge.ui.fragment.web.WebViewFragment
import com.uiuang.cloudknowledge.utils.toast
import com.uiuang.cloudknowledge.viewmodel.request.RequestWanHomeViewModel
import com.uiuang.cloudknowledge.viewmodel.state.HomeViewModel
import com.uiuang.cloudknowledge.weight.recyclerview.DefineLoadMoreView
import com.uiuang.mvvm.ext.nav
import com.uiuang.mvvm.ext.navigateAction
import com.uiuang.mvvm.util.dp2px
import com.uiuang.mvvm.util.screenWidth
import com.yanzhenjie.recyclerview.SwipeRecyclerView
import com.youth.banner.indicator.CircleIndicator
import kotlinx.android.synthetic.main.fragment_sister.recyclerView
import kotlinx.android.synthetic.main.fragment_sister.swipeRefresh
import kotlinx.android.synthetic.main.fragment_wan_home.*


class WanHomeFragment : BaseFragment<HomeViewModel, FragmentWanHomeBinding>() {
    //界面状态管理者
    private lateinit var loadSir: LoadService<Any>

    private val requestWanHomeViewModel: RequestWanHomeViewModel by viewModels()

    //recyclerview的底部加载view 因为要在首页动态改变他的颜色，所以加了他这个字段
    private lateinit var footView: DefineLoadMoreView

    private lateinit var headerWanAndroidBinding: HeaderWanAndroidBinding

    private val wanAndroidAdapter: WanAndroidAdapter by lazy {
        WanAndroidAdapter(arrayListOf())
    }

    private val wanBannerAdapter: WanBannerAdapter by lazy {
        WanBannerAdapter(arrayListOf())
    }

    companion object {
        @JvmStatic
        fun newInstance() = WanHomeFragment()
    }

    override fun layoutId(): Int = R.layout.fragment_wan_home

    override fun initView(savedInstanceState: Bundle?) {
        //状态页配置
        loadSir = loadServiceInit(swipeRefresh) {
            //点击重试时触发的操作
            loadSir.showLoading()
            requestWanHomeViewModel.getWanAndroidBanner()
        }

        //初始化recyclerView
        headerWanAndroidBinding = DataBindingUtil.inflate<HeaderWanAndroidBinding>(
            layoutInflater,
            R.layout.header_wan_android,
            null,
            false
        )
        val px = requireActivity().dp2px(100)
        val screenWidth: Int = requireActivity().screenWidth
        val width: Int = screenWidth.minus(px)
        val height: Float = width / 1.8f
        val lp = ConstraintLayout.LayoutParams(screenWidth, height.toInt())
        headerWanAndroidBinding.banner.layoutParams = lp

        headerWanAndroidBinding.radioGroup.visibility = View.VISIBLE
        headerWanAndroidBinding.rb1.setOnCheckedChangeListener { _, isChecked ->
            refresh(isChecked, isArticle = true, isRefresh = true)
        }
        headerWanAndroidBinding.rb2.setOnCheckedChangeListener { _, isChecked ->
            refresh(isChecked, isArticle = false, isRefresh = true)
        }
        recyclerView.addHeaderView(headerWanAndroidBinding.root)
        recyclerView.init(
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false),
            wanAndroidAdapter
        ).let {
            it.addItemDecoration(
                DividerItemDecoration(
                    requireActivity(),
                    DividerItemDecoration.VERTICAL
                )
            )
            footView = it.initFooter(SwipeRecyclerView.LoadMoreListener {
                if (headerWanAndroidBinding.rb1.isChecked) {
                    refresh(isChecked = true, isArticle = true, isRefresh = false)
                } else {
                    refresh(isChecked = true, isArticle = false, isRefresh = false)
                }
            })
//            //初始化FloatingActionButton
            it.initFloatBtn(floatBtnWanAndroid)
        }
        //初始化 SwipeRefreshLayout
        swipeRefresh.init {
            //触发刷新监听时请求数据
            requestWanHomeViewModel.getWanAndroidBanner()
            if (headerWanAndroidBinding.rb1.isChecked) {
                refresh(isChecked = true, isArticle = true, isRefresh = true)
            } else {
                refresh(isChecked = true, isArticle = false, isRefresh = true)
            }
        }

        headerWanAndroidBinding.banner.run {
            addBannerLifecycleObserver(this@WanHomeFragment)//添加生命周期观察者
            adapter = wanBannerAdapter
            indicator = CircleIndicator(requireActivity())
            setOnBannerListener { _, position ->
                val item = wanBannerAdapter.getData(position)
                openDetail(item.url, item.title)
            }
        }
        wanAndroidAdapter.run {
            addChildClickViewIds(R.id.tv_tag_name, R.id.cb_collect)
            setOnItemClickListener { _, view, position ->
                val item = getItem(position - 1)
                WebViewFragment.openDetail(view, item.link, item.title)
//                openDetail(item.link, item.title)
            }
            setOnItemChildClickListener { _, view, position ->
                when (view.id) {
                    R.id.tv_tag_name -> getItem(position - 1).chapterName?.toast()
                    R.id.cb_collect -> "未登录".toast()
                }
            }
        }
    }

    override fun createObserver() {
        super.createObserver()
        requestWanHomeViewModel.wanAndroidBannerBean.observe(viewLifecycleOwner, Observer {
            //设值 新写了个拓展函数，搞死了这个恶心的重复代码
            val listData = it.listData
            if (it.isSuccess) {
                loadSir.showSuccess()
                headerWanAndroidBinding.banner.setDatas(listData)
            } else loadSir.showError()
        })
        requestWanHomeViewModel.articlesBean.observe(viewLifecycleOwner, Observer {
            loadListData(it, wanAndroidAdapter, loadSir, recyclerView, swipeRefresh)
        })
    }

    override fun lazyLoadData() {
        //设置界面 加载中
        loadSir.showLoading()
        requestWanHomeViewModel.getWanAndroidBanner()
        refresh(isChecked = true, isArticle = true, isRefresh = true)
    }

    private fun refresh(isChecked: Boolean, isArticle: Boolean, isRefresh: Boolean) {
        if (isChecked) {
            swipeRefresh.isRefreshing = true
            wanAndroidAdapter.setNoImage(isArticle)
            if (isArticle)
                requestWanHomeViewModel.getHomeArticleList(isRefresh, null)
            else
                requestWanHomeViewModel.getHomeProjectList(isRefresh)

        }
    }

    private fun openDetail(url: String?, title: String?, isTitleFix: Boolean = false) {
        if (!url.isNullOrEmpty()) {
            nav().navigateAction(R.id.action_global_webViewFragment, Bundle().apply {
                putString("url", url)
                putBoolean("isTitleFix", isTitleFix)
                putString("title", if (title.isNullOrEmpty()) url else title)
            })
        }
    }

}