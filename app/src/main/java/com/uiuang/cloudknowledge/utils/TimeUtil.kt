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

    fun getReleaseDate(releaseDate: String): String {
        val sDateFormat = SimpleDateFormat("yyyyMMdd", Locale.CHINA)
        val dateFormat = SimpleDateFormat("yyyy年MM月dd日", Locale.CHINA)
        var date:Date = sDateFormat.parse(releaseDate)
        return dateFormat.format(date)
    }
}