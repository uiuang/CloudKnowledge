package com.uiuang.cloudknowledge.weight.viewpager

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager


/**
 * @Title:
 * @Description:
 * @author zsc
 * @date 2020/8/4 19:03
 */
class HackyViewPager(context: Context, attrs: AttributeSet?) : ViewPager(context, attrs) {


    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return try {
            super.onInterceptTouchEvent(ev)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
            false
        }
    }
}