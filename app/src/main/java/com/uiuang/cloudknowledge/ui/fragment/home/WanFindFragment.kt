package com.uiuang.cloudknowledge.ui.fragment.home

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.kingja.loadsir.core.LoadService
import com.uiuang.cloudknowledge.R
import com.uiuang.cloudknowledge.app.base.BaseFragment
import com.uiuang.cloudknowledge.bean.wan.ArticlesBean
import com.uiuang.cloudknowledge.bean.event.CollectBus
import com.uiuang.cloudknowledge.bean.wan.TabBean
import com.uiuang.cloudknowledge.bean.wan.WebBean
import com.uiuang.cloudknowledge.data.enums.CollectType
import com.uiuang.cloudknowledge.databinding.FragmentWanFindBinding
import com.uiuang.cloudknowledge.ext.*
import com.uiuang.cloudknowledge.ui.adapter.wan.WanAndroidAdapter
import com.uiuang.cloudknowledge.ui.adapter.wan.WxArticleAdapter
import com.uiuang.cloudknowledge.utils.DataUtil
import com.uiuang.cloudknowledge.utils.SettingUtil
import com.uiuang.cloudknowledge.utils.showToastLong
import com.uiuang.cloudknowledge.utils.toast
import com.uiuang.cloudknowledge.viewmodel.request.RequestCollectViewModel
import com.uiuang.cloudknowledge.viewmodel.request.RequestWanFindViewModel
import com.uiuang.cloudknowledge.viewmodel.state.WanFindViewModel
import com.uiuang.cloudknowledge.weight.recyclerview.DefineLoadMoreView
import com.uiuang.mvvm.ext.nav
import com.uiuang.mvvm.ext.navigateAction
import com.yanzhenjie.recyclerview.SwipeRecyclerView
import kotlinx.android.synthetic.main.fragment_sister.swipeRefresh
import kotlinx.android.synthetic.main.fragment_wan_find.*
import kotlinx.android.synthetic.main.fragment_wan_find.recyclerView
import kotlinx.android.synthetic.main.include_list.*
import kotlinx.android.synthetic.main.include_toolbar.*


class WanFindFragment : BaseFragment<WanFindViewModel, FragmentWanFindBinding>() {
    //选中id
    private var wxArticleId: Int = 0

    //选中的tabBean
    private var currentPosition: Int = -1

    //界面状态管理者
    private lateinit var loadSir: LoadService<Any>

    //recyclerview的底部加载view 因为要在首页动态改变他的颜色，所以加了他这个字段
    private lateinit var footView: DefineLoadMoreView

    private var isFirst = true
    //收藏viewModel
    private val requestCollectViewModel: RequestCollectViewModel by viewModels()
    //请求ViewModel
    private val requestWanFindViewModel: RequestWanFindViewModel by viewModels()

    //微信公众号的tab列表adapter
    private val wxArticleAdapter: WxArticleAdapter by lazy {
        WxArticleAdapter(arrayListOf())
    }
    private val wanAndroidAdapter: WanAndroidAdapter by lazy {
        WanAndroidAdapter(arrayListOf())
    }

    private val layoutManager: LinearLayoutManager by lazy {
        LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
    }

    companion object {
        @JvmStatic
        fun newInstance() = WanFindFragment()
    }

    override fun layoutId(): Int = R.layout.fragment_wan_find

    override fun initView(savedInstanceState: Bundle?) {
        //状态页配置
        loadSir = loadServiceInit(swipeRefresh) {
            //点击重试时触发的操作
            lazyLoadData()
        }
        rv_wxarticle.init(
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false),
            wxArticleAdapter
        )
        wanAndroidAdapter.setNoShowChapterName()
        recyclerView.init(
            layoutManager,
            wanAndroidAdapter
        ).let {
            it.addItemDecoration(
                DividerItemDecoration(
                    requireActivity(),
                    DividerItemDecoration.VERTICAL
                )
            )
            footView = it.initFooter(SwipeRecyclerView.LoadMoreListener {
                requestWanFindViewModel.getWxArticleDetail(false, wxArticleId)
            })
        }
        //初始化 SwipeRefreshLayout
        swipeRefresh.init {
            //触发刷新监听时请求数据
            requestWanFindViewModel.getWxArticleDetail(true, wxArticleId)
        }
        wxArticleAdapter.setOnItemClickListener { _, _, position ->
            position.selectItem()
        }
        wanAndroidAdapter.run {
            setOnItemClickListener { _, _, position ->
                val item: ArticlesBean = data[position]
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
        }
    }

    override fun lazyLoadData() {
        loadSir.showLoading()
        isFirst = false
        val findPosition = SettingUtil.getFindPosition()
        if (findPosition == -1 ||
            !requestWanFindViewModel.handleCustomData(
                DataUtil.getTreeData(requireActivity()),
                findPosition
            )
        ) {
            requestWanFindViewModel.getWxArticle()
        }
    }

    override fun createObserver() {
        requestWanFindViewModel.dataTitle.observe(viewLifecycleOwner, Observer {
            wxArticleAdapter.setList(it)
            0.selectItem()
        })
        requestWanFindViewModel.articlesBean.observe(viewLifecycleOwner, Observer {
            loadListData(it, wanAndroidAdapter, loadSir, recyclerView, swipeRefresh)
            layoutManager.scrollToPosition(0)
        })
        appViewModel.findPosition.observe(viewLifecycleOwner, Observer {
            if (it != -1) {
                if (!isFirst) {
                    val tabBeanList: MutableList<TabBean>? = DataUtil.getTreeData(activity)
                    if (requestWanFindViewModel.handleCustomData(tabBeanList, it)) {
                        "发现页内容已改为\" ${tabBeanList?.get(it)?.name.toString()}\"".toast()
                    }
                } else {
                    "发现页内容已更改，请打开查看~".showToastLong()
                }
            }
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
                setUiTheme(it, toolbar, floatBtn, swipeRefresh, loadSir, footView)
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

    private fun Int.selectItem() {

        if (this < wxArticleAdapter.data.size) {
            wxArticleId = wxArticleAdapter.data[this].id
            wxArticleAdapter.setId(wxArticleId)
            wxArticleAdapter.notifyItemChanged(currentPosition)
            wxArticleAdapter.notifyItemChanged(this)
            currentPosition = this
            requestWanFindViewModel.getWxArticleDetail(
                true,
                wxArticleId
            )
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