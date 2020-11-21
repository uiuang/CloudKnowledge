package com.uiuang.cloudknowledge.viewmodel.state

import com.uiuang.cloudknowledge.data.enums.TodoType
import com.uiuang.mvvm.base.viewmodel.BaseViewModel
import com.uiuang.mvvm.callback.databind.IntObservableField
import com.uiuang.mvvm.callback.databind.StringObservableField

class TodoViewModel : BaseViewModel() {
    /**
     * 标题
     */
    var todoTitle = StringObservableField()

    /**
     * 内容
     */
    var todoContent = StringObservableField()

    /**
     * 时间
     */
    var todoTime = StringObservableField()

    /***
     * 优先级
     */
    var todoLevel = StringObservableField(TodoType.TodoType1.content)

    /**
     * 优先级颜色
     */
    var todoColor = IntObservableField(TodoType.TodoType1.color)

}