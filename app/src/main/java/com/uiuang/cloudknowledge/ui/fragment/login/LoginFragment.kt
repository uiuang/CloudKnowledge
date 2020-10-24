package com.uiuang.cloudknowledge.ui.fragment.login

import android.os.Bundle
import android.widget.CompoundButton
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.uiuang.cloudknowledge.R
import com.uiuang.cloudknowledge.app.base.BaseFragment
import com.uiuang.cloudknowledge.databinding.FragmentLoginBinding
import com.uiuang.cloudknowledge.ext.hideSoftKeyboard
import com.uiuang.cloudknowledge.ext.initClose
import com.uiuang.cloudknowledge.ext.showMessage
import com.uiuang.cloudknowledge.utils.CacheUtil
import com.uiuang.cloudknowledge.viewmodel.request.RequestLoginRegisterViewModel
import com.uiuang.cloudknowledge.viewmodel.state.LoginRegisterViewModel
import com.uiuang.mvvm.ext.nav
import com.uiuang.mvvm.ext.navigateAction
import com.uiuang.mvvm.ext.parseState
import kotlinx.android.synthetic.main.fragment_login.*


class LoginFragment : BaseFragment<LoginRegisterViewModel, FragmentLoginBinding>() {

    private val requestLoginRegisterViewModel: RequestLoginRegisterViewModel by viewModels()

    override fun layoutId(): Int = R.layout.fragment_login

    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.viewModel = mViewModel
        mDatabind.click = ProxyClick()

        toolbar.initClose("云知登录") {
            nav().navigateUp()
        }
    }


    override fun createObserver() {
        requestLoginRegisterViewModel.loginResult.observe(
            viewLifecycleOwner,
            Observer { resultState ->
                parseState(resultState, {
                    //登录成功 通知账户数据发生改变鸟
                    CacheUtil.setUser(it)
                    appViewModel.userinfo.postValue(it)
                    nav().navigateUp()
                }, {
                    //登录失败
                    showMessage(it.errorMsg)
                })
            })
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