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
import android.util.AttributeSet
import android.view.View
import android.view.accessibility.AccessibilityManager
import android.widget.Checkable
import android.widget.CompoundButton
import androidx.annotation.RequiresApi
import androidx.annotation.RestrictTo
import androidx.core.content.res.TypedArrayUtils
import androidx.preference.PreferenceViewHolder
import androidx.preference.TwoStatePreference
import com.uiuang.cloudknowledge.R
import com.uiuang.cloudknowledge.utils.SettingUtil.getColorStateList

/**
 * A [Preference] that provides checkbox widget functionality.
 *
 * This preference will store a boolean into the SharedPreferences.
 *
 *      |            Attribute           | Value Type |
 *      |:------------------------------:|:----------:|
 *      | android:summaryOff             | String     |
 *      | android:summaryOn              | String     |
 *      | android:disableDependentsState | Boolean    |
 *
 * @see TwoStatePreference
 * @see SwitchPreference
 */
@SuppressLint("RestrictedApi")
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class CheckBoxPreference @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = TypedArrayUtils.getAttr(
        context, R.attr.checkBoxPreferenceStyle,
        android.R.attr.checkBoxPreferenceStyle
    ),
    defStyleRes: Int = 0
) : TwoStatePreference(context, attrs, defStyleAttr, defStyleRes) {

    var checkBoxView: CompoundButton? = null
    override fun onBindViewHolder(holder: PreferenceViewHolder) {
        super.onBindViewHolder(holder)
        syncCheckboxView(holder.findViewById(android.R.id.checkbox))
        syncSummaryView(holder)
    }

    /**
     * @hide
     */
    @RestrictTo(RestrictTo.Scope.LIBRARY)
    override fun performClick(view: View) {
        super.performClick(view)
        syncViewIfAccessibilityEnabled(view)
    }

    private fun syncViewIfAccessibilityEnabled(view: View) {
        val accessibilityManager =
            context.getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
        if (!accessibilityManager.isEnabled) {
            return
        }
        val checkboxView =
            view.findViewById<View>(android.R.id.checkbox)
        syncCheckboxView(checkboxView)
        val summaryView =
            view.findViewById<View>(android.R.id.summary)
        syncSummaryView(summaryView)
    }


    private fun syncCheckboxView(view: View) {
        if (view is CompoundButton) {
            checkBoxView = view
            checkBoxView!!.setOnCheckedChangeListener(null)
        }
        if (view is Checkable) {
            (view as Checkable).isChecked = mChecked
        }
        if (view is CompoundButton) {
            checkBoxView!!.setOnCheckedChangeListener(listener)
            checkBoxView!!.buttonTintList = getColorStateList(context)
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun setButtonColor() {
        if (checkBoxView != null) {
            checkBoxView!!.buttonTintList = getColorStateList(context)
        }
    }

    private val listener = CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
        if (!callChangeListener(isChecked)) {
            // Listener didn't like it, change it back.
            // CompoundButton will make sure we don't recurse.
            buttonView.isChecked = !isChecked
            return@OnCheckedChangeListener
        }
        this@CheckBoxPreference.isChecked = isChecked
    }


    init {
        val a = context.obtainStyledAttributes(
            attrs,
            R.styleable.CheckBoxPreference, defStyleAttr, defStyleRes
        )
        summaryOn = TypedArrayUtils.getString(
            a, R.styleable.CheckBoxPreference_summaryOn,
            R.styleable.CheckBoxPreference_android_summaryOn
        )
        summaryOff = TypedArrayUtils.getString(
            a, R.styleable.CheckBoxPreference_summaryOff,
            R.styleable.CheckBoxPreference_android_summaryOff
        )
        disableDependentsState = TypedArrayUtils.getBoolean(
            a,
            R.styleable.CheckBoxPreference_disableDependentsState,
            R.styleable.CheckBoxPreference_android_disableDependentsState, false
        )
        a.recycle()
    }
}