package com.uiuang.cloudknowledge.ui.fragment.mine

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.uiuang.cloudknowledge.R
import com.uiuang.cloudknowledge.app.base.BaseFragment
import com.uiuang.cloudknowledge.databinding.FragmentAddTodoBinding
import com.uiuang.cloudknowledge.viewmodel.state.TodoViewModel


class AddTodoFragment : BaseFragment<TodoViewModel,FragmentAddTodoBinding>() {




    override fun layoutId(): Int =R.layout.fragment_add_todo

    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.click = ProxyClick()
        mDatabind.vm = mViewModel

    }


    inner class ProxyClick {
        /** 选择时间*/
        fun todoTime() {

        }

        /**选择类型*/
        fun todoType() {

        }

        /**提交*/
        fun submit() {

        }

    }

}