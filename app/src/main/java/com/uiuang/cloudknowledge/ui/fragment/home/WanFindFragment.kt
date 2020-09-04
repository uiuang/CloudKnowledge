package com.uiuang.cloudknowledge.ui.fragment.home

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.kingja.loadsir.core.LoadService
import com.uiuang.cloudknowledge.R
import com.uiuang.cloudknowledge.app.base.BaseFragment
import com.uiuang.cloudknowledge.databinding.FragmentWanFindBinding
import com.uiuang.cloudknowledge.ext.*
import com.uiuang.cloudknowledge.ui.adapter.wan.WanAndroidAdapter
import com.uiuang.cloudknowledge.ui.adapter.wan.WxArticleAdapter
import com.uiuang.cloudknowledge.utils.DataUtil
import com.uiuang.cloudknowledge.utils.SettingUtil
import com.uiuang.cloudknowledge.viewmodel.request.RequestWanFindViewModel
import com.uiuang.cloudknowledge.viewmodel.state.HomeViewModel
import com.uiuang.cloudknowledge.weight.recyclerview.DefineLoadMoreView
import com.yanzhenjie.recyclerview.SwipeRecyclerView
import kotlinx.android.synthetic.main.fragment_sister.*
import kotlinx.android.synthetic.main.fragment_sister.swipeRefresh
import kotlinx.android.synthetic.main.fragment_wan_find.*
import kotlinx.android.synthetic.main.fragment_wan_find.recyclerView


class WanFindFragment : BaseFragment<HomeViewModel, FragmentWanFindBinding>() {
    //选中id
    private var wxArticleId: Int = 0
    //选中的tabBean
    private var currentPosition:Int =0

    //界面状态管理者
    private lateinit var loadSir: LoadService<Any>

    //recyclerview的底部加载view 因为要在首页动态改变他的颜色，所以加了他这个字段
    private lateinit var footView: DefineLoadMoreView

    //请求ViewModel
    private val requestWanFindViewModel: RequestWanFindViewModel by viewModels()
    //微信公众号的tab列表adapter
    private val wxArticleAdapter: WxArticleAdapter by lazy {
        WxArticleAdapter(arrayListOf())
    }
    private val wanAndroidAdapter: WanAndroidAdapter by lazy {
        WanAndroidAdapter(arrayListOf())
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
            loadSir.showLoading()
            lazyLoadData()
        }
        rv_wxarticle.init(
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false),
            wxArticleAdapter
        )
        recyclerView.init(
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false),
            wanAndroidAdapter
        ).let{
            it.addItemDecoration(
                DividerItemDecoration(
                    requireActivity(),
                    DividerItemDecoration.VERTICAL
                )
            )
            footView = it.initFooter(SwipeRecyclerView.LoadMoreListener {
                requestWanFindViewModel.getWxArticleDetail(false,wxArticleId)
            })
        }
        //初始化 SwipeRefreshLayout
        swipeRefresh.init {
            //触发刷新监听时请求数据
            requestWanFindViewModel.getWxArticleDetail(true,wxArticleId)
//            if (headerWanAndroidBinding.rb1.isChecked) {
//                refresh(isChecked = true, isArticle = true, isRefresh = true)
//            } else {
//                refresh(isChecked = true, isArticle = false, isRefresh = true)
//            }
        }
        wxArticleAdapter.setOnItemClickListener { _, _, position ->
            position.selectItem()
        }
    }

    override fun lazyLoadData() {
        val findPosition = SettingUtil.getFindPosition()
        if (findPosition == -1 ||
            !requestWanFindViewModel.handleCustomData(DataUtil.getTreeData(requireActivity()), findPosition)) {
            requestWanFindViewModel.getWxArticle()
        }
    }

    override fun createObserver() {
        requestWanFindViewModel.dataTitle.observe(viewLifecycleOwner, Observer {
            wxArticleAdapter.setNewInstance(it)
            0.selectItem()
        })
        requestWanFindViewModel.homeListBean.observe(viewLifecycleOwner, Observer {
            loadListData(it, wanAndroidAdapter, loadSir, recyclerView, swipeRefresh)
        })
    }

    private fun Int.selectItem() {
        if (this < wxArticleAdapter.data.size) {
            wxArticleId = wxArticleAdapter.data[this].id
            wxArticleAdapter.setId(wxArticleId)
            swipeRefresh.isRefreshing = true
            requestWanFindViewModel.getWxArticleDetail( true,
                wxArticleId
            )
        }
        wxArticleAdapter.notifyItemChanged(this)
        currentPosition = this
    }
}