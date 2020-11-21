package com.uiuang.cloudknowledge.viewmodel.state

import androidx.databinding.ObservableField
import com.uiuang.cloudknowledge.bean.wan.IntegralBean
import com.uiuang.mvvm.base.viewmodel.BaseViewModel

class IntegralViewModel : BaseViewModel() {
    var integralBean = ObservableField<IntegralBean>()
}