package com.uiuang.cloudknowledge.ui.fragment.gank

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BasicGridItem
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.bottomsheets.setPeekHeight
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.afollestad.materialdialogs.list.customListAdapter
import com.kingja.loadsir.core.LoadService
import com.uiuang.cloudknowledge.R
import com.uiuang.cloudknowledge.app.base.BaseFragment
import com.uiuang.cloudknowledge.bean.gank.GankIOResultBean
import com.uiuang.cloudknowledge.bean.wan.WebBean
import com.uiuang.cloudknowledge.data.enums.CollectType
import com.uiuang.cloudknowledge.databinding.IncludeListBinding
import com.uiuang.cloudknowledge.ext.*
import com.uiuang.cloudknowledge.ui.adapter.gank.GankAndroidAdapter
import com.uiuang.cloudknowledge.ui.adapter.gank.ListIconDialogAdapter
import com.uiuang.cloudknowledge.utils.CacheUtil
import com.uiuang.cloudknowledge.utils.toast
import com.uiuang.cloudknowledge.viewmodel.request.RequestGankViewModel
import com.uiuang.cloudknowledge.viewmodel.state.HomeViewModel
import com.uiuang.cloudknowledge.weight.recyclerview.DefineLoadMoreView
import com.uiuang.mvvm.ext.nav
import com.uiuang.mvvm.ext.navigateAction
import com.uiuang.mvvm.util.dp2px
import com.uiuang.mvvm.util.screenHeight
import com.yanzhenjie.recyclerview.SwipeRecyclerView
import kotlinx.android.synthetic.main.include_list.*
import kotlinx.android.synthetic.main.include_recyclerview.*


class CustomFragment : BaseFragment<HomeViewModel, IncludeListBinding>() {

    private var gankType: String? = null

    //界面状态管理者
    private lateinit var loadSir: LoadService<Any>

    //请求ViewModel
    private val requestGankViewModel: RequestGankViewModel by viewModels()

    //recyclerview的底部加载view 因为要在首页动态改变他的颜色，所以加了他这个字段
    private lateinit var footView: DefineLoadMoreView

    private val items by lazy {
        arrayListOf(
            BasicGridItem(R.drawable.home_title_all, "全部"),
            BasicGridItem(R.drawable.home_title_flutter, "Flutter"),
            BasicGridItem(R.drawable.home_title_ios, "iOS"),
            BasicGridItem(R.drawable.home_title_app, "App"),
            BasicGridItem(R.drawable.home_title_qian, "前端"),
            BasicGridItem(R.drawable.home_title_backend, "后端")
//            BasicGridItem(R.drawable.home_title_source, "推荐")
        )
    }
    private val iconDialogAdapter: ListIconDialogAdapter by lazy {
        ListIconDialogAdapter(items)
    }
    private val gankAndroidAdapter: GankAndroidAdapter by lazy {
        GankAndroidAdapter()
    }

    companion object {
        @JvmStatic
        fun newInstance() = CustomFragment()
    }

    override fun layoutId(): Int = R.layout.include_list

    override fun initView(savedInstanceState: Bundle?) {
        val type = CacheUtil.getGankType()
        setSelectType(type)
        //状态页配置
        loadSir = loadServiceInit(recyclerView) {
            //点击重试时触发的操作
            loadSir.showLoading()
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
            it.initFloatBtn(floatBtn)
        }
        //初始化 SwipeRefreshLayout
        swipeRefresh.init {
            val type = CacheUtil.getGankType()
            setSelectType(type)
            //触发刷新监听时请求数据
            requestGankViewModel.loadGankData(true, gankType)
        }
        gankAndroidAdapter.setOnItemClickListener { _, _, position ->
            val item: GankIOResultBean = gankAndroidAdapter.getItem(position - 1)
            nav().navigateAction(R.id.action_global_webViewFragment, Bundle().apply {
                val webBean = WebBean(
                    0,
                    false,
                    item.title,
                    item.url,
                    CollectType.Url.type
                )
                putParcelable("webBean", webBean)
            })
        }
    }

    private fun initHeader(headerView: View) {
        val gankType: String = CacheUtil.getGankType()
        val tvName: TextView = headerView.findViewById<TextView>(R.id.tx_name)
        tvName.text = gankType

        val view: LinearLayout =
            headerView.findViewById<LinearLayout>(R.id.ll_choose_catalogue)
        view.setOnClickListener { v ->
            MaterialDialog(requireActivity(), BottomSheet())
                .cancelable(true)
                .lifecycleOwner(this)
                .show {
                    setPeekHeight(requireActivity().screenHeight - requireActivity().dp2px(200))
                    title(text = "选择分类")
                    customListAdapter(
                        iconDialogAdapter,
                        LinearLayoutManager(requireActivity())
                    )
                    iconDialogAdapter.setOnItemClickListener { adapter, view, position ->
                        if (isOtherType(items[position].title)) {
                            changeContent(tvName, items[position].title)
                            dismiss()
                        }
                    }
                }
        }
    }

    private fun changeContent(textView: TextView, content: String) {
        textView.text = content
        setSelectType(content)
        CacheUtil.setGankType(content)
        loadSir.showLoading()
        requestGankViewModel.loadGankData(true, mType = gankType)
    }

    override fun createObserver() {
        requestGankViewModel.sisterDataState.observe(viewLifecycleOwner, Observer {
            //设值 新写了个拓展函数，搞死了这个恶心的重复代码
            loadListData(it, gankAndroidAdapter, loadSir, recyclerView, swipeRefresh)
        })
        appViewModel.appColor.observe(viewLifecycleOwner, Observer {
            //监听全局的主题颜色改变
            setUiTheme(it, floatBtn, swipeRefresh, loadSir, footView)
        })
    }

    override fun lazyLoadData() {
        //设置界面 加载中
        loadSir.showLoading()
        val type = CacheUtil.getGankType()
        setSelectType(type)
        requestGankViewModel.loadGankData(true, gankType)
    }

    private fun isOtherType(selectType: String): Boolean {
        val clickText: String = CacheUtil.getGankType()
        return if (clickText == selectType) {
            "当前已经是${selectType}分类".toast()
            false
        } else {
            true
        }
    }

    private fun setSelectType(type: String) {
        gankType = when (type) {
            "全部" -> "All"
            "iOS" -> "iOS"
            "Flutter" -> "Flutter"
            "App" -> "app"
            "前端" -> "frontend"
            "后端" -> "backend"
//            "推荐" -> "promote"
            else -> type
        }
    }

}