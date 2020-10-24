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
import com.uiuang.cloudknowledge.databinding.FragmentListBinding
import com.uiuang.cloudknowledge.ext.*
import com.uiuang.cloudknowledge.ui.adapter.wan.ShareArticleAdapter
import com.uiuang.cloudknowledge.ui.fragment.web.WebViewFragment
import com.uiuang.cloudknowledge.viewmodel.request.RequestArticleViewModel
import com.uiuang.cloudknowledge.viewmodel.state.ArticleViewModel
import com.uiuang.cloudknowledge.weight.recyclerview.SpaceItemDecoration
import com.uiuang.mvvm.ext.nav
import com.uiuang.mvvm.ext.navigateAction
import com.uiuang.mvvm.util.dp2px
import com.yanzhenjie.recyclerview.SwipeRecyclerView
import kotlinx.android.synthetic.main.include_list.*
import kotlinx.android.synthetic.main.include_recyclerview.*
import kotlinx.android.synthetic.main.include_toolbar.*


class ArticleFragment : BaseFragment<ArticleViewModel,FragmentListBinding>() {
    //适配器
    private val articleAdapter: ShareArticleAdapter by lazy { ShareArticleAdapter(arrayListOf()) }

    //界面状态管理者
    private lateinit var loadSir: LoadService<Any>

    //记得要写泛型，虽然在 by lazy中 提示不用写，但是你不写就会报错
    private val requestViewModel: RequestArticleViewModel by viewModels()




    override fun layoutId(): Int =R.layout.fragment_list

    override fun initView(savedInstanceState: Bundle?) {
        toolbar.run {
            initClose("我分享的文章") {
                nav().navigateUp()
            }
            inflateMenu(R.menu.todo_menu)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.todo_add -> {
                        nav().navigateAction(R.id.action_articleFragment_to_shareArticleFragment)
                    }
                }
                true
            }
        }
        //状态页配置
        loadSir = loadServiceInit(swipeRefresh) {
            //点击重试时触发的操作
            loadSir.showLoading()
            requestViewModel.getShareData(true)
        }

        //初始化recyclerView
        recyclerView.init(LinearLayoutManager(context), articleAdapter).let {
            it.addItemDecoration(SpaceItemDecoration(0, requireContext().dp2px(8)))
            it.initFooter(SwipeRecyclerView.LoadMoreListener {
                //触发加载更多时请求数据
                requestViewModel.getShareData(false)
            })
            //初始化FloatingActionButton
            it.initFloatBtn(floatBtn)
        }
        //初始化 SwipeRefreshLayout
        swipeRefresh.init {
            //触发刷新监听时请求数据
            requestViewModel.getShareData(true)
        }

        articleAdapter.run {
            setOnItemClickListener { _, view, position ->
                var articlesBean = articleAdapter.data[position]
                WebViewFragment.openDetail(view,articlesBean.link,articlesBean.title)
//                nav().navigateAction(R.id.action_global_webViewFragment, Bundle().apply {
//                    putParcelable("ariticleData", articleAdapter.data[position])
//                })
            }
            addChildClickViewIds(R.id.item_share_del)
            setOnItemChildClickListener { _, view, position ->
                when (view.id) {
                    R.id.item_share_del -> {
                        showMessage("确定删除该文章吗？", positiveButtonText = "删除", positiveAction = {
                            requestViewModel.deleteShareData(
                                articleAdapter.data[position].id,
                                position
                            )
                        }, negativeButtonText = "取消")
                    }
                }
            }
        }
    }


    override fun lazyLoadData() {
        //设置界面 加载中
        loadSir.showLoading()
        requestViewModel.getShareData(true)
    }

    override fun createObserver() {
        requestViewModel.shareDataState.observe(viewLifecycleOwner, Observer {
            //设值 新写了个拓展函数，搞死了这个恶心的重复代码
            loadListData(it, articleAdapter, loadSir, recyclerView,swipeRefresh)
        })
        requestViewModel.delDataState.observe(viewLifecycleOwner, Observer {
            if (it.isSuccess) {
                //删除成功 如果是删除的最后一个了，那么直接把界面设置为空布局
                if (articleAdapter.data.size == 1) {
                    loadSir.showEmpty()
                }
                articleAdapter.remove(it.data!!)
            } else {
                //删除失败，提示
                showMessage(it.errorMsg)
            }
        })
        eventViewModel.shareArticleEvent.observe(viewLifecycleOwner, Observer {
            if (articleAdapter.data.size == 0) {
                //界面没有数据时，变为加载中 增强一丢丢体验
                loadSir.showLoading()
            } else {
                //有数据时，swipeRefresh 显示刷新状态
                swipeRefresh.isRefreshing = true
            }
            requestViewModel.getShareData(true)
        })
    }

}