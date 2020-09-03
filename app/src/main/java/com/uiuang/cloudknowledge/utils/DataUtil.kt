package com.uiuang.cloudknowledge.utils

import android.content.Context
import android.text.TextUtils
import java.util.*

object DataUtil {
    /**
     * 玩安卓首页列表显示用户名
     */
    fun getHomeAuthor(isNew: Boolean, author: String?, shareName: String?): String? {
        var name = author
        if (TextUtils.isEmpty(name)) name = shareName
        if (TextUtils.isEmpty(name)) name = "匿名"
        return if (isNew) {
            " $name"
        } else {
            name
        }
    }

    /**
     * 玩安卓知识体系列表显示用户名
     */
    fun getAuthor(author: String, shareName: String): String? {
        var name = author
        if (TextUtils.isEmpty(name)) name = shareName
        return if (TextUtils.isEmpty(name)) { ""
        } else " · $name"
    }

    /**
     * 将int转为String
     */
    fun getStringValue(value: Int): String? {
        return value.toString()
    }

    /**
     * 积分增加减少
     */
    fun getAdd(value: Int): String? {
        return if (value < 0) {
            value.toString()
        } else {
            "+$value"
        }
    }


}