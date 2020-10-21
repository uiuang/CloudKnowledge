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
import com.uiuang.cloudknowledge.databinding.FragmentRegisterBinding
import com.uiuang.cloudknowledge.ext.initClose
import com.uiuang.cloudknowledge.ext.showMessage
import com.uiuang.cloudknowledge.viewmodel.request.RequestLoginRegisterViewModel
import com.uiuang.cloudknowledge.viewmodel.state.LoginRegisterViewModel
import com.uiuang.mvvm.ext.nav
import kotlinx.android.synthetic.main.fragment_register.*


class RegisterFragment : BaseFragment<LoginRegisterViewModel,FragmentRegisterBinding>() {

    private val requestLoginRegisterViewModel: RequestLoginRegisterViewModel by viewModels()


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
                mViewModel.password2.get().isEmpty() -> showMessage("请填写确认密码")
                mViewModel.password.get().length < 6 -> showMessage("密码最少6位")
                mViewModel.password.get() != mViewModel.password2.get() -> showMessage("密码不一致")
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

    override fun layoutId(): Int =R.layout.fragment_register

    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.viewModel = mViewModel
        mDatabind.click = ProxyClick()
        toolbar.initClose("注册") {
            nav().navigateUp()
        }
    }
}