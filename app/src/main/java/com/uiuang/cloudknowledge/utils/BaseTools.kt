package com.uiuang.cloudknowledge.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.uiuang.cloudknowledge.app.App


/**
 * @Title:
 * @Description:
 * @author zsc
 * @date 2020/9/3 21:20
 */

fun Any.copy() {
    if (!this.toString().isNullOrEmpty()) {
        // 得到剪贴板管理器
        val cmb: ClipboardManager =
            App.instance.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        cmb.text = this.toString().trim { it <= ' ' }
        // 创建一个剪贴数据集，包含一个普通文本数据条目（需要复制的数据）
        val clipData: ClipData = ClipData.newPlainText(null, this.toString())
        // 把数据集设置（复制）到剪贴板
        cmb.setPrimaryClip(clipData)
    }
}

fun String.openLink(context:Context?) {
    val issuesUrl = Uri.parse(this)
    val intent = Intent(Intent.ACTION_VIEW, issuesUrl)
    context?.startActivity(intent)
}