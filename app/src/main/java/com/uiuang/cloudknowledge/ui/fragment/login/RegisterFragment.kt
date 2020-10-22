package com.uiuang.cloudknowledge.ui.fragment.login

import android.os.Bundle
import android.widget.CompoundButton
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.uiuang.cloudknowledge.R
import com.uiuang.cloudknowledge.app.base.BaseFragment
import com.uiuang.cloudknowledge.databinding.FragmentRegisterBinding
import com.uiuang.cloudknowledge.ext.initClose
import com.uiuang.cloudknowledge.ext.showMessage
import com.uiuang.cloudknowledge.utils.CacheUtil
import com.uiuang.cloudknowledge.viewmodel.request.RequestLoginRegisterViewModel
import com.uiuang.cloudknowledge.viewmodel.state.LoginRegisterViewModel
import com.uiuang.mvvm.ext.nav
import com.uiuang.mvvm.ext.navigateAction
import com.uiuang.mvvm.ext.parseState
import kotlinx.android.synthetic.main.fragment_register.*


class RegisterFragment : BaseFragment<LoginRegisterViewModel,FragmentRegisterBinding>() {

    private val requestLoginRegisterViewModel: RequestLoginRegisterViewModel by viewModels()
    override fun layoutId(): Int =R.layout.fragment_register

    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.viewModel = mViewModel
        mDatabind.click = ProxyClick()
        toolbar.initClose("注册") {
            nav().navigateUp()
        }
    }

    override fun createObserver() {
        requestLoginRegisterViewModel.loginResult.observe(
            viewLifecycleOwner,
            Observer { resultState ->
                parseState(resultState, {
                    CacheUtil.setUser(it)
                    appViewModel.userinfo.postValue(it)
                    nav().navigateAction(R.id.action_registerFragment_to_mainFragment)
                }, {
                    showMessage(it.errorMsg)
                })
            })
    }

    inner class ProxyClick {
        /**清空*/
        fun clear() {
            mViewModel.username.value=""
        }

        /**注册*/
        fun register() {
            when {
                mViewModel.username.value.isEmpty() -> showMessage("请填写账号")
                mViewModel.password.get().isEmpty() -> showMessage("请填写密码")
                mViewModel.repassword.get().isEmpty() -> showMessage("请填写确认密码")
                mViewModel.password.get().length < 6 -> showMessage("密码最少6位")
                mViewModel.password.get() != mViewModel.repassword.get() -> showMessage("密码不一致")
                else -> requestLoginRegisterViewModel.registerAndLogin(
                    mViewModel.username.value,
                    mViewModel.password.get()
                )
            }
        }

        var onCheckedChangeListener1 = CompoundButton.OnCheckedChangeListener { _, isChecked ->
            mViewModel.isShowPwd.set(isChecked)
        }
        var onCheckedChangeListener2 = CompoundButton.OnCheckedChangeListener { _, isChecked ->
            mViewModel.isShowPwd2.set(isChecked)
        }
    }


}