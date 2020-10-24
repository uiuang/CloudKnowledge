/*
 * Copyright 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.uiuang.cloudknowledge.weight.preference

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Build.VERSION_CODES
import android.util.AttributeSet
import android.util.TypedValue
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.TypedArrayUtils
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat.CollectionItemInfoCompat
import androidx.preference.PreferenceGroup
import androidx.preference.PreferenceViewHolder
import com.uiuang.cloudknowledge.R
import com.uiuang.cloudknowledge.utils.SettingUtil

/**
 * 自定义PreferenceCategory 拓展了一下，
 * 可以设置标题颜色
 */
class PreferenceCategory @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet?,
    defStyleAttr: Int,
    defStyleRes: Int = 0
) : PreferenceGroup(context, attrs, defStyleAttr, defStyleRes) {
    var titleView: TextView? = null

    @SuppressLint("RestrictedApi")
    constructor(context: Context?, attrs: AttributeSet?) : this(
        context, attrs, TypedArrayUtils.getAttr(
            context!!, R.attr.preferenceCategoryStyle,
            R.attr.preferenceCategoryStyle
        )
    ) {
    }

    constructor(context: Context?) : this(context, null) {}

    override fun isEnabled(): Boolean {
        return false
    }

    override fun shouldDisableDependents(): Boolean {
        return !super.isEnabled()
    }

    override fun onBindViewHolder(holder: PreferenceViewHolder) {
        super.onBindViewHolder(holder)
        if (Build.VERSION.SDK_INT >= VERSION_CODES.P) {
            holder.itemView.isAccessibilityHeading = true
            titleView = holder.findViewById(android.R.id.title) as TextView
//            if (titleView == null) {
//                return
//            }
            titleView?.setTextColor(SettingUtil.getColor(context))
        } else if (Build.VERSION.SDK_INT < VERSION_CODES.LOLLIPOP) {
            // We can't safely look for colorAccent in the category layout XML below Lollipop, as it
            // only exists within AppCompat, and will crash if using a platform theme. We should
            // still try and parse the attribute here in case we are running inside
            // PreferenceFragmentCompat with an AppCompat theme, and to set the category title
            // accordingly.
            val value = TypedValue()
            if (!context.theme.resolveAttribute(R.attr.colorAccent, value, true)) {
                // Return if the attribute could not be resolved
                return
            }
            titleView = holder.findViewById(android.R.id.title) as TextView

            val fallbackColor = ContextCompat.getColor(
                context,
                R.color.preference_fallback_accent_color
            )
            // If the current color is not the fallback color we hardcode in the layout XML,
            // then this has already been handled by developers and we shouldn't override the
            // color.
            if (titleView?.currentTextColor != fallbackColor) {
                return
            }
            titleView?.setTextColor(SettingUtil.getColor(context))
        }
    }

    fun setTitleColor(color: Int) {
        if (titleView != null) {
            titleView?.setTextColor(color)
        }
    }

    @Deprecated("Super class Preference deprecated this API.")
    override fun onInitializeAccessibilityNodeInfo(info: AccessibilityNodeInfoCompat) {
        super.onInitializeAccessibilityNodeInfo(info)
        if (Build.VERSION.SDK_INT < VERSION_CODES.P) {
            val existingItemInfo = info.collectionItemInfo ?: return
            val newItemInfo = CollectionItemInfoCompat.obtain(
                existingItemInfo.rowIndex,
                existingItemInfo.rowSpan,
                existingItemInfo.columnIndex,
                existingItemInfo.columnSpan,
                true,
                existingItemInfo.isSelected
            )
            info.setCollectionItemInfo(newItemInfo)
        }
    }
}