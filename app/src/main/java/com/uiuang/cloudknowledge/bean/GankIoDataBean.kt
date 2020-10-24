package com.uiuang.cloudknowledge.bean

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


/**
 * @Title:
 * @Description:
 * @author zsc
 * @date 2020/8/2 21:42
 */
@SuppressLint("ParcelCreator")
@Parcelize
data class GankIoDataBean(
    var coinCount: Int,//当前积分
    var rank: Int,
    var userId: Int,
    var username: String
) : Parcelable

