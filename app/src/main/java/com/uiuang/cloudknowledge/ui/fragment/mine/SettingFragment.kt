package com.uiuang.cloudknowledge.ui.fragment.mine

import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.WhichButton
import com.afollestad.materialdialogs.actions.getActionButton
import com.afollestad.materialdialogs.color.colorChooser
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.afollestad.materialdialogs.list.listItemsSingleChoice
import com.uiuang.cloudknowledge.R
import com.uiuang.cloudknowledge.app.http.NetworkApi
import com.uiuang.cloudknowledge.ext.initClose
import com.uiuang.cloudknowledge.ext.showMessage
import com.uiuang.cloudknowledge.utils.*
import com.uiuang.cloudknowledge.weight.preference.CheckBoxPreference
import com.uiuang.cloudknowledge.weight.preference.IconPreference
import com.uiuang.cloudknowledge.weight.preference.PreferenceCategory
import com.uiuang.mvvm.ext.nav


class SettingFragment : PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener {
    var toolBarView: View? = null
    private var colorPreview: IconPreference? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //这里重写根据PreferenceFragmentCompat 的布局 ，往他的根布局插入了一个toolbar
        val containerView = view.findViewById<FrameLayout>(android.R.id.list_container)
        containerView.let {
            //转为线性布局
            val linearLayout = it.parent as? LinearLayout
            linearLayout?.run {
                toolBarView = LayoutInflater.from(activity).inflate(R.layout.include_toolbar, null)
                toolBarView?.let { view ->
                    view.findViewById<Toolbar>(R.id.toolbar)?.initClose("设置") {
                        nav().navigateUp()
                    }
                    //添加到第一个
                    addView(toolBarView, 0)

                }

            }
        }
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.root_preferences)
        colorPreview = findPreference("color")
        initPreferences()
        findPreference<Preference>("exit")?.isVisible = CacheUtil.isLogin()//未登录时，退出登录需要隐藏

        findPreference<Preference>("exit")?.setOnPreferenceClickListener {
            showMessage(
                "确定退出登录吗",
                positiveButtonText = "退出",
                negativeButtonText = "取消",
                positiveAction = {
                    //清空cookie
                    NetworkApi.INSTANCE.cookieJar.clear()
                    CacheUtil.setUser(null)
//                    shareViewModel.userinfo.value = null
                    nav().navigateUp()
                })
            false
        }

        findPreference<Preference>("clearCache")?.setOnPreferenceClickListener {
            showMessage(
                "确定清理缓存吗",
                positiveButtonText = "清理",
                negativeButtonText = "取消",
                positiveAction = {
                    activity?.let {
                        CacheDataManager.clearAllCache(requireActivity())
                    }
                    initPreferences()
                })
            false
        }

        findPreference<Preference>("mode")?.setOnPreferenceClickListener {
            activity?.let { activity ->
                MaterialDialog(activity).show {
                    cancelable(false)
                    lifecycleOwner(viewLifecycleOwner)
                    listItemsSingleChoice(
                        R.array.setting_modes,
                        initialSelection = SettingUtil.getListMode()
                    ) { _, index, text ->
                        SettingUtil.setListMode(index)
                        it.summary = text
                        //通知其他界面立马修改配置
//                        shareViewModel.appAnimation.value = index
                    }
                    title(text = "设置列表动画")
                    positiveButton(R.string.confirm)
                    negativeButton(R.string.cancel)
                    getActionButton(WhichButton.POSITIVE).updateTextColor(
                        SettingUtil.getColor(
                            activity
                        )
                    )
                    getActionButton(WhichButton.NEGATIVE).updateTextColor(
                        SettingUtil.getColor(
                            activity
                        )
                    )
                }
            }

            false
        }
        findPreference<IconPreference>("color")?.setOnPreferenceClickListener {
            activity?.let { activity ->
                MaterialDialog(activity).show {
                    title(R.string.choose_theme_color)
                    colorChooser(
                        ColorUtil.ACCENT_COLORS,
                        initialSelection = SettingUtil.getColor(activity),
                        subColors = ColorUtil.PRIMARY_COLORS_SUB,
                        allowCustomArgb = true,
                        showAlphaSelector = false,
                        changeActionButtonsColor = true
                    ) { _, color ->
                        ///修改颜色
                        SettingUtil.setColor(color)
                        findPreference<PreferenceCategory>("base")?.setTitleColor(color)
                        findPreference<PreferenceCategory>("other")?.setTitleColor(color)
                        findPreference<PreferenceCategory>("about")?.setTitleColor(color)
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            findPreference<CheckBoxPreference>("top")?.setButtonColor()
                        }
                        toolBarView?.setBackgroundColor(color)
                        //通知其他界面立马修改配置
//                        shareViewModel.appColor.value = color
                    }
                    getActionButton(WhichButton.POSITIVE).updateTextColor(
                        SettingUtil.getColor(
                            activity
                        )
                    )
                    getActionButton(WhichButton.NEGATIVE).updateTextColor(
                        SettingUtil.getColor(
                            activity
                        )
                    )
                    positiveButton(R.string.done)
                    negativeButton(R.string.cancel)
                }
            }
            false
        }
    }

    private fun initPreferences() {
        requireActivity().let {

            findPreference<CheckBoxPreference>("top")?.isChecked = SettingUtil.isNeedTop()

            findPreference<Preference>("clearCache")?.summary =
                CacheDataManager.getTotalCacheSize(it)

            findPreference<Preference>("version")?.summary = "当前版本 " + AppUtils.getAppVersionName(requireActivity().packageName)

            val modes = it.resources.getStringArray(R.array.setting_modes)
            findPreference<Preference>("mode")?.summary =
                modes[SettingUtil.getListMode()]
        }
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (key == "color") {
            colorPreview?.setView()
        }
        if (key == "top") {
            SettingUtil.setIsNeedTop(sharedPreferences!!.getBoolean("top", true))
        }
    }


}