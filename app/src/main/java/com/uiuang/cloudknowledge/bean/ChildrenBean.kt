package com.uiuang.cloudknowledge.bean

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ChildrenBean(
    var courseId: Int = 0,
    val id: Int = 0,
    val name: String = "",
    val order: Int = 0,
    val parentChapterId: Int = 0,
    val userControlSetTop: Boolean = false,
    val visible: Int = 0
//    val children: List<*>? = null
) : Parcelable