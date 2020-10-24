package com.uiuang.cloudknowledge.utils

import android.widget.Toast
import com.uiuang.mvvm.base.appContext


/**
 * @Title:
 * @Description:
 * @author zsc
 * @date 2020/8/1 23:14
 */
fun Any.toast(): Toast {
    return Toast.makeText(appContext, this.toString(), Toast.LENGTH_SHORT).apply { show() }
}

fun Any.showToastLong(): Toast {
    return Toast.makeText(appContext, this.toString(), Toast.LENGTH_LONG).apply { show() }
}
