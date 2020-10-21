package com.uiuang.cloudknowledge.ui.fragment.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.fragment.app.viewModels
import com.uiuang.cloudknowledge.R
import com.uiuang.cloudknowledge.app.base.BaseFragment
import com.uiuang.cloudknowledge.databinding.FragmentLoginBinding
import com.uiuang.cloudknowledge.ext.hideSoftKeyboard
import com.uiuang.cloudknowledge.ext.init
import com.uiuang.cloudknowledge.ext.initClose
import com.uiuang.cloudknowledge.ext.showMessage
import com.uiuang.cloudknowledge.viewmodel.request.RequestLoginRegisterViewModel
import com.uiuang.cloudknowledge.viewmodel.state.LoginRegisterViewModel
import com.uiuang.mvvm.ext.nav
import com.uiuang.mvvm.ext.navigateAction
import kotlinx.android.synthetic.main.fragment_login.*


class LoginFragment : BaseFragment<LoginRegisterViewModel, FragmentLoginBinding>() {

    private val requestLoginRegisterViewModel: RequestLoginRegisterViewModel by viewModels()

    override fun layoutId(): Int = R.layout.fragment_login

    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.viewModel = mViewModel
        mDatabind.click = ProxyClick()

        toolbar.initClose("云知登录"){
            nav().navigateUp()
        }
    }


    override fun createObserver() {

    }
    inner class ProxyClick {

        fun clear() {
            mViewModel.username.value = ""
        }

        fun login() {
            when {
                mViewModel.username.value.isEmpty() -> showMessage("请填写账号")
                mViewModel.password.get().isEmpty() -> showMessage("请填写密码")
                else -> requestLoginRegisterViewModel.loginReq(
                    mViewModel.username.value,
                    mViewModel.password.get()
                )
            }
        }

        fun goRegister() {
            hideSoftKeyboard(activity)
            nav().navigateAction(R.id.action_loginFragment_to_registerFragment)
        }

        var onCheckedChangeListener =
            CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
                mViewModel.isShowPwd.set(isChecked)
            }

    }


}