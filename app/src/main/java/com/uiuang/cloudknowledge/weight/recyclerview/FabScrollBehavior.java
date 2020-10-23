package com.uiuang.cloudknowledge.weight.recyclerview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * @author zsc
 * @Title:
 * @Description:
 * @date 2020/8/3 23:08
 */


public class FabScrollBehavior extends FloatingActionButton.Behavior {

    // 因为需要在布局xml中引用，所以必须实现该构造方法
    public FabScrollBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }



    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull FloatingActionButton child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {

        return axes == ViewCompat.SCROLL_AXIS_VERTICAL||super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, axes, type);
    }



    @Override
    public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull FloatingActionButton child, @NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type, @NonNull int[] consumed) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type, consumed);
//        if (dyConsumed > 0) { // 向下滑动
//            animateOut(child);
//        } else if (dyConsumed < 0) { // 向上滑动
//            animateIn(child);
//        }
        if (dyConsumed > 0 && child.getVisibility() == View.VISIBLE) {
            animateOut(child);
        } else if (dyConsumed < 0 && child.getVisibility() != View.VISIBLE) {
//            child.show();
            animateIn(child);
        }

    }

    // FAB移出屏幕动画（隐藏动画）
    private void animateOut(FloatingActionButton fab) {
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
        int bottomMargin = layoutParams.bottomMargin;
        fab.animate().translationY(fab.getHeight() + bottomMargin).setInterpolator(new LinearInterpolator()).start();
    }

    // FAB移入屏幕动画（显示动画）
    private void animateIn(FloatingActionButton fab) {
        fab.animate().translationY(0).setInterpolator(new LinearInterpolator()).start();
    }

}
