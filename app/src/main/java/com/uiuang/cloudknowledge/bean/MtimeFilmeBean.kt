package com.uiuang.cloudknowledge.bean

import com.uiuang.mvvm.network.BaseResponse

data class MtimeFilmeBean<T>(var totalComingMovie: Int,var voucherMsg:String="",var data: T):BaseResponse<T>(){
    override fun isSuccess(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getResponseData(): T =data

    override fun getResponseCode(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getResponseMsg(): String =voucherMsg

}