package com.uiuang.cloudknowledge.ui.adapter.wan

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.uiuang.cloudknowledge.R
import com.uiuang.cloudknowledge.data.enums.TodoType
import com.uiuang.cloudknowledge.weight.preference.ColorCircleView

/**
 * 重要程度 adapter
 *
 *
 */
class PriorityAdapter(data: ArrayList<TodoType>) : BaseQuickAdapter<TodoType, BaseViewHolder>(R.layout.item_todo_dialog, data) {
    var checkType = TodoType.TodoType1.type

    constructor(data: ArrayList<TodoType>, checkType: Int) : this(data) {
        this.checkType = checkType
    }

    override fun convert(holder: BaseViewHolder, item: TodoType) {
        //赋值
        item.run {
            holder.setText(R.id.item_todo_dialog_name, item.content)
            if (checkType == item.type) {
                holder.getView<ColorCircleView>(R.id.item_todo_dialog_icon).setViewSelect(item.color)
            } else {
                holder.getView<ColorCircleView>(R.id.item_todo_dialog_icon).setView(item.color)
            }
        }
    }
}