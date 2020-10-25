package com.uiuang.cloudknowledge.weight.preference

import android.content.Context
import android.util.AttributeSet
import androidx.preference.Preference
import androidx.preference.PreferenceViewHolder
import com.uiuang.cloudknowledge.R
import com.uiuang.cloudknowledge.utils.SettingUtil
import com.uiuang.mvvm.util.logd


/**
 * @Author:         hegaojian
 * @CreateDate:     2019/8/12 14:23
 */

class IconPreference(context: Context, attrs: AttributeSet) : Preference(context, attrs) {

    var circleImageView: ColorCircleView? = null

    init {
        widgetLayoutResource = R.layout.item_icon_preference_preview
    }

    override fun onBindViewHolder(holder: PreferenceViewHolder?) {
        super.onBindViewHolder(holder)
        val color = SettingUtil.getColor(context)
        circleImageView = holder?.itemView?.findViewById(R.id.iv_preview)
        circleImageView?.color = color
        circleImageView?.border = color
    }

    fun setView() {

        val color = SettingUtil.getColor(context)
        color.toString().logd()
        circleImageView?.color = color
        circleImageView?.border = color
    }
}