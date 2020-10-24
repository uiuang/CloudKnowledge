package com.uiuang.cloudknowledge.ui.fragment.home

import android.os.Bundle
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.kingja.loadsir.core.LoadService
import com.uiuang.cloudknowledge.R
import com.uiuang.cloudknowledge.app.base.BaseFragment
import com.uiuang.cloudknowledge.bean.TabBean
import com.uiuang.cloudknowledge.databinding.FragmentTreeBinding
import com.uiuang.cloudknowledge.databinding.HeaderItemTreeBinding
import com.uiuang.cloudknowledge.ext.init
import com.uiuang.cloudknowledge.ext.loadListData
import com.uiuang.cloudknowledge.ext.loadServiceInit
import com.uiuang.cloudknowledge.ext.showLoading
import com.uiuang.cloudknowledge.ui.adapter.wan.TreeAdapter
import com.uiuang.cloudknowledge.utils.DataUtil
import com.uiuang.cloudknowledge.utils.showToastLong
import com.uiuang.cloudknowledge.viewmodel.request.RequestTreeViewModel
import com.uiuang.cloudknowledge.viewmodel.state.WanFindViewModel
import com.uiuang.cloudknowledge.weight.recyclerview.SpacesItemDecoration
import com.uiuang.mvvm.ext.nav
import com.uiuang.mvvm.ext.navigateAction
import kotlinx.android.synthetic.main.fragment_tree.*


class TreeFragment : BaseFragment<WanFindViewModel, FragmentTreeBinding>() {

    //界面状态管理者
    private lateinit var loadSir: LoadService<Any>

    private val requestTreeViewModel: RequestTreeViewModel by viewModels()

    private lateinit var headerItemTreeBinding: HeaderItemTreeBinding

    private val treeAdapter: TreeAdapter by lazy {
        TreeAdapter()
    }

    private val layoutManager: LinearLayoutManager by lazy {
        LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }

    companion object {
        @JvmStatic
        fun newInstance() = TreeFragment()
    }

    override fun layoutId(): Int = R.layout.fragment_tree

    override fun initView(savedInstanceState: Bundle?) {
        //状态页配置
        loadSir = loadServiceInit(swipeRefresh) {
            //点击重试时触发的操作
            loadSir.showLoading()
            requestTreeViewModel.getTree()
        }
        headerItemTreeBinding = DataBindingUtil.inflate<HeaderItemTreeBinding>(
            layoutInflater,
            R.layout.header_item_tree,
            null,
            false
        )
        srv_tree.addHeaderView(headerItemTreeBinding.root)
        val tvPosition: TextView = headerItemTreeBinding.tvPosition
        tvPosition.setOnClickListener {
            if (!treeAdapter.isSelect()) {
                val layoutManager = GridLayoutManager(activity, 2)
                srv_tree.layoutManager = layoutManager
                tvPosition.text = "选择类别"
                treeAdapter.setSelect(true)
                treeAdapter.notifyDataSetChanged()
                srv_tree.addItemDecoration(
                    SpacesItemDecoration(requireActivity()).setNoShowDivider(
                        1,
                        0
                    ).setDrawable(R.drawable.shape_line)
                )
            } else {
//                val layoutManager = LinearLayoutManager(activity)
                srv_tree.layoutManager = layoutManager
                tvPosition.text = "发现页内容订制"
                treeAdapter.setSelect(false)
                treeAdapter.notifyDataSetChanged()
                if (srv_tree.itemDecorationCount > 0) {
                    srv_tree.removeItemDecorationAt(0)
                }
            }
        }

        srv_tree.init(layoutManager, treeAdapter)
        treeAdapter.run {
            setNavigationAction { item, id, view ->
                startDetail(id, item)
            }
            setOnItemClickListener { adapter, view, position ->
                if (treeAdapter.isSelect()) {
                    if (treeAdapter.getSelectedPosition() == position.minus(srv_tree.headerCount)) {
                        "当前已经是\"${treeAdapter.data[position.minus(srv_tree.headerCount)].name}\"".showToastLong();
                        return@setOnItemClickListener
                    }
                    srv_tree.layoutManager = layoutManager
                    tvPosition.text = "发现页内容订制"
                    treeAdapter.setSelect(false)
                    treeAdapter.notifyDataSetChanged()
                    if (srv_tree.itemDecorationCount > 0) {
                        srv_tree.removeItemDecorationAt(0)
                    }
                    layoutManager.scrollToPositionWithOffset(
                        position,
                        0
                    )
                    appViewModel.findPosition.value = position.minus(srv_tree.headerCount)

                }
            }
        }


        //初始化 SwipeRefreshLayout
        swipeRefresh.init {
            //触发刷新监听时请求数据
            requestTreeViewModel.getTree()
        }


    }

    override fun createObserver() {
        requestTreeViewModel.tabBean.observe(viewLifecycleOwner, Observer {
            if (treeAdapter.data.size == 0) {
                DataUtil.putTreeData(requireActivity(), it.listData)
            }
            loadListData(it, treeAdapter, loadSir, srv_tree, swipeRefresh)
        })
    }

    override fun lazyLoadData() {
        requestTreeViewModel.getTree()
    }

    private fun startDetail(id: Int, tabBean: TabBean) {
        nav().navigateAction(R.id.action_mainFragment_to_categoryDetailFragment, Bundle().apply {
            putInt("id", id)
            putParcelable("tabBean", tabBean)
        })
    }
}
