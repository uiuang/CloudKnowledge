package com.uiuang.cloudknowledge.ui.adapter.wan

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.uiuang.cloudknowledge.R
import com.uiuang.cloudknowledge.bean.IntegralHistoryBean
import com.uiuang.cloudknowledge.utils.SettingUtil
import com.uiuang.mvvm.util.DatetimeUtil

class IntegralHistoryAdapter(data: ArrayList<IntegralHistoryBean>) :
    BaseQuickAdapter<IntegralHistoryBean, BaseViewHolder>(
        R.layout.item_integral_history, data
    ) {


    override fun convert(holder: BaseViewHolder, item: IntegralHistoryBean) {
        //赋值
        item.run {
            val descStr =
                if (desc.contains("积分")) desc.subSequence(desc.indexOf("积分"), desc.length) else ""
            holder.setText(R.id.item_integral_history_title, reason + descStr)
            holder.setText(
                R.id.item_integral_history_date,
                DatetimeUtil.formatDate(date, DatetimeUtil.DATE_PATTERN_SS)
            )
            holder.setText(R.id.item_integral_history_count, "+$coinCount")
            holder.setTextColor(R.id.item_integral_history_count, SettingUtil.getColor(context))
        }
    }
}