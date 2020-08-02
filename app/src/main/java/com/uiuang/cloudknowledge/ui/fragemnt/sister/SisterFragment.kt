package com.uiuang.cloudknowledge.ui.fragemnt.sister

import android.os.Bundle
import androidx.fragment.app.viewModels
import com.kingja.loadsir.core.LoadService
import com.uiuang.cloudknowledge.R
import com.uiuang.cloudknowledge.app.base.BaseFragment
import com.uiuang.cloudknowledge.databinding.FragmentSisterBinding
import com.uiuang.cloudknowledge.ext.init
import com.uiuang.cloudknowledge.ext.loadServiceInit
import com.uiuang.cloudknowledge.ext.showLoading
import com.uiuang.cloudknowledge.viewmodel.request.RequestSisterViewModel
import com.uiuang.cloudknowledge.viewmodel.state.HomeViewModel
import com.uiuang.cloudknowledge.weight.recyclerview.DefineLoadMoreView
import kotlinx.android.synthetic.main.fragment_sister.*


class SisterFragment : BaseFragment<HomeViewModel, FragmentSisterBinding>() {
    //界面状态管理者
    private lateinit var loadsir: LoadService<Any>

    //请求ViewModel
    private val requestTreeViewModel: RequestSisterViewModel by viewModels()

    //recyclerview的底部加载view 因为要在首页动态改变他的颜色，所以加了他这个字段
    private lateinit var footView: DefineLoadMoreView
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
            requestTreeViewModel.getPlazaData(true)
        }
        //初始化recyclerView
//        recyclerView.init(LinearLayoutManager(context), articleAdapter).let {
//            it.addItemDecoration(SpaceItemDecoration(0, ConvertUtils.dp2px(8f)))
//            footView = it.initFooter(SwipeRecyclerView.LoadMoreListener {
//                requestTreeViewModel.getPlazaData(false)
//            })
//            //初始化FloatingActionButton
//            it.initFloatBtn(floatbtn)
//        }
        //初始化 SwipeRefreshLayout
        swipeRefresh.init {
            //触发刷新监听时请求数据
            requestTreeViewModel.getPlazaData(true)
        }
    }

    override fun lazyLoadData() {
        //设置界面 加载中
        loadsir.showLoading()
        requestTreeViewModel.getPlazaData(true)
    }

}