package com.uiuang.cloudknowledge.bean.wan

import android.os.Parcelable
import com.uiuang.cloudknowledge.bean.base.ApiPagerResponse
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ShareArticleBean(
    var coinInfo: CoinInfoBean,
    var shareArticles: ApiPagerResponse<ArrayList<ArticlesBean>>
) : Parcelable