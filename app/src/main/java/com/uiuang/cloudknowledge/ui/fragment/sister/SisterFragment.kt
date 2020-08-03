package com.uiuang.cloudknowledge.ui.fragment.sister

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.kingja.loadsir.core.LoadService
import com.uiuang.cloudknowledge.R
import com.uiuang.cloudknowledge.app.base.BaseFragment
import com.uiuang.cloudknowledge.databinding.FragmentSisterBinding
import com.uiuang.cloudknowledge.ext.*
import com.uiuang.cloudknowledge.ui.adapter.gank.WelfareAdapter
import com.uiuang.cloudknowledge.viewmodel.request.RequestSisterViewModel
import com.uiuang.cloudknowledge.viewmodel.state.HomeViewModel
import com.uiuang.cloudknowledge.weight.recyclerview.DefineLoadMoreView
import com.uiuang.cloudknowledge.weight.recyclerview.GridSpaceItemDecoration
import com.yanzhenjie.recyclerview.SwipeRecyclerView
import kotlinx.android.synthetic.main.fragment_sister.*


class SisterFragment : BaseFragment<HomeViewModel, FragmentSisterBinding>() {
    //界面状态管理者
    private lateinit var loadsir: LoadService<Any>

    //请求ViewModel
    private val requestSisterViewModel: RequestSisterViewModel by viewModels()

    //recyclerview的底部加载view 因为要在首页动态改变他的颜色，所以加了他这个字段
    private lateinit var footView: DefineLoadMoreView

    private val welfareAdapter: WelfareAdapter by lazy {
        WelfareAdapter(arrayListOf())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    companion object {
        @JvmStatic
        fun newInstance() = SisterFragment()
    }

    override fun layoutId(): Int = R.layout.fragment_sister

    override fun initView(savedInstanceState: Bundle?) {
        //初始化 toolbar
        toolbar.run {
            init("福利")
        }
//        requestTreeViewModel.getPlazaData(true)

        //状态页配置
        loadsir = loadServiceInit(swipeRefresh) {
            //点击重试时触发的操作
            loadsir.showLoading()
            requestSisterViewModel.getPlazaData(true)
        }
        //初始化recyclerView
        recyclerView.init(
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL),
            welfareAdapter
        ).let {
            it.addItemDecoration(GridSpaceItemDecoration(12))
            footView = it.initFooter(SwipeRecyclerView.LoadMoreListener {
                requestSisterViewModel.getPlazaData(false)
            })
            //初始化FloatingActionButton
            it.initFloatBtn(floatBtn)
        }
        //初始化 SwipeRefreshLayout
        swipeRefresh.init {
            //触发刷新监听时请求数据
            requestSisterViewModel.getPlazaData(true)
        }
        welfareAdapter.run {
            setOnItemClickListener { adapter, view, position ->
//                nav().navigateAction(R.id.action_to_webFragment, Bundle().apply {
//                    putParcelable("ariticleData", articleAdapter.data[position])
//                })
            }
        }
    }


    override fun createObserver() {
        super.createObserver()
        requestSisterViewModel.sisterDataState.observe(viewLifecycleOwner, Observer {
            //设值 新写了个拓展函数，搞死了这个恶心的重复代码
            loadListData(it, welfareAdapter, loadsir, recyclerView, swipeRefresh)
        })
    }

    override fun lazyLoadData() {
        //设置界面 加载中
        loadsir.showLoading()
        requestSisterViewModel.getPlazaData(true)
    }

}