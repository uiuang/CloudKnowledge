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
import com.uiuang.cloudknowledge.bean.event.CollectBus
import com.uiuang.cloudknowledge.bean.wan.WebBean
import com.uiuang.cloudknowledge.data.enums.CollectType
import com.uiuang.cloudknowledge.databinding.HeaderWanAndroidBinding
import com.uiuang.cloudknowledge.databinding.IncludeListBinding
import com.uiuang.cloudknowledge.ext.*
import com.uiuang.cloudknowledge.ui.adapter.wan.WanAndroidAdapter
import com.uiuang.cloudknowledge.ui.adapter.wan.WanBannerAdapter
import com.uiuang.cloudknowledge.utils.toast
import com.uiuang.cloudknowledge.viewmodel.request.RequestCollectViewModel
import com.uiuang.cloudknowledge.viewmodel.request.RequestWanHomeViewModel
import com.uiuang.cloudknowledge.viewmodel.state.HomeViewModel
import com.uiuang.cloudknowledge.weight.recyclerview.DefineLoadMoreView
import com.uiuang.mvvm.ext.nav
import com.uiuang.mvvm.ext.navigateAction
import com.uiuang.mvvm.util.dp2px
import com.uiuang.mvvm.util.screenWidth
import com.yanzhenjie.recyclerview.SwipeRecyclerView
import com.youth.banner.indicator.CircleIndicator
import kotlinx.android.synthetic.main.include_list.*
import kotlinx.android.synthetic.main.include_recyclerview.*
import kotlinx.android.synthetic.main.include_toolbar.*


class WanHomeFragment : BaseFragment<HomeViewModel, IncludeListBinding>() {
    //界面状态管理者
    private lateinit var loadSir: LoadService<Any>
    //收藏viewModel
    private val requestCollectViewModel: RequestCollectViewModel by viewModels()

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

    override fun layoutId(): Int = R.layout.include_list

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
            it.initFloatBtn(floatBtn)
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
                nav().navigateAction(R.id.action_global_webViewFragment, Bundle().apply {
                    val webBean = WebBean(
                        item.id,
                        false,
                        item.title,
                        item.url,
                        CollectType.Url.type
                    )
                    putParcelable("webBean", webBean)
                })
////                openDetail(item.url, item.title)
            }
        }
        wanAndroidAdapter.run {
            addChildClickViewIds(R.id.tv_tag_name)
            setOnItemClickListener { _, view, position ->
                val item = getItem(position - this@WanHomeFragment.recyclerView.headerCount)
                nav().navigateAction(R.id.action_global_webViewFragment, Bundle().apply {
                    val webBean = WebBean(
                        item.id,
                        item.collect,
                        item.title,
                        item.link,
                        CollectType.Article.type
                    )
                    putParcelable("webBean", webBean)
                })
            }
            setCollectClick { item, v, position ->
                if (v.isChecked) {
                    requestCollectViewModel.unCollect(item.id)
                } else {
                    requestCollectViewModel.collect(item.id)
                }
            }
            setOnItemChildClickListener { _, view, position ->
                when (view.id) {
                    R.id.tv_tag_name -> getItem(position - 1).chapterName?.toast()
                }
            }
        }
    }

    override fun createObserver() {
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

        requestCollectViewModel.collectUiState.observe(viewLifecycleOwner, Observer {
            if (it.isSuccess) {
                //收藏或取消收藏操作成功，发送全局收藏消息
                eventViewModel.collectEvent.value =
                    CollectBus(
                        it.id,
                        it.collect
                    )
            } else {
                showMessage(it.errorMsg)
                for (index in wanAndroidAdapter.data.indices) {
                    if (wanAndroidAdapter.data[index].id == it.id) {
                        wanAndroidAdapter.data[index].collect = it.collect
                        wanAndroidAdapter.notifyItemChanged(index)
                        break
                    }
                }
            }
        })

        appViewModel.run {
            //监听账户信息是否改变 有值时(登录)将相关的数据设置为已收藏，为空时(退出登录)，将已收藏的数据变为未收藏
            userinfo.observe(viewLifecycleOwner, Observer {
                if (it != null) {
                    it.collectIds.forEach { id ->
                        for (item in wanAndroidAdapter.data) {
                            if (id.toInt() == item.id) {
                                item.collect = true
                                break
                            }
                        }
                    }
                } else {
                    for (item in wanAndroidAdapter.data) {
                        item.collect = false
                    }
                }
                wanAndroidAdapter.notifyDataSetChanged()
            })
            //监听全局的主题颜色改变
            appColor.observe(viewLifecycleOwner, Observer {
                setUiTheme(it, toolbar, floatBtn, swipeRefresh, loadSir, footView,headerWanAndroidBinding.viewLine,headerWanAndroidBinding.rb1,headerWanAndroidBinding.rb2)
            })
            //监听全局的列表动画改编
            appAnimation.observe(viewLifecycleOwner, Observer {
                wanAndroidAdapter.setAdapterAnimation(it)
            })
            //监听全局的收藏信息 收藏的Id跟本列表的数据id匹配则需要更新
            eventViewModel.collectEvent.observe(viewLifecycleOwner, Observer {
                for (index in wanAndroidAdapter.data.indices) {
                    if (wanAndroidAdapter.data[index].id == it.id) {
                        wanAndroidAdapter.data[index].collect = it.collect
                        wanAndroidAdapter.notifyItemChanged(index)
                        break
                    }
                }
            })
        }
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

}