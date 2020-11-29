package com.uiuang.cloudknowledge.ui.fragment.mine

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.datetime.datePicker
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.uiuang.cloudknowledge.R
import com.uiuang.cloudknowledge.app.base.BaseFragment
import com.uiuang.cloudknowledge.bean.wan.TodoResponse
import com.uiuang.cloudknowledge.data.enums.TodoType
import com.uiuang.cloudknowledge.databinding.FragmentAddTodoBinding
import com.uiuang.cloudknowledge.ext.initClose
import com.uiuang.cloudknowledge.ext.showMessage
import com.uiuang.cloudknowledge.viewmodel.request.RequestTodoViewModel
import com.uiuang.cloudknowledge.viewmodel.state.TodoViewModel
import com.uiuang.cloudknowledge.weight.customview.PriorityDialog
import com.uiuang.mvvm.ext.nav
import com.uiuang.mvvm.util.DatetimeUtil
import com.uiuang.mvvm.util.notNull
import kotlinx.android.synthetic.main.include_toolbar.*
import java.util.*


class AddTodoFragment : BaseFragment<TodoViewModel, FragmentAddTodoBinding>() {
    private var todoResponse: TodoResponse? = null

    //请求数据ViewModel
    private val requestViewModel: RequestTodoViewModel by viewModels()


    override fun layoutId(): Int = R.layout.fragment_add_todo

    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.click = ProxyClick()
        mDatabind.vm = mViewModel
        arguments?.let {
            todoResponse = it.getParcelable("todo")
            todoResponse?.let { todo ->
                mViewModel.todoTitle.set(todo.title)
                mViewModel.todoContent.set(todo.content)
                mViewModel.todoTime.set(todo.dateStr)
                mViewModel.todoLevel.set(TodoType.byType(todo.priority).content)
                mViewModel.todoColor.set(TodoType.byType(todo.priority).color)
            }
        }
        toolbar.initClose(if (todoResponse == null) "添加TODO" else "修改TODO") {
            nav().navigateUp()
        }
//        appViewModel.appColor.value?.let { SettingUtil.setShapColor(addtodoSubmit, it) }
    }

    override fun createObserver() {
        requestViewModel.updateDataState.observe(viewLifecycleOwner, Observer {
            if (it.isSuccess) {
                //添加TODO成功 返回并发送消息回调
                nav().navigateUp()
                eventViewModel.todoEvent.setValue(false)
            } else {
                showMessage(it.errorMsg)
            }
        })
    }

    inner class ProxyClick {
        /** 选择时间*/
        fun todoTime() {
            activity?.let {
                MaterialDialog(it)
                    .lifecycleOwner(this@AddTodoFragment).show {
                        cornerRadius(0f)
                        datePicker(minDate = Calendar.getInstance()) { dialog, date ->
                            mViewModel.todoTime.set(
                                DatetimeUtil.formatDate(
                                    date.time,
                                    DatetimeUtil.DATE_PATTERN
                                )
                            )
                        }
                    }
            }
        }

        /**选择类型*/
        fun todoType() {
            activity?.let {
                PriorityDialog(it, TodoType.byValue(mViewModel.todoLevel.get()).type).apply {
                    setPriorityInterface(object : PriorityDialog.PriorityInterface {
                        override fun onSelect(type: TodoType) {
                            mViewModel.todoLevel.set(type.content)
                            mViewModel.todoColor.set(type.color)
                        }
                    })
                }.show()
            }
        }

        /**提交*/
        fun submit() {
            when {
                mViewModel.todoTitle.get().isEmpty() -> {
                    showMessage("请填写标题")
                }
                mViewModel.todoContent.get().isEmpty() -> {
                    showMessage("请填写内容")
                }
                mViewModel.todoTime.get().isEmpty() -> {
                    showMessage("请选择预计完成时间")
                }
                else -> {
                    todoResponse.notNull({
                        showMessage("确认提交编辑吗？", positiveButtonText = "提交", positiveAction = {
                            requestViewModel.updateTodo(
                                it.id,
                                mViewModel.todoTitle.get(),
                                mViewModel.todoContent.get(),
                                mViewModel.todoTime.get(),
                                TodoType.byValue(mViewModel.todoLevel.get()).type
                            )
                        }, negativeButtonText = "取消")
                    }, {
                        showMessage("确认添加吗？", positiveButtonText = "添加", positiveAction = {
                            requestViewModel.addTodo(
                                mViewModel.todoTitle.get(),
                                mViewModel.todoContent.get(),
                                mViewModel.todoTime.get(),
                                TodoType.byValue(mViewModel.todoLevel.get()).type
                            )
                        }, negativeButtonText = "取消")
                    })
                }
            }
        }

    }

}