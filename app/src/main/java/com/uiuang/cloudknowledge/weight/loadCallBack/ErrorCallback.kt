package com.uiuang.cloudknowledge.weight.loadCallBack

import com.kingja.loadsir.callback.Callback
import com.uiuang.cloudknowledge.R


class ErrorCallback : Callback() {

    override fun onCreateView(): Int {
        return R.layout.layout_error
    }

}