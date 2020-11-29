package com.uiuang.cloudknowledge.viewmodel.request

import androidx.lifecycle.MutableLiveData
import com.uiuang.cloudknowledge.app.http.getWanAndroidServer
import com.uiuang.cloudknowledge.app.state.ListDataUiState
import com.uiuang.cloudknowledge.app.state.UpdateUiState
import com.uiuang.cloudknowledge.bean.wan.TodoResponse
import com.uiuang.mvvm.base.viewmodel.BaseViewModel
import com.uiuang.mvvm.ext.request

class RequestTodoViewModel : BaseViewModel() {

    var pageNo = 1

    //列表集合数据
    var todoDataState = MutableLiveData<ListDataUiState<TodoResponse>>()

    //删除的回调数据
    var delDataState = MutableLiveData<UpdateUiState<Int>>()

    //完成的回调数据
    var doneDataState = MutableLiveData<UpdateUiState<Int>>()

    //添加修改的回调数据
    var updateDataState = MutableLiveData<UpdateUiState<Int>>()


    fun getTodoData(isRefresh: Boolean) {
        if (isRefresh) {
            pageNo = 1
        }
        request({ getWanAndroidServer.getTodoData(pageNo) }, {
            //请求成功
            pageNo++
            val listDataUiState =
                ListDataUiState(
                    isSuccess = true,
                    isRefresh = isRefresh,
                    isEmpty = it.isEmpty(),
                    hasMore = it.hasMore(),
                    isFirstEmpty = isRefresh && it.isEmpty(),
                    listData = it.datas
                )
            todoDataState.value = listDataUiState
        }, {
            //请求失败
            val listDataUiState =
                ListDataUiState(
                    isSuccess = false,
                    errMessage = it.errorMsg,
                    isRefresh = isRefresh,
                    listData = arrayListOf<TodoResponse>()
                )
            todoDataState.value = listDataUiState
        })
    }

    fun delTodo(id: Int, position: Int) {
        request({ getWanAndroidServer.deleteTodo(id) }, {
            val uistate = UpdateUiState(isSuccess = true, data = position)
            delDataState.value = uistate
        }, {
            val uistate = UpdateUiState(isSuccess = false, data = position, errorMsg = it.errorMsg)
            delDataState.value = uistate
        }, isShowDialog = true)
    }

    fun doneTodo(id: Int, position: Int) {
        request({ getWanAndroidServer.doneTodo(id, 1) }, {
            val uistate = UpdateUiState(isSuccess = true, data = position)
            doneDataState.value = uistate
        }, {
            val uistate = UpdateUiState(isSuccess = false, data = position, errorMsg = it.errorMsg)
            doneDataState.value = uistate
        }, isShowDialog = true)
    }

    fun addTodo(todoTitle: String, todoContent: String, todoTime: String, todoLeve: Int) {
        request({
            getWanAndroidServer.addTodo(todoTitle, todoContent, todoTime, 0, todoLeve)
        }, {
            val uistate = UpdateUiState(isSuccess = true, data = 0)
            updateDataState.value = uistate
        }, {
            val uistate = UpdateUiState(isSuccess = false, data = 0, errorMsg = it.errorMsg)
            updateDataState.value = uistate
        }, isShowDialog = true)
    }

    fun updateTodo(
        id: Int,
        todoTitle: String,
        todoContent: String,
        todoTime: String,
        todoLeve: Int
    ) {
        request({
            getWanAndroidServer.updateTodo(todoTitle, todoContent, todoTime, 0, todoLeve, id)
        }, {
            val uistate = UpdateUiState(isSuccess = true, data = 0)
            updateDataState.value = uistate
        }, {
            val uistate = UpdateUiState<Int>(isSuccess = false, errorMsg = it.errorMsg)
            updateDataState.value = uistate

        }, isShowDialog = true)
    }
}