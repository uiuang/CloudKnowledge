package com.uiuang.cloudknowledge.ui.adapter.wan

import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.uiuang.cloudknowledge.R
import com.uiuang.cloudknowledge.bean.ArticlesBean
import com.uiuang.cloudknowledge.bean.NavJsonBean


/**
 * @Title:
 * @Description:
 * @author zsc
 * @date 2020/9/6 11:21
 */
class NavigationContentAdapter :
    BaseQuickAdapter<NavJsonBean, BaseViewHolder>(R.layout.item_navigation_content) {
    private var navigationAction: (item: ArticlesBean, view: View) -> Unit =
        { _: ArticlesBean, _: View -> }
//    init {
//        setAdapterAnimation(SettingUtil.getListMode())
//    }


    override fun convert(holder: BaseViewHolder, item: NavJsonBean) {
        holder.setText(R.id.tv_title, item.name)
        val rvContent: RecyclerView = holder.getView(R.id.rv_content)
        rvContent.layoutManager = GridLayoutManager(context, 2)
        val navigationChildAdapter = NavigationContentChildAdapter(item.articles)
        rvContent.adapter = navigationChildAdapter
        navigationChildAdapter.setOnItemClickListener { _, view, position ->
            val articlesBean: ArticlesBean = navigationChildAdapter.data[position]
            navigationAction.invoke(articlesBean,view)
        }
    }

    fun setNavigationAction(inputNavigationAction: (item: ArticlesBean, view: View) -> Unit) {
        this.navigationAction = inputNavigationAction
    }

}