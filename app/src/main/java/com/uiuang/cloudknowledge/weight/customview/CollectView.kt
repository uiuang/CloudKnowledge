package com.uiuang.cloudknowledge.weight.customview

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.uiuang.cloudknowledge.R
import com.uiuang.cloudknowledge.utils.CacheUtil
import com.uiuang.cloudknowledge.utils.VibrateUtils
import com.uiuang.mvvm.ext.nav
import com.uiuang.mvvm.ext.navigateAction


/**
 * @author CuiZhen
 */
class CollectView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RevealLayout(context, attrs, defStyleAttr), View.OnTouchListener {

    private var onCollectViewClickListener: OnCollectViewClickListener? = null

    override fun initAttr(attrs: AttributeSet) {
        super.initAttr(attrs)
        setCheckWithExpand(true)
        setUncheckWithExpand(false)
        setCheckedLayoutId(R.layout.layout_collect_view_checked)
        setUncheckedLayoutId(R.layout.layout_collect_view_unchecked)
        setAnimDuration(300)
        setAllowRevert(true)
        setOnTouchListener(this)
    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_UP -> {
                //震动一下
                VibrateUtils.vibrate(40)
                if (CacheUtil.isLogin()) {
                    onCollectViewClickListener?.onClick(this)
                } else {
                    isChecked = true
                    nav(v).navigateAction(R.id.action_global_loginFragment)
                }

            }
        }
        return false
    }

    fun setOnCollectViewClickListener(onCollectViewClickListener: OnCollectViewClickListener) {
        this.onCollectViewClickListener = onCollectViewClickListener
    }

    interface OnCollectViewClickListener {
        fun onClick(v: CollectView)
    }
}