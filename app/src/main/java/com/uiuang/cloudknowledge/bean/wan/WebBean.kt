package com.uiuang.cloudknowledge.bean.wan

import android.os.Parcelable
import com.uiuang.cloudknowledge.data.enums.CollectType
import kotlinx.android.parcel.Parcelize

@Parcelize
data class WebBean(
    var id: Int,
    var collect: Boolean,//是否收藏
    var title: String,
    var url: String,
    var collectType: Int = CollectType.Article.type
) : Parcelable