package com.uiuang.cloudknowledge.utils

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.StateListDrawable
import android.os.Build
import android.view.View
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import com.kingja.loadsir.core.LoadService
import com.tencent.mmkv.MMKV
import com.uiuang.cloudknowledge.R
import com.uiuang.cloudknowledge.app.App
import com.uiuang.cloudknowledge.app.http.Constants
import com.uiuang.cloudknowledge.weight.loadCallBack.LoadingCallback
import java.lang.reflect.InvocationTargetException
import kotlin.math.roundToInt


object SettingUtil {

    /**
     * 获取当前主题颜色
     */
    fun getColor(context: Context): Int {
        val kv = MMKV.mmkvWithID("app")
        val defaultColor = ContextCompat.getColor(context, R.color.colorPrimary)
        val color = kv.decodeInt("color", defaultColor)
        return if (color != 0 && Color.alpha(color) != 255) {
            defaultColor
        } else {
            color
        }

    }

    /**
     * 设置主题颜色
     */
    fun setColor(color: Int) {
        val kv = MMKV.mmkvWithID("app")
        kv.encode("color", color)
    }

    /**
     * 获取列表动画模式
     */
    fun getListMode(): Int {
        val kv = MMKV.mmkvWithID("app")
        //0 关闭动画 1.渐显 2.缩放 3.从下到上 4.从左到右 5.从右到左
        return kv.decodeInt("mode", 2)
    }

    /**
     * 设置列表动画模式
     */
    fun setListMode(mode: Int) {
        val kv = MMKV.mmkvWithID("app")
        kv.encode("mode", mode)
    }

    /**
     * 获取是否请求置顶文章
     */
    fun getRequestTop(): Boolean {
        val kv = MMKV.mmkvWithID("app")
        return kv.decodeBool("top", true)
    }

    /**
     * 首页是否开启获取指定文章
     */
    fun isNeedTop(): Boolean {
        val kv = MMKV.mmkvWithID("app")
        return kv.decodeBool("top", true)
    }

    /**
     * 设置首页是否开启获取指定文章
     */
    fun setIsNeedTop(isNeedTop: Boolean): Boolean {
        val kv = MMKV.mmkvWithID("app")
        return kv.encode("top", isNeedTop)
    }

    fun getColorStateList(context: Context): ColorStateList {
        val colors =
            intArrayOf(
                getColor(
                    context
                ), ContextCompat.getColor(context, R.color.colorGray)
            )
        val states = arrayOfNulls<IntArray>(2)
        states[0] = intArrayOf(android.R.attr.state_checked, android.R.attr.state_checked)
        states[1] = intArrayOf()
        return ColorStateList(states, colors)
    }

    fun getColorStateList(color: Int): ColorStateList {
        val colors = intArrayOf(color, ContextCompat.getColor(App.instance, R.color.colorGray))
        val states = arrayOfNulls<IntArray>(2)
        states[0] = intArrayOf(android.R.attr.state_checked, android.R.attr.state_checked)
        states[1] = intArrayOf()
        return ColorStateList(states, colors)
    }
    fun getColorStateList2(color: Int): ColorStateList {
        val colors = intArrayOf(color, ContextCompat.getColor(App.instance, R.color.colorGray))
        val states = arrayOfNulls<IntArray>(2)
        states[0] = intArrayOf(android.R.attr.state_selected, android.R.attr.state_selected)
        states[1] = intArrayOf()
        return ColorStateList(states, colors)
    }

     fun createColorStateList(defaultColor: Int, selectedColor: Int): ColorStateList {
        val states = arrayOfNulls<IntArray>(2)
        val colors = IntArray(2)
        var i = 0
        states[i] = intArrayOf()
        colors[i] = selectedColor
        i++

        // Default enabled state
        states[i] = intArrayOf()
        colors[i] = defaultColor
        i++
        return ColorStateList(states, colors)
    }


    fun getOneColorStateList(context: Context): ColorStateList {
        val colors = intArrayOf(
            getColor(
                context
            )
        )
        val states = arrayOfNulls<IntArray>(1)
        states[0] = intArrayOf()
        return ColorStateList(states, colors)
    }

    fun getOneColorStateList(color: Int): ColorStateList {
        val colors = intArrayOf(color)
        val states = arrayOfNulls<IntArray>(1)
        states[0] = intArrayOf()
        return ColorStateList(states, colors)
    }

    /**
     * 设置shap文件的颜色
     *
     * @param view
     * @param color
     */
    fun setShapeColor(view: View, color: Int) {
        val drawable = view.background as GradientDrawable
        drawable.setColor(color)
    }

    /**
     * 设置shap的渐变颜色
     */
    fun setShapeColor(view: View, color: IntArray, orientation: GradientDrawable.Orientation) {
        val drawable = view.background as GradientDrawable
        drawable.orientation = orientation//渐变方向
        drawable.colors = color//渐变颜色数组
    }

    /**
     * 设置selector文件的颜色
     *
     * @param view
     * @param yesColor
     * @param noColor
     */
    fun setSelectorColor(view: View, yesColor: Int, noColor: Int) {
        val mySelectorGrad = view.background as StateListDrawable
        try {
            val slDraClass = StateListDrawable::class.java
            val getStateCountMethod =
                slDraClass.getDeclaredMethod("getStateCount", *arrayOfNulls(0))
            val getStateSetMethod =
                slDraClass.getDeclaredMethod("getStateSet", Int::class.javaPrimitiveType)
            val getDrawableMethod =
                slDraClass.getDeclaredMethod("getStateDrawable", Int::class.javaPrimitiveType)
            val count = getStateCountMethod.invoke(mySelectorGrad) as Int//对应item标签
            for (i in 0 until count) {
                val stateSet = getStateSetMethod.invoke(
                    mySelectorGrad,
                    i
                ) as IntArray//对应item标签中的 android:state_xxxx
                if (stateSet.isEmpty()) {
                    val drawable = getDrawableMethod.invoke(
                        mySelectorGrad,
                        i
                    ) as GradientDrawable//这就是你要获得的Enabled为false时候的drawable
                    drawable.setColor(yesColor)
                } else {
                    for (j in stateSet.indices) {
                        val drawable = getDrawableMethod.invoke(
                            mySelectorGrad,
                            i
                        ) as GradientDrawable//这就是你要获得的Enabled为false时候的drawable
                        drawable.setColor(noColor)
                    }
                }
            }
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }

    }

    /**
     * 设置颜色透明一半
     * @param color
     * @return
     */
    fun translucentColor(color: Int): Int {
        val factor = 0.5f
        val alpha = (Color.alpha(color) * factor).roundToInt()
        val red = Color.red(color)
        val green = Color.green(color)
        val blue = Color.blue(color)
        return Color.argb(alpha, red, green, blue)
    }

    /**
     * 设置loading的颜色 加载布局
     */
    fun setLoadingColor(color: Int, loadSir: LoadService<Any>) {
        loadSir.setCallBack(LoadingCallback::class.java) { _, view ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                view.findViewById<ProgressBar>(R.id.loading_progress).indeterminateTintMode =
                    PorterDuff.Mode.SRC_ATOP
                view.findViewById<ProgressBar>(R.id.loading_progress).indeterminateTintList =
                    getOneColorStateList(
                        color
                    )
            }
        }
    }

    /**
     * 获取列表动画模式
     */
    fun getFindPosition(): Int {
        val kv = MMKV.mmkvWithID("app")
        return kv.decodeInt(Constants.FIND_POSITION, -1)
    }

    /**
     * 设置列表动画模式
     */
    fun setFindPosition(find_position: Int) {
        val kv = MMKV.mmkvWithID("app")
        kv.encode(Constants.FIND_POSITION, find_position)
    }

    fun removeFindPosition() {
        val kv = MMKV.mmkvWithID("app")
        kv.removeValueForKey(Constants.FIND_POSITION)
    }


}
