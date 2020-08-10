package com.uiuang.cloudknowledge.viewmodel.request

import androidx.lifecycle.MutableLiveData
import com.uiuang.cloudknowledge.app.http.getMtimeTicketServer
import com.uiuang.cloudknowledge.app.state.ListDataUiState
import com.uiuang.cloudknowledge.bean.FilmDetailBean
import com.uiuang.cloudknowledge.bean.FilmItemBean
import com.uiuang.mvvm.base.viewmodel.BaseViewModel
import com.uiuang.mvvm.ext.request

class RequestFilmDetailViewModel : BaseViewModel() {
    var filmDetailBean: MutableLiveData<FilmDetailBean> = MutableLiveData()

    fun getFilmDetail(movieId: Int) {

        request({ getMtimeTicketServer.getFilmDetail(movieId) }, {
            filmDetailBean.value = it
        }, {

            filmDetailBean.postValue(null)
        })

    }
}