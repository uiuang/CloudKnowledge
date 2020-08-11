package com.uiuang.cloudknowledge.ui.adapter.film

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.uiuang.cloudknowledge.R
import com.uiuang.cloudknowledge.bean.Actor

class FilmActorsAdapter(data: MutableList<Actor>?) : BaseQuickAdapter<Actor, BaseViewHolder>(R.layout.item_actor_layout, data) {
    override fun convert(holder: BaseViewHolder, item: Actor) {
        holder.setText(R.id.tv_actor_roleName, item.roleName)
        holder.setText(R.id.tv_actor_name, item.name)
        holder.setText(R.id.tv_actor_nameEn, item.nameEn)
    }
}