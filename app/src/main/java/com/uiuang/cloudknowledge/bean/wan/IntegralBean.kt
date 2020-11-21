package com.uiuang.cloudknowledge.bean.wan

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class IntegralBean(
    var coinCount: Int,//当前积分
    var level: Int,
    var rank: Int,
    var userId: Int,
    var username: String
) : Parcelable
