package com.uiuang.cloudknowledge.ui.fragment.home

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.kingja.loadsir.core.LoadService
import com.uiuang.cloudknowledge.R
import com.uiuang.cloudknowledge.app.base.BaseFragment
import com.uiuang.cloudknowledge.app.http.Constants.ID
import com.uiuang.cloudknowledge.app.http.Constants.IS_LOAD
import com.uiuang.cloudknowledge.app.http.Constants.NAME
import com.uiuang.cloudknowledge.app.http.Constants.WEB_ISTITLEFIX
import com.uiuang.cloudknowledge.app.http.Constants.WEB_TITLE
import com.uiuang.cloudknowledge.app.http.Constants.WEB_URL
import com.uiuang.cloudknowledge.bean.event.CollectBus
import com.uiuang.cloudknowledge.bean.wan.WebBean
import com.uiuang.cloudknowledge.data.enums.CollectType
import com.uiuang.cloudknowledge.databinding.FragmentCategoryDetailBinding
import com.uiuang.cloudknowledge.ext.*
import com.uiuang.cloudknowledge.ui.adapter.wan.WanAndroidAdapter
import com.uiuang.cloudknowledge.viewmodel.request.RequestCollectViewModel
import com.uiuang.cloudknowledge.viewmodel.request.RequestWanHomeViewModel
import com.uiuang.cloudknowledge.viewmodel.state.HomeViewModel
import com.uiuang.cloudknowledge.weight.recyclerview.DefineLoadMoreView
import com.uiuang.mvvm.ext.nav
import com.uiuang.mvvm.ext.navigateAction
import com.yanzhenjie.recyclerview.SwipeRecyclerView
import kotlinx.android.synthetic.main.fragment_category_article.recyclerView


class CategoryArticleFragment : BaseFragment<HomeViewModel, FragmentCategoryDetailBinding>() {
    private var categoryId: Int = 0
    private var categoryName: String? = null
    private var isLoad: Boolean = false

    //界面状态管理者
    private lateinit var loadSir: LoadService<Any>
    private lateinit var footView: DefineLoadMoreView

    //收藏viewModel
    private val requestCollectViewModel: RequestCollectViewModel by viewModels()

    private val requestWanHomeViewModel: RequestWanHomeViewModel by viewModels()
    private val wanAndroidAdapter: WanAndroidAdapter by lazy {
        WanAndroidAdapter(arrayListOf())
    }

    companion object {
        @JvmStatic
        fun newInstance(categoryId: Int, categoryName: String, isLoad: Boolean) =
            CategoryArticleFragment().apply {
                arguments = Bundle().apply {
                    putInt(ID, categoryId)
                    putString(NAME, categoryName)
                    putBoolean(IS_LOAD, isLoad)
                }
            }
    }

    override fun layoutId(): Int = R.layout.fragment_category_article

    override fun initView(savedInstanceState: Bundle?) {
        arguments?.let {
            categoryId = it.getInt(ID)
            categoryName = it.getString(NAME)
            isLoad = it.getBoolean(IS_LOAD)
        }
        //状态页配置
        loadSir = loadServiceInit(recyclerView) {
            //点击重试时触发的操作
            loadSir.showLoading()
            requestWanHomeViewModel.getHomeArticleList(true, categoryId)
        }
        wanAndroidAdapter.setNoShowChapterName()
        recyclerView.let {
            it.init(LinearLayoutManager(requireActivity()), wanAndroidAdapter)
            it.addItemDecoration(
                DividerItemDecoration(
                    requireActivity(),
                    DividerItemDecoration.VERTICAL
                )
            )
            footView = it.initFooter(SwipeRecyclerView.LoadMoreListener {
                requestWanHomeViewModel.getHomeArticleList(false, cid = categoryId)
            })
        }
        wanAndroidAdapter.run {
            setOnItemClickListener { _, _, position ->
                val item = getItem(position)
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
            setCollectClick { item, v, _ ->
                if (v.isChecked) {
                    requestCollectViewModel.unCollect(item.id)
                } else {
                    requestCollectViewModel.collect(item.id)
                }
            }
        }
    }

    override fun createObserver() {
        requestWanHomeViewModel.articlesBean.observe(viewLifecycleOwner, Observer {
            loadListData(it, wanAndroidAdapter, loadSir, recyclerView)
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
        loadSir.showLoading()
        requestWanHomeViewModel.getHomeArticleList(true, cid = categoryId)

    }

    private fun openDetail(url: String?, title: String?, isTitleFix: Boolean = false) {
        if (!url.isNullOrEmpty()) {
            nav().navigateAction(R.id.action_global_webViewFragment, Bundle().apply {
                putString(WEB_URL, url)
                putBoolean(WEB_ISTITLEFIX, isTitleFix)
                putString(WEB_TITLE, if (title.isNullOrEmpty()) url else title)
            })
        }
    }
}
