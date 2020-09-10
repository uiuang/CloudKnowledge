package com.uiuang.cloudknowledge.ui.fragment.gank

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.kingja.loadsir.core.LoadService
import com.uiuang.cloudknowledge.R
import com.uiuang.cloudknowledge.app.base.BaseFragment
import com.uiuang.cloudknowledge.databinding.FragmentGankBinding
import com.uiuang.cloudknowledge.databinding.FragmentGankHomeBinding
import com.uiuang.cloudknowledge.databinding.HeaderItemEverydayBinding
import com.uiuang.cloudknowledge.ext.init
import com.uiuang.cloudknowledge.ext.initFooter
import com.uiuang.cloudknowledge.ext.loadServiceInit
import com.uiuang.cloudknowledge.ext.showLoading
import com.uiuang.cloudknowledge.ui.adapter.gank.GankBannerAdapter
import com.uiuang.cloudknowledge.ui.adapter.wan.WanAndroidAdapter
import com.uiuang.cloudknowledge.ui.adapter.wan.WanBannerAdapter
import com.uiuang.cloudknowledge.viewmodel.request.RequestGankHomeViewModel
import com.uiuang.cloudknowledge.viewmodel.request.RequestWanHomeViewModel
import com.uiuang.cloudknowledge.viewmodel.state.HomeViewModel
import com.uiuang.cloudknowledge.weight.recyclerview.DefineLoadMoreView
import com.yanzhenjie.recyclerview.SwipeRecyclerView
import kotlinx.android.synthetic.main.fragment_category_article.*
import kotlinx.android.synthetic.main.fragment_category_article.recyclerView
import kotlinx.android.synthetic.main.fragment_gank_home.*


class GankHomeFragment : BaseFragment<HomeViewModel,FragmentGankHomeBinding>() {
    //界面状态管理者
    private lateinit var loadSir: LoadService<Any>
    private lateinit var footView: DefineLoadMoreView
    private lateinit var headerItemEverydayBinding: HeaderItemEverydayBinding
    private val requestGankHomeViewModel: RequestGankHomeViewModel by viewModels()

    private val wanAndroidAdapter: WanAndroidAdapter by lazy {
        WanAndroidAdapter(arrayListOf())
    }

    private val gankBannerAdapter: GankBannerAdapter by lazy {
        GankBannerAdapter(arrayListOf())
    }

    companion object {
        @JvmStatic
        fun newInstance() = GankHomeFragment()
    }

    override fun layoutId(): Int =R.layout.fragment_gank_home

    override fun initView(savedInstanceState: Bundle?) {
        //状态页配置
        loadSir = loadServiceInit(recyclerView) {
            //点击重试时触发的操作
            loadSir.showLoading()
            requestGankHomeViewModel.getData()
        }
        headerItemEverydayBinding = DataBindingUtil.inflate<HeaderItemEverydayBinding>(
            layoutInflater,
            R.layout.header_item_everyday,
            null,
            false
        )
        headerItemEverydayBinding.includeEveryday.click = ProxyClick()
        recyclerView.addHeaderView(headerItemEverydayBinding.root)
        recyclerView.let {
            it.init(LinearLayoutManager(requireActivity()), wanAndroidAdapter)
            it.addItemDecoration(
                DividerItemDecoration(
                    requireActivity(),
                    DividerItemDecoration.VERTICAL
                )
            )
            footView = it.initFooter(SwipeRecyclerView.LoadMoreListener {
            })
        }

    }

    override fun lazyLoadData() {
        loadSir.showLoading()
        requestGankHomeViewModel.getData()

    }

    inner class ProxyClick {
        /** 登录 */
        fun idleReading() {
        }

        fun wanAndroid() {

        }

        fun movieHot() {

        }
        fun everyDay() {

        }

    }
}