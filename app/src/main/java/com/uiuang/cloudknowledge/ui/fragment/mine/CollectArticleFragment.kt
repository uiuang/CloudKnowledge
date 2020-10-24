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
import com.uiuang.cloudknowledge.bean.CollectBus
import com.uiuang.cloudknowledge.databinding.IncludeListBinding
import com.uiuang.cloudknowledge.ext.*
import com.uiuang.cloudknowledge.ui.adapter.wan.CollectAdapter
import com.uiuang.cloudknowledge.viewmodel.request.RequestArticleViewModel
import com.uiuang.cloudknowledge.viewmodel.request.RequestCollectViewModel
import com.uiuang.cloudknowledge.viewmodel.state.CollectViewModel
import com.uiuang.cloudknowledge.weight.recyclerview.SpaceItemDecoration
import com.uiuang.mvvm.base.appContext
import com.uiuang.mvvm.ext.nav
import com.uiuang.mvvm.ext.navigateAction
import com.uiuang.mvvm.util.dp2px
import com.yanzhenjie.recyclerview.SwipeRecyclerView
import kotlinx.android.synthetic.main.include_list.*
import kotlinx.android.synthetic.main.include_recyclerview.*

/**
 * 收藏文章列表
 */
class CollectArticleFragment : BaseFragment<CollectViewModel, IncludeListBinding>() {

    //界面状态管理者
    private lateinit var loadSir: LoadService<Any>

    private val articleAdapter: CollectAdapter by lazy { CollectAdapter(arrayListOf()) }
    private val requestCollectViewModel: RequestCollectViewModel by viewModels()


    override fun layoutId(): Int = R.layout.include_list

    override fun initView(savedInstanceState: Bundle?) {
        //状态页配置
        loadSir = loadServiceInit(swipeRefresh) {
            //点击重试时触发的操作
            loadSir.showLoading()
            requestCollectViewModel.getCollectArticleData(true)
        }
        //初始化recyclerView
        recyclerView.init(LinearLayoutManager(context), articleAdapter).let {
            it.addItemDecoration(SpaceItemDecoration(0, appContext.dp2px(8)))
            it.initFooter(SwipeRecyclerView.LoadMoreListener {
                requestCollectViewModel.getCollectArticleData(false)
            })
            //初始化FloatingActionButton
            it.initFloatBtn(floatBtn)
        }
        //初始化 SwipeRefreshLayout
        swipeRefresh.init {
            //触发刷新监听时请求数据
            requestCollectViewModel.getCollectArticleData(true)
        }
        articleAdapter.run {
            setCollectClick { item, v, position ->
                if (v.isChecked) {
                    requestCollectViewModel.unCollect(item.originId)
                } else {
                    requestCollectViewModel.collect(item.originId)
                }
            }
            setOnItemClickListener { _, view, position ->
//                nav().navigateAction(R.id.action_global_loginFragment, Bundle().apply {
//                    putParcelable("collect", articleAdapter.data[position])
//                })
            }
        }
    }
    override fun lazyLoadData() {
        loadSir.showLoading()
        requestCollectViewModel.getCollectArticleData(true)
    }

    override fun createObserver() {
        requestCollectViewModel.articleDataState.observe(viewLifecycleOwner, Observer {
            //设值 新写了个拓展函数，搞死了这个恶心的重复代码
            loadListData(it, articleAdapter, loadSir, recyclerView,swipeRefresh)
        })
        requestCollectViewModel.collectUiState.observe(viewLifecycleOwner, Observer {
            if (it.isSuccess) {
                //收藏或取消收藏操作成功，发送全局收藏消息
                eventViewModel.collectEvent.value = CollectBus(it.id, it.collect)
            } else {
                showMessage(it.errorMsg)
                for (index in articleAdapter.data.indices) {
                    if (articleAdapter.data[index].originId == it.id) {
                        articleAdapter.notifyItemChanged(index)
                        break
                    }
                }
            }
        })
        eventViewModel.run {
            //监听全局的收藏信息 收藏的Id跟本列表的数据id匹配则 需要删除他 否则则请求最新收藏数据
            collectEvent.observe(viewLifecycleOwner, Observer {
                for (index in articleAdapter.data.indices) {
                    if (articleAdapter.data[index].originId == it.id) {
                        articleAdapter.remove(index)
                        if (articleAdapter.data.size == 0) {
                            loadSir.showEmpty()
                        }
                        return@Observer
                    }
                }
                requestCollectViewModel.getCollectArticleData(true)
            })
        }
    }

}