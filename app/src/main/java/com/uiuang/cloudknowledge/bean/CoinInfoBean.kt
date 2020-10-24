package com.uiuang.cloudknowledge.bean

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * 分享人信息
 */
@SuppressLint("ParcelCreator")
@Parcelize
data class CoinInfoBean(
    var coinCount: Int,
    var rank: String,
    var userId: Int,
    var username: String
) : Parcelable
