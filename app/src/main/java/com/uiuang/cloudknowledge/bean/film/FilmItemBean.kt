package com.uiuang.cloudknowledge.bean.film

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FilmItemBean(
    var aN1: String? = null,
    var aN2: String? = null,
    var actors: String? = null,
    var commonSpecial: String? = null,
    var d: String? = null,
    var dN: String? = null,
    var id: Int = 0,
    var img: String? = null,
    var is3D: Boolean = false,
    var isDMAX: Boolean = false,
    var isHot: Boolean = false,
    var isIMAX: Boolean = false,
    var isIMAX3D: Boolean = false,
    var isNew: Boolean = false,
    var movieId: Int = 0,
    var movieType: String? = null,
// 制片国家
    var locationName: String? = "",
    var r: Double,
    var rd: String? = null,
    var sC: Int = 0,
    var t: String? = null,
    var tCn: String? = null,
    var tEn: String? = null,
    var wantedCount: Int = 0,
    var year: String? = null,
    var versions: List<VersionsBean>? = null
) : Parcelable {
    @Parcelize
    data class VersionsBean(
        var version: String
    ) : Parcelable
}