package com.uiuang.cloudknowledge.ui.fragment.mine

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.uiuang.cloudknowledge.R
import com.uiuang.cloudknowledge.app.base.BaseFragment
import com.uiuang.cloudknowledge.databinding.FragmentListBinding
import com.uiuang.cloudknowledge.viewmodel.state.TodoViewModel


class TodoListFragment : BaseFragment<TodoViewModel, FragmentListBinding>() {


    override fun layoutId(): Int = R.layout.fragment_list

    override fun initView(savedInstanceState: Bundle?) {
    }


}