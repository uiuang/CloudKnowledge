package com.uiuang.cloudknowledge.ui.fragment.gank

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.app.NotificationCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.kingja.loadsir.core.LoadService
import com.uiuang.cloudknowledge.R
import com.uiuang.cloudknowledge.app.base.BaseFragment
import com.uiuang.cloudknowledge.databinding.FragmentAndroidBinding
import com.uiuang.cloudknowledge.ext.*
import com.uiuang.cloudknowledge.ui.adapter.gank.GankAndroidAdapter
import com.uiuang.cloudknowledge.utils.CacheUtil
import com.uiuang.cloudknowledge.viewmodel.request.RequestGankViewModel
import com.uiuang.cloudknowledge.viewmodel.state.HomeViewModel
import com.uiuang.cloudknowledge.weight.recyclerview.DefineLoadMoreView
import com.yanzhenjie.recyclerview.SwipeRecyclerView
import kotlinx.android.synthetic.main.fragment_sister.*


class CustomFragment : BaseFragment<HomeViewModel, FragmentAndroidBinding>() {

    private var gankType: String? = null

    //界面状态管理者
    private lateinit var loadsir: LoadService<Any>

    //请求ViewModel
    private val requestGankViewModel: RequestGankViewModel by viewModels()

    //recyclerview的底部加载view 因为要在首页动态改变他的颜色，所以加了他这个字段
    private lateinit var footView: DefineLoadMoreView


    private val gankAndroidAdapter: GankAndroidAdapter by lazy {
        GankAndroidAdapter()
    }

    companion object {
        @JvmStatic
        fun newInstance() = CustomFragment()
    }

    override fun layoutId(): Int = R.layout.fragment_android

    override fun initView(savedInstanceState: Bundle?) {
        val type = CacheUtil.getGankType()
        setSelectType(type)
        //状态页配置
        loadsir = loadServiceInit(swipeRefresh) {
            //点击重试时触发的操作
            loadsir.showLoading()
            val type = CacheUtil.getGankType()
            setSelectType(type)
            requestGankViewModel.loadGankData(true, this.gankType)
        }
        val mHeaderView: View = LayoutInflater.from(context).inflate(
            R.layout.header_item_gank_custom,
            recyclerView.parent as ViewGroup,
            false
        )
        recyclerView.addHeaderView(mHeaderView)
        initHeader(mHeaderView)
        //初始化recyclerView
        recyclerView.init(
            LinearLayoutManager(requireActivity()),
            gankAndroidAdapter
        ).let {
//            it.addItemDecoration(GridSpaceItemDecoration(12))
            footView = it.initFooter(SwipeRecyclerView.LoadMoreListener {
                requestGankViewModel.loadGankData(false, gankType)
            })
            //初始化FloatingActionButton
//            it.initFloatBtn(floatBtn)
        }
        //初始化 SwipeRefreshLayout
        swipeRefresh.init {
            val type = CacheUtil.getGankType()
            setSelectType(type)
            //触发刷新监听时请求数据
            requestGankViewModel.loadGankData(true, gankType)
        }
    }

    private fun initHeader(headerView: View) {
        val gankType: String = CacheUtil.getGankType()
        headerView.findViewById<TextView>(R.id.tx_name).text = gankType

        val view: LinearLayout =
            headerView.findViewById<LinearLayout>(R.id.ll_choose_catalogue)
        view.setOnClickListener { v ->
//            if (builder != null) {
//                builder.show()
//            }
        }
    }

    override fun createObserver() {
        requestGankViewModel.sisterDataState.observe(viewLifecycleOwner, Observer {
            //设值 新写了个拓展函数，搞死了这个恶心的重复代码
            loadListData(it, gankAndroidAdapter, loadsir, recyclerView, swipeRefresh)
        })
    }

    override fun lazyLoadData() {
        //设置界面 加载中
        loadsir.showLoading()
        val type = CacheUtil.getGankType()
        setSelectType(type)
        requestGankViewModel.loadGankData(true, gankType)
    }

    private fun setSelectType(type: String) {
        gankType = when (type) {
            "全部" -> "All"
            "iOS" -> "iOS"
            "Flutter" -> "Flutter"
            "App" -> "app"
            "前端" -> "frontend"
            "后端" -> "backend"
            "推荐" -> "promote"
            else -> type
        }
    }

}