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
/**
 * 发起添加群流程。群号：Android 云阅交流群(727379132) 的 key 为： jSdY9xxzZ7xXG55_V8OUb8ds_YT6JjAn
 * 调用 joinQQGroup(jSdY9xxzZ7xXG55_V8OUb8ds_YT6JjAn) 即可发起手Q客户端申请加群 Android 云阅交流群(727379132)
 *
 * @param key 由官网生成的key
 */
fun String.joinQQGroup(context: Context?) {
    val intent = Intent()
    intent.data =
        Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D$this")
    // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    try {
        context?.startActivity(intent)
    } catch (e: Exception) {
        // 未安装手Q或安装的版本不支持
        ("未安装手Q或安装的版本不支持~").showToastLong()
    }
}

fun String.joinQQChat(context: Context?) {
    val intent = Intent()
    intent.data =
        Uri.parse("mqqwpa://im/chat?chat_type=wpa&uin=$this")
    // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    try {
        context?.startActivity(intent)
    } catch (e: Exception) {
        // 未安装手Q或安装的版本不支持
        ("未安装手Q或安装的版本不支持~").showToastLong()
    }
}