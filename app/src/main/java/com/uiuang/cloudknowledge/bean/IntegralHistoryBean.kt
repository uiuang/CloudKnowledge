package com.uiuang.cloudknowledge.bean

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class IntegralHistoryBean(
    var coinCount: Int,
    var date: Long,
    var desc: String,
    var id: Int,
    var type: Int,
    var reason: String,
    var userId: Int,
    var userName: String) : Parcelable