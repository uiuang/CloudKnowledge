package com.uiuang.cloudknowledge.ui.fragment.home

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.kingja.loadsir.core.LoadService
import com.uiuang.cloudknowledge.R
import com.uiuang.cloudknowledge.app.base.BaseFragment
import com.uiuang.cloudknowledge.app.http.Constants.ID
import com.uiuang.cloudknowledge.app.http.Constants.IS_LOAD
import com.uiuang.cloudknowledge.app.http.Constants.NAME
import com.uiuang.cloudknowledge.app.http.Constants.WEB_ISTITLEFIX
import com.uiuang.cloudknowledge.app.http.Constants.WEB_TITLE
import com.uiuang.cloudknowledge.app.http.Constants.WEB_URL
import com.uiuang.cloudknowledge.databinding.FragmentCategoryDetailBinding
import com.uiuang.cloudknowledge.ext.*
import com.uiuang.cloudknowledge.ui.adapter.wan.WanAndroidAdapter
import com.uiuang.cloudknowledge.viewmodel.request.RequestWanHomeViewModel
import com.uiuang.cloudknowledge.viewmodel.state.HomeViewModel
import com.uiuang.cloudknowledge.weight.recyclerview.DefineLoadMoreView
import com.uiuang.mvvm.ext.nav
import com.uiuang.mvvm.ext.navigateAction
import com.yanzhenjie.recyclerview.SwipeRecyclerView
import kotlinx.android.synthetic.main.fragment_category_article.*


class CategoryArticleFragment : BaseFragment<HomeViewModel, FragmentCategoryDetailBinding>() {
    private var categoryId: Int = 0
    private var categoryName: String? = null
    private var isLoad: Boolean = false

    //界面状态管理者
    private lateinit var loadSir: LoadService<Any>
    private lateinit var footView: DefineLoadMoreView

    private val requestWanHomeViewModel: RequestWanHomeViewModel by viewModels()
    private val wanAndroidAdapter: WanAndroidAdapter by lazy {
        WanAndroidAdapter(arrayListOf())
    }

    companion object {
        @JvmStatic
        fun newInstance(categoryId: Int, categoryName: String, isLoad: Boolean) =
            CategoryArticleFragment().apply {
                arguments = Bundle().apply {
                    putInt(ID, categoryId)
                    putString(NAME, categoryName)
                    putBoolean(IS_LOAD, isLoad)
                }
            }
    }

    override fun layoutId(): Int = R.layout.fragment_category_article

    override fun initView(savedInstanceState: Bundle?) {
        arguments?.let {
            categoryId = it.getInt(ID)
            categoryName = it.getString(NAME)
            isLoad = it.getBoolean(IS_LOAD)
        }
        //状态页配置
        loadSir = loadServiceInit(recyclerView) {
            //点击重试时触发的操作
            loadSir.showLoading()
            requestWanHomeViewModel.getHomeArticleList(true, categoryId)
        }
        wanAndroidAdapter.setNoShowChapterName()
        recyclerView.let {
            it.init(LinearLayoutManager(requireActivity()), wanAndroidAdapter)
            it.addItemDecoration(
                DividerItemDecoration(
                    requireActivity(),
                    DividerItemDecoration.VERTICAL
                )
            )
            footView = it.initFooter(SwipeRecyclerView.LoadMoreListener {
                requestWanHomeViewModel.getHomeArticleList(false, cid = categoryId)
            })
        }
        wanAndroidAdapter.let {
            it.setOnItemClickListener { _, _, position ->
                val item = it.getItem(position)
                openDetail(item.link, item.title)
            }
        }
    }

    override fun createObserver() {
        requestWanHomeViewModel.articlesBean.observe(viewLifecycleOwner, Observer {
            loadListData(it, wanAndroidAdapter, loadSir, recyclerView)
        })
    }

    override fun lazyLoadData() {
        loadSir.showLoading()
        requestWanHomeViewModel.getHomeArticleList(true, cid = categoryId)

    }

    private fun openDetail(url: String?, title: String?, isTitleFix: Boolean = false) {
        if (!url.isNullOrEmpty()) {
            nav().navigateAction(R.id.action_global_webViewFragment, Bundle().apply {
                putString(WEB_URL, url)
                putBoolean(WEB_ISTITLEFIX, isTitleFix)
                putString(WEB_TITLE, if (title.isNullOrEmpty()) url else title)
            })
        }
    }
}
