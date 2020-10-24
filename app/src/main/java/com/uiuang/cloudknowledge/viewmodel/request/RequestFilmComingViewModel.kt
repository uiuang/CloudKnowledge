package com.uiuang.cloudknowledge.viewmodel.request

import androidx.lifecycle.MutableLiveData
import com.uiuang.cloudknowledge.app.http.getMtimeTicketServer
import com.uiuang.cloudknowledge.app.state.ListDataUiState
import com.uiuang.cloudknowledge.bean.ComingFilmBean
import com.uiuang.mvvm.base.viewmodel.BaseViewModel
import com.uiuang.mvvm.ext.request

class RequestFilmComingViewModel : BaseViewModel() {
    var filmComingDataState: MutableLiveData<ListDataUiState<ComingFilmBean.MoviesBean>> =
        MutableLiveData()

    fun getComingFilm() {
        request({ getMtimeTicketServer.getComingFilm() }, {
            val listDataUiState =
                ListDataUiState(
                    isSuccess = true,
                    isRefresh = true,
                    isEmpty = false,
                    hasMore = false,
                    isFirstEmpty = false,
                    listData = it.moviecomings
                )
            filmComingDataState.postValue(listDataUiState)
        }, {
            val listDataUiState =
                ListDataUiState(
                    isSuccess = false,
                    errMessage = it.errorMsg,
                    isRefresh = true,
                    listData = arrayListOf<ComingFilmBean.MoviesBean>()
                )
            filmComingDataState.postValue(listDataUiState)
        })
    }
}