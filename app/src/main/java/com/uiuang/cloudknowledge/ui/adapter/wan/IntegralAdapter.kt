package com.uiuang.cloudknowledge.ui.adapter.wan

import androidx.core.content.ContextCompat
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.uiuang.cloudknowledge.R
import com.uiuang.cloudknowledge.bean.IntegralBean
import com.uiuang.cloudknowledge.utils.SettingUtil

class IntegralAdapter(data: ArrayList<IntegralBean>) :
    BaseQuickAdapter<IntegralBean, BaseViewHolder>(R.layout.item_integral, data) {

    private var rankNum = -1

    constructor(data: ArrayList<IntegralBean>, rank: Int) : this(data) {
        this.rankNum = rank
    }

    override fun convert(holder: BaseViewHolder, item: IntegralBean) {
        //赋值
        item.run {
            if (rankNum == holder.adapterPosition + 1) {
                holder.setTextColor(R.id.item_integral_rank, SettingUtil.getColor(context))
                holder.setTextColor(
                    R.id.item_integral_name,
                    SettingUtil.getColor(context)
                )
                holder.setTextColor(
                    R.id.item_integral_count,
                    SettingUtil.getColor(context)
                )
            } else {
                holder.setTextColor(
                    R.id.item_integral_rank,
                    ContextCompat.getColor(context, R.color.colorBlack333)
                )
                holder.setTextColor(
                    R.id.item_integral_name,
                    ContextCompat.getColor(context, R.color.colorBlack666)
                )
                holder.setTextColor(
                    R.id.item_integral_count,
                    ContextCompat.getColor(context, R.color.textHint)
                )
            }
            holder.setText(R.id.item_integral_rank, "${holder.adapterPosition + 1}")
            holder.setText(R.id.item_integral_name, username)
            holder.setText(R.id.item_integral_count, coinCount.toString())
        }
    }

}