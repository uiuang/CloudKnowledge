package com.uiuang.cloudknowledge.app.base

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.preference.*

import androidx.recyclerview.widget.RecyclerView


abstract class BasePreferenceFragment : PreferenceFragmentCompat() {
    private fun setAllPreferencesToAvoidHavingExtraSpace(preference: Preference) {
        preference.isIconSpaceReserved = false
        if (preference is PreferenceGroup)
            for (i in 0 until preference.preferenceCount) {
                setAllPreferencesToAvoidHavingExtraSpace(preference.getPreference(i))
            }
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        if (preferenceScreen != null)
            setAllPreferencesToAvoidHavingExtraSpace(preferenceScreen)
        super.setPreferenceScreen(preferenceScreen);
    }

    override fun onCreateAdapter(preferenceScreen: PreferenceScreen?): RecyclerView.Adapter<*> {
        return object : PreferenceGroupAdapter(preferenceScreen) {
            @SuppressLint("RestrictedApi")
            override fun onPreferenceHierarchyChange(preference: Preference) {
                if (preferenceScreen != null)
                    setAllPreferencesToAvoidHavingExtraSpace(preference)
                super.onPreferenceHierarchyChange(preference)
            }
        }
    }


}