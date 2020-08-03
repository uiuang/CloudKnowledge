package com.uiuang.cloudknowledge.weight.recyclerview

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import androidx.recyclerview.widget.StaggeredGridLayoutManager

/**
 * @author zsc
 * @Title:
 * @Description: 给 GridLayoutManager or StaggeredGridLayoutManager 设置间距，可设置去除首尾间距个数
 * @date 2020/8/3 21:27
 */
class GridSpaceItemDecoration(spacing: Int) : ItemDecoration() {
    /**
     * 每行个数
     */
    private var mSpanCount = 0

    /**
     * 间距
     */
    private var mSpacing: Int

    /**
     * 距屏幕周围是否也有间距
     */
    private var mIncludeEdge: Boolean

    /**
     * 头部 不显示间距的item个数
     */
    private var mStartFromSize = 0

    /**
     * 尾部 不显示间距的item个数 默认不处理最后一个item的间距
     */
    private var mEndFromSize = 1

    /**
     * 瀑布流 头部第一个整行的position
     */
    private var fullPosition = -1

    /**
     * @param spacing     item 间距
     * @param includeEdge item 距屏幕周围是否也有间距
     */
    init {
        mSpacing = spacing
        mIncludeEdge = true
    }


    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val lastPosition = state.itemCount - 1
        var position = parent.getChildAdapterPosition(view)
        if (mStartFromSize <= position && position <= lastPosition - mEndFromSize) {

            // 行
            var spanGroupIndex = -1
            // 列
            var column = 0
            // 瀑布流是否占满一行
            var fullSpan = false
            val layoutManager = parent.layoutManager
            if (layoutManager is GridLayoutManager) {
                val gridLayoutManager = layoutManager
                val spanSizeLookup = gridLayoutManager.spanSizeLookup
                val spanCount = gridLayoutManager.spanCount
                // 当前position的spanSize
                val spanSize = spanSizeLookup.getSpanSize(position)
                // 一行几个
                mSpanCount = spanCount / spanSize
                // =0 表示是最左边 0 2 4
                val spanIndex = spanSizeLookup.getSpanIndex(position, spanCount)
                // 列
                column = spanIndex / spanSize
                // 行 减去mStartFromSize,得到从0开始的行
                spanGroupIndex =
                    spanSizeLookup.getSpanGroupIndex(position, spanCount) - mStartFromSize
            } else if (layoutManager is StaggeredGridLayoutManager) {
                // 瀑布流获取列方式不一样
                val params = view.layoutParams as StaggeredGridLayoutManager.LayoutParams
                // 列
                column = params.spanIndex
                // 是否是全一行
                fullSpan = params.isFullSpan
                mSpanCount = layoutManager.spanCount
            }
            // 减掉不设置间距的position,得到从0开始的position
            position -= mStartFromSize
            if (mIncludeEdge) {
                /*
                 *示例：
                 * spacing = 10 ；spanCount = 3
                 * ---------10--------
                 * 10   3+7   6+4    10
                 * ---------10--------
                 * 10   3+7   6+4    10
                 * ---------10--------
                 */
                if (fullSpan) {
                    outRect.left = 0
                    outRect.right = 0
                } else {
                    outRect.left = mSpacing - column * mSpacing / mSpanCount
                    outRect.right = (column + 1) * mSpacing / mSpanCount
                }
                if (spanGroupIndex > -1) {
                    // grid 显示规则
                    if (spanGroupIndex < 1 && position < mSpanCount) {
                        // 第一行才有上间距
                        outRect.top = mSpacing
                    }
                } else {
                    if (fullPosition == -1 && position < mSpanCount && fullSpan) {
                        // 找到头部第一个整行的position，后面的上间距都不显示
                        fullPosition = position
                    }
                    // Stagger显示规则 头部没有整行或者头部体验整行但是在之前的position显示上间距
                    val isFirstLineStagger =
                        (fullPosition == -1 || position < fullPosition) && position < mSpanCount
                    if (isFirstLineStagger) {
                        // 第一行才有上间距
                        outRect.top = mSpacing
                    }
                }
                outRect.bottom = mSpacing
            } else {
                /*
                 *示例：
                 * spacing = 10 ；spanCount = 3
                 * --------0--------
                 * 0   3+7   6+4    0
                 * -------10--------
                 * 0   3+7   6+4    0
                 * --------0--------
                 */
                if (fullSpan) {
                    outRect.left = 0
                    outRect.right = 0
                } else {
                    outRect.left = column * mSpacing / mSpanCount
                    outRect.right = mSpacing - (column + 1) * mSpacing / mSpanCount
                }
                if (spanGroupIndex > -1) {
                    if (spanGroupIndex >= 1) {
                        // 超过第0行都显示上间距
                        outRect.top = mSpacing
                    }
                } else {
                    if (fullPosition == -1 && position < mSpanCount && fullSpan) {
                        // 找到头部第一个整行的position
                        fullPosition = position
                    }
                    // Stagger上间距显示规则
                    val isStaggerShowTop =
                        position >= mSpanCount || fullSpan && position != 0 || fullPosition != -1 && position != 0
                    if (isStaggerShowTop) {
                        // 超过第0行都显示上间距
                        outRect.top = mSpacing
                    }
                }
            }
        }
    }

    /**
     * 设置从哪个位置 开始设置间距
     *
     * @param startFromSize 一般为HeaderView的个数 + 刷新布局(不一定设置)
     */
    fun setStartFrom(startFromSize: Int): GridSpaceItemDecoration {
        mStartFromSize = startFromSize
        return this
    }

    /**
     * 设置从哪个位置 结束设置间距。默认为1，默认用户设置了上拉加载
     *
     * @param endFromSize 一般为FooterView的个数 + 加载更多布局(不一定设置)
     */
    fun setEndFromSize(endFromSize: Int): GridSpaceItemDecoration {
        mEndFromSize = endFromSize
        return this
    }

    /**
     * 设置从哪个位置 结束设置间距
     *
     * @param startFromSize 一般为HeaderView的个数 + 刷新布局(不一定设置)
     * @param endFromSize   默认为1，一般为FooterView的个数 + 加载更多布局(不一定设置)
     */
    fun setNoShowSpace(startFromSize: Int, endFromSize: Int): GridSpaceItemDecoration {
        mStartFromSize = startFromSize
        mEndFromSize = endFromSize
        return this
    }
}