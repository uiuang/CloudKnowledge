package com.uiuang.cloudknowledge.ui.fragment.mine

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.kingja.loadsir.core.LoadService
import com.uiuang.cloudknowledge.R
import com.uiuang.cloudknowledge.app.base.BaseFragment
import com.uiuang.cloudknowledge.databinding.IncludeListBinding
import com.uiuang.cloudknowledge.ext.*
import com.uiuang.cloudknowledge.ui.adapter.wan.CollectUrlAdapter
import com.uiuang.cloudknowledge.viewmodel.request.RequestCollectViewModel
import com.uiuang.cloudknowledge.viewmodel.state.CollectViewModel
import com.uiuang.cloudknowledge.weight.recyclerview.SpaceItemDecoration
import com.uiuang.mvvm.base.appContext
import com.uiuang.mvvm.util.dp2px
import kotlinx.android.synthetic.main.include_list.*
import kotlinx.android.synthetic.main.include_recyclerview.*

/**
 * 收藏url列表
 */
class CollectUrlFragment : BaseFragment<CollectViewModel, IncludeListBinding>() {


    //界面状态管理者
    private lateinit var loadsir: LoadService<Any>

    private val articleAdapter: CollectUrlAdapter by lazy { CollectUrlAdapter(arrayListOf()) }
    private val requestCollectViewModel: RequestCollectViewModel by viewModels()
    override fun layoutId() = R.layout.include_list

    override fun initView(savedInstanceState: Bundle?) {
        //状态页配置
        loadsir = loadServiceInit(swipeRefresh) {
            //点击重试时触发的操作
            loadsir.showLoading()
            requestCollectViewModel.getCollectUrlData()

        }
        //初始化recyclerView
        recyclerView.init(LinearLayoutManager(context), articleAdapter).let {
            it.addItemDecoration(SpaceItemDecoration(0, appContext.dp2px(8)))
            //初始化FloatingActionButton
            it.initFloatBtn(floatBtn)
        }
        //初始化 SwipeRefreshLayout
        swipeRefresh.init {
            //触发刷新监听时请求数据
            requestCollectViewModel.getCollectUrlData()
        }
        articleAdapter.run {
            setCollectClick { item, v, position ->
                if (v.isChecked) {
                    requestCollectViewModel.unCollectUrl(item.id)
                } else {
                    requestCollectViewModel.collectUrl(item.name, item.link)
                }
            }
            setOnItemClickListener { _, view, position ->
//                nav().navigateAction(R.id.action_to_webFragment, Bundle().apply {
//                    putParcelable("collectUrl", articleAdapter.data[position])
//                })
            }
        }
    }

    override fun lazyLoadData() {
        //设置界面 加载中
        loadsir.showLoading()
        requestCollectViewModel.getCollectUrlData()
    }

    override fun createObserver() {
        requestCollectViewModel.urlDataState.observe(viewLifecycleOwner, Observer {
            swipeRefresh.isRefreshing = false
            recyclerView.loadMoreFinish(it.isEmpty, it.hasMore)
            if (it.isSuccess) {
                //成功
                when {
                    //第一页并没有数据 显示空布局界面
                    it.isEmpty -> {
                        loadsir.showEmpty()
                    }
                    else -> {
                        loadsir.showSuccess()
                        articleAdapter.setList(it.listData)
                    }
                }
            } else {
                //失败
                loadsir.showError(it.errMessage)
            }
        })
        requestCollectViewModel.collectUrlUiState.observe(viewLifecycleOwner, Observer {
            if (it.isSuccess) {
                for (index in articleAdapter.data.indices) {
                    if (articleAdapter.data[index].id == it.id) {
                        articleAdapter.remove(index)
                        if (articleAdapter.data.size == 0) {
                            loadsir.showEmpty()
                        }
                        return@Observer
                    }
                }
            } else {
                showMessage(it.errorMsg)
                for (index in articleAdapter.data.indices) {
                    if (articleAdapter.data[index].id == it.id) {
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
                    if (articleAdapter.data[index].id == it.id) {
                        articleAdapter.data.removeAt(index)
                        articleAdapter.notifyItemChanged(index)
                        if (articleAdapter.data.size == 0) {
                            loadsir.showEmpty()
                        }
                        return@Observer
                    }
                }
                requestCollectViewModel.getCollectUrlData()
            })
        }
    }


}