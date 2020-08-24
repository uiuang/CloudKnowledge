package com.uiuang.cloudknowledge.ui.fragment.home

import android.os.Bundle
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.kingja.loadsir.core.LoadService
import com.uiuang.cloudknowledge.R
import com.uiuang.cloudknowledge.app.base.BaseFragment
import com.uiuang.cloudknowledge.databinding.FragmentWanHomeBinding
import com.uiuang.cloudknowledge.databinding.HeaderWanAndroidBinding
import com.uiuang.cloudknowledge.ext.init
import com.uiuang.cloudknowledge.ext.loadServiceInit
import com.uiuang.cloudknowledge.ext.showError
import com.uiuang.cloudknowledge.ext.showLoading
import com.uiuang.cloudknowledge.ui.adapter.wan.WanAndroidAdapter
import com.uiuang.cloudknowledge.ui.adapter.wan.WanBannerAdapter
import com.uiuang.cloudknowledge.viewmodel.request.RequestWanHomeViewModel
import com.uiuang.cloudknowledge.viewmodel.state.HomeViewModel
import com.uiuang.cloudknowledge.weight.recyclerview.DefineLoadMoreView
import com.uiuang.mvvm.util.dp2px
import com.uiuang.mvvm.util.screenWidth
import com.youth.banner.indicator.CircleIndicator
import kotlinx.android.synthetic.main.fragment_sister.*
import kotlinx.android.synthetic.main.fragment_sister.recyclerView
import kotlinx.android.synthetic.main.fragment_sister.swipeRefresh
import kotlinx.android.synthetic.main.fragment_wan_home.*


class WanHomeFragment : BaseFragment<HomeViewModel, FragmentWanHomeBinding>() {
    //界面状态管理者
    private lateinit var loadsir: LoadService<Any>

    private val requestWanHomeViewModel: RequestWanHomeViewModel by viewModels()

    //recyclerview的底部加载view 因为要在首页动态改变他的颜色，所以加了他这个字段
    private lateinit var footView: DefineLoadMoreView

    //    private var inflate = LayoutInflater.from(requireContext())
    private var headerWanAndroidBinding: HeaderWanAndroidBinding? = null
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
        loadsir = loadServiceInit(swipeRefresh) {
            //点击重试时触发的操作
            loadsir.showLoading()
            requestWanHomeViewModel.getWanAndroidBanner()
        }

        //初始化recyclerView
        headerWanAndroidBinding = DataBindingUtil.inflate<HeaderWanAndroidBinding>(
            layoutInflater,
            R.layout.header_wan_android,
            null,
            false
        )
        val px = context?.dp2px(100)
        val width: Int = context?.screenWidth!!.minus(px!!)
        val height: Float = width / 1.8f
        val lp = ConstraintLayout.LayoutParams(context?.screenWidth!!.toInt(), height.toInt())
        headerWanAndroidBinding!!.banner.layoutParams = lp
        headerWanAndroidBinding!!.radioGroup.visibility = View.VISIBLE
        headerWanAndroidBinding!!.rb1.run {
            setOnCheckedChangeListener { buttonView, isChecked -> {

            } }
        }
        headerWanAndroidBinding!!.rb2.run {
            setOnCheckedChangeListener { buttonView, isChecked -> {

            } }
        }
        recyclerView.addHeaderView(headerWanAndroidBinding?.root)
        recyclerView.init(
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false),
            wanAndroidAdapter
        ).let {
//            it.addItemDecoration(GridSpaceItemDecoration(12))
////            footView = it.initFooter(SwipeRecyclerView.LoadMoreListener {
////                requestSisterViewModel.getPlazaData(false)
////            })
//            //初始化FloatingActionButton
//            it.initFloatBtn(floatBtn)
        }
        //初始化 SwipeRefreshLayout
        swipeRefresh.init {
            //触发刷新监听时请求数据
            requestWanHomeViewModel.getWanAndroidBanner()
        }

        headerWanAndroidBinding!!.banner.run {
            addBannerLifecycleObserver(this@WanHomeFragment)//添加生命周期观察者
            adapter = wanBannerAdapter
            indicator = CircleIndicator(requireActivity())
        }
    }

    override fun createObserver() {
        super.createObserver()
        requestWanHomeViewModel.wanAndroidBannerBean.observe(viewLifecycleOwner, Observer {
            //设值 新写了个拓展函数，搞死了这个恶心的重复代码
            var listData = it.listData
            when (it.isSuccess) {
                true -> {
                    loadsir.showSuccess()
                    headerWanAndroidBinding!!.banner.setDatas(listData)
//                    wanBannerAdapter.setDatas(listData)
//                    wanBannerAdapter.notifyDataSetChanged()
                }
                else -> {
                    loadsir.showError()
                }
            }
        })
    }

    override fun lazyLoadData() {
        //设置界面 加载中
        loadsir.showLoading()
        requestWanHomeViewModel.getWanAndroidBanner()
    }

    private fun refresh(isChecked: Boolean, isArticle: Boolean) {
        if (isChecked) {
           swipeRefresh.isRefreshing = true
//            viewModel.setPage(0)
//            mAdapter.setNoImage(isArticle)
            if (isArticle) {
               requestWanHomeViewModel. getHomeArticleList(true,null)
            } else {
               requestWanHomeViewModel. getHomeProjectList(true)
            }
        }
    }
}