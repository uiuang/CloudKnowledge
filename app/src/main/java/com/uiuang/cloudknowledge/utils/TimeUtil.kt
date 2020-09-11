package com.uiuang.cloudknowledge.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

object TimeUtil {
    /**
     * 获取当前日期
     */
    @SuppressLint("SimpleDateFormat")
    fun getData(): String {
        val sDateFormat = SimpleDateFormat("yyyy-MM-dd")
        return sDateFormat.format(Date())
    }
}