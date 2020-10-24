package com.uiuang.cloudknowledge.bean

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ShareArticleBean(
    var coinInfo: CoinInfoBean,
    var shareArticles: ApiPagerResponse<ArrayList<ArticlesBean>>
) :Parcelable