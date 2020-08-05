package com.uiuang.cloudknowledge.viewmodel.request

import androidx.lifecycle.MutableLiveData
import com.uiuang.cloudknowledge.app.http.getMtimeTicketServer
import com.uiuang.cloudknowledge.app.state.ListDataUiState
import com.uiuang.cloudknowledge.bean.FilmItemBean
import com.uiuang.mvvm.base.viewmodel.BaseViewModel
import com.uiuang.mvvm.ext.request

/**
 * 热映榜
 */
class RequestFilmShowingViewModel : BaseViewModel() {
    var filmShowingDataState: MutableLiveData<ListDataUiState<FilmItemBean>> = MutableLiveData()

    fun getHotFilm() {
        request({ getMtimeTicketServer.getHotFilm() }, {
            val listDataUiState =
                ListDataUiState(
                    isSuccess = true,
                    isRefresh = true,
                    isEmpty = it.isEmpty(),
                    hasMore = false,
                    isFirstEmpty = it.isEmpty(),
                    listData = it.ms
                )
            filmShowingDataState.postValue(listDataUiState)
        }, {
            val listDataUiState =
                ListDataUiState(
                    isSuccess = false,
                    errMessage = it.errorMsg,
                    isRefresh = true,
                    listData = arrayListOf<FilmItemBean>()
                )
            filmShowingDataState.postValue(listDataUiState)
        })
    }
}