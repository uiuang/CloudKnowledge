package com.uiuang.cloudknowledge.weight.loadCallBack


import com.kingja.loadsir.callback.Callback
import com.uiuang.cloudknowledge.R


class EmptyCallback : Callback() {

    override fun onCreateView(): Int {
        return R.layout.layout_empty
    }

}