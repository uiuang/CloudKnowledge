package com.uiuang.cloudknowledge.ui.fragment.mine

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.bottomsheets.setPeekHeight
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.afollestad.materialdialogs.list.listItems
import com.kingja.loadsir.core.LoadService
import com.uiuang.cloudknowledge.R
import com.uiuang.cloudknowledge.app.base.BaseFragment
import com.uiuang.cloudknowledge.databinding.FragmentListBinding
import com.uiuang.cloudknowledge.ext.*
import com.uiuang.cloudknowledge.ui.adapter.wan.TodoListAdapter
import com.uiuang.cloudknowledge.viewmodel.request.RequestTodoViewModel
import com.uiuang.cloudknowledge.viewmodel.state.TodoViewModel
import com.uiuang.cloudknowledge.weight.recyclerview.SpaceItemDecoration
import com.uiuang.mvvm.ext.nav
import com.uiuang.mvvm.ext.navigateAction
import com.uiuang.mvvm.util.dp2px
import com.yanzhenjie.recyclerview.SwipeRecyclerView
import kotlinx.android.synthetic.main.include_list.*
import kotlinx.android.synthetic.main.include_recyclerview.*
import kotlinx.android.synthetic.main.include_toolbar.*


class TodoListFragment : BaseFragment<TodoViewModel, FragmentListBinding>() {
    //适配器
    private val articleAdapter: TodoListAdapter by lazy { TodoListAdapter(arrayListOf()) }

    //界面状态管理者
    private lateinit var loadsir: LoadService<Any>

    //请求数据ViewModel
    private val requestViewModel: RequestTodoViewModel by viewModels()

    override fun layoutId(): Int = R.layout.fragment_list

    override fun initView(savedInstanceState: Bundle?) {
        toolbar.run {
            initClose("TODO") {
                nav().navigateUp()
            }
            inflateMenu(R.menu.todo_menu)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.todo_add -> {
                        nav().navigateAction(R.id.action_todoListFragment_to_addTodoFragment)
                    }
                }
                true
            }
        }
        //状态页配置 swipeRefresh参数表示你要包裹的布局
        loadsir = loadServiceInit(swipeRefresh) {
            //点击错误重试时触发的操作
            loadsir.showLoading()
            //请求数据
            requestViewModel.getTodoData(true)
        }

        //初始化recyclerView
        recyclerView.init(LinearLayoutManager(context), articleAdapter).let {
            it.addItemDecoration(SpaceItemDecoration(0, requireContext().dp2px(8)))
            it.initFooter(SwipeRecyclerView.LoadMoreListener {
                //触发加载更多时请求数据
                requestViewModel.getTodoData(false)
            })
            //初始化FloatingActionButton
            it.initFloatBtn(floatBtn)
        }
        //初始化 SwipeRefreshLayout
        swipeRefresh.init {
            //触发刷新监听时请求数据
            requestViewModel.getTodoData(true)
        }
        articleAdapter.run {
            setOnItemClickListener { _, _, position ->
                nav().navigateAction(R.id.action_todoListFragment_to_addTodoFragment,
                    Bundle().apply {
                        putParcelable("todo", articleAdapter.data[position])
                    })
            }
            addChildClickViewIds(R.id.item_todo_setting)
            setOnItemChildClickListener { adapter, view, position ->
                when (view.id) {
                    R.id.item_todo_setting -> {
                        val items = if (articleAdapter.data[position].isDone()) {
                            listOf("删除", "编辑")
                        } else {
                            listOf("删除", "编辑", "完成")
                        }
                        activity?.let { activity ->
                            MaterialDialog(activity, BottomSheet())
                                .lifecycleOwner(viewLifecycleOwner).show {
                                    cornerRadius(8f)
                                    setPeekHeight(requireContext().dp2px((items.size * 50 + 36)))
                                    listItems(items = items) { _, index, item ->
                                        when (index) {
                                            0 -> {
                                                //删除
                                                requestViewModel.delTodo(
                                                    articleAdapter.data[position].id,
                                                    position
                                                )
                                            }
                                            1 -> {
                                                //编辑
                                                nav().navigateAction(R.id.action_todoListFragment_to_addTodoFragment,
                                                    Bundle().apply {
                                                        putParcelable(
                                                            "todo",
                                                            articleAdapter.data[position]
                                                        )
                                                    })
                                            }
                                            2 -> {
                                                //完成
                                                requestViewModel.doneTodo(
                                                    articleAdapter.data[position].id,
                                                    position
                                                )
                                            }
                                        }
                                    }
                                }
                        }
                    }
                }
            }
        }
    }
    override fun lazyLoadData() {
        //设置界面 加载中
        loadsir.showLoading()
        requestViewModel.getTodoData(true)
    }

    override fun createObserver() {
        requestViewModel.todoDataState.observe(viewLifecycleOwner, Observer {
            //设值 新写了个拓展函数，搞死了这个恶心的重复代码
            loadListData(it, articleAdapter, loadsir, recyclerView,swipeRefresh)
        })
        requestViewModel.delDataState.observe(viewLifecycleOwner, Observer {
            if (it.isSuccess) {
                if (articleAdapter.data.size == 1) {
                    loadsir.showEmpty()
                }
                articleAdapter.remove(it.data!!)
            } else {
                showMessage(it.errorMsg)
            }
        })
        requestViewModel.doneDataState.observe(viewLifecycleOwner, Observer {
            if (it.isSuccess) {
                swipeRefresh.isRefreshing = true
                requestViewModel.getTodoData(true)
            } else {
                showMessage(it.errorMsg)
            }
        })

        eventViewModel.todoEvent.observe(viewLifecycleOwner, Observer {
            if (articleAdapter.data.size == 0) {
                //界面没有数据时，变为加载中 增强一丢丢体验
                loadsir.showLoading()
            } else {
                //有数据时，swipeRefresh 显示刷新状态
                swipeRefresh.isRefreshing = true
            }
            //请求数据
            requestViewModel.getTodoData(true)
        })

    }

}