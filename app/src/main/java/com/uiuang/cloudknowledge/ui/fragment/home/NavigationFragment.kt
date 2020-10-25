package com.uiuang.cloudknowledge.ui.fragment.home

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.kingja.loadsir.core.LoadService
import com.uiuang.cloudknowledge.R
import com.uiuang.cloudknowledge.app.base.BaseFragment
import com.uiuang.cloudknowledge.bean.wan.WebBean
import com.uiuang.cloudknowledge.data.enums.CollectType
import com.uiuang.cloudknowledge.databinding.FragmentNavigationBinding
import com.uiuang.cloudknowledge.ext.*
import com.uiuang.cloudknowledge.ui.adapter.wan.NavigationAdapter
import com.uiuang.cloudknowledge.ui.adapter.wan.NavigationContentAdapter
import com.uiuang.cloudknowledge.viewmodel.request.RequestNavigationViewModel
import com.uiuang.cloudknowledge.viewmodel.state.HomeViewModel
import com.uiuang.mvvm.ext.nav
import com.uiuang.mvvm.ext.navigateAction
import kotlinx.android.synthetic.main.fragment_navigation.*


class NavigationFragment : BaseFragment<HomeViewModel, FragmentNavigationBinding>() {
    private var currentPosition: Int = 0

    //界面状态管理者
    private lateinit var loadSir: LoadService<Any>
    private val requestNavigationViewModel: RequestNavigationViewModel by viewModels()
    private val navigationAdapter: NavigationAdapter by lazy {
        NavigationAdapter()
    }
    private val navigationContentAdapter: NavigationContentAdapter by lazy {
        NavigationContentAdapter()
    }

    private val layoutManager1: LinearLayoutManager by lazy {
        LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }
    private val layoutManager2: LinearLayoutManager by lazy {
        LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }

    companion object {

        @JvmStatic
        fun newInstance() = NavigationFragment()
    }

    override fun layoutId(): Int = R.layout.fragment_navigation

    override fun initView(savedInstanceState: Bundle?) {
        //状态页配置
        loadSir = loadServiceInit(recyclerView) {
            //点击重试时触发的操作
            lazyLoadData()
        }
        rv_navigation.init(layoutManager1, navigationAdapter)
        navigationAdapter.setOnItemClickListener { adapter, view, position ->
            position.selectItem()
//            position.moveToCenter()

            layoutManager1.scrollToPositionWithOffset(position, 0)
            layoutManager2.scrollToPositionWithOffset(position, 0)
        }
        recyclerView.init(layoutManager2, navigationContentAdapter)

        navigationContentAdapter.setNavigationAction { item, _ ->
            nav().navigateAction(R.id.action_global_webViewFragment, Bundle().apply {
                val webBean = WebBean(
                    item.id,
                    item.collect,
                    item.title,
                    item.link,
                    CollectType.Url.type
                )
                putParcelable("webBean", webBean)
            })
        }


    }

    override fun createObserver() {
        requestNavigationViewModel.navJsonBean.observe(viewLifecycleOwner, Observer {
            if (it.isSuccess) {
                loadSir.showSuccess()
                navigationContentAdapter.setList(it.listData)
            } else {
                loadSir.showError(it.errMessage)
            }
        })
        requestNavigationViewModel.navJsonBeanList.observe(viewLifecycleOwner, Observer {
            navigationAdapter.setNewInstance(it)
            0.selectItem()
        })

        appViewModel.run {
            //监听账户信息是否改变 有值时(登录)将相关的数据设置为已收藏，为空时(退出登录)，将已收藏的数据变为未收藏
            userinfo.observe(viewLifecycleOwner, Observer {
                if (it != null) {
                    it.collectIds.forEach { id ->
                        for (item in navigationContentAdapter.data) {
                            for (articles in item.articles!!) {
                                if (id.toInt() == articles.id) {
                                    articles.collect = true
                                    break
                                }
                            }
                        }
                    }
                } else {
                    for (item in navigationContentAdapter.data) {
                        for (articles in item.articles!!) {
                            articles.collect = false

                        }
                    }
                }
                navigationContentAdapter.notifyDataSetChanged()
            })

            //监听全局的列表动画改编
            appAnimation.observe(viewLifecycleOwner, Observer {
                navigationContentAdapter.setAdapterAnimation(it)
            })
            //监听全局的收藏信息 收藏的Id跟本列表的数据id匹配则需要更新
            eventViewModel.collectEvent.observe(viewLifecycleOwner, Observer {
                for (index in navigationContentAdapter.data.indices) {
                    for (item in navigationContentAdapter.data[index].articles!!) {
                        if (item.id == it.id) {
                            item.collect = it.collect
                            navigationContentAdapter.notifyItemChanged(index)
                            break
                        }
                    }
                }
            })
        }

    }

    private fun Int.selectItem() {
        if (this < 0 || navigationAdapter.data.size <= this) {
            return
        }
        navigationAdapter.data[currentPosition].selected = false
        navigationAdapter.notifyItemChanged(currentPosition)
        currentPosition = this
        navigationAdapter.data[this].selected = true
        navigationAdapter.notifyItemChanged(this)
    }

    private fun Int.moveToCenter() {
        recyclerView.scrollToPosition(this)
    }


    override fun lazyLoadData() {
        loadSir.showLoading()
        requestNavigationViewModel.getNavigationJson()
    }

    private fun openDetail(url: String?, title: String?, isTitleFix: Boolean = false) {
        if (!url.isNullOrEmpty()) {
            nav().navigateAction(R.id.action_global_webViewFragment, Bundle().apply {
                putString("url", url)
                putBoolean("isTitleFix", isTitleFix)
                putString("title", if (title.isNullOrEmpty()) url else title)
            })
        }
    }
}