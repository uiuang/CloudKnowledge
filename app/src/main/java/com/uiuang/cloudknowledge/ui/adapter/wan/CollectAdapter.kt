package com.uiuang.cloudknowledge.ui.adapter.wan

import android.text.TextUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.chad.library.adapter.base.BaseDelegateMultiAdapter
import com.chad.library.adapter.base.delegate.BaseMultiTypeDelegate
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.uiuang.cloudknowledge.R
import com.uiuang.cloudknowledge.bean.ArticlesBean
import com.uiuang.cloudknowledge.weight.customview.CollectView
import com.uiuang.mvvm.util.toHtml


class CollectAdapter(data: ArrayList<ArticlesBean>) :
    BaseDelegateMultiAdapter<ArticlesBean, BaseViewHolder>(data) {
    //文章类型
    private val article = 1

    //项目类型 本来打算不区分文章和项目布局用统一布局的，但是布局完以后发现差异化蛮大的，所以还是分开吧
    private val project = 2
    private var collectAction: (item: ArticlesBean, v: CollectView, position: Int) -> Unit =
        { _: ArticlesBean, _: CollectView, _: Int -> }

    init {

//        setAdapterAnimation(SettingUtil.getListMode())

        // 第一步，设置代理
        setMultiTypeDelegate(object : BaseMultiTypeDelegate<ArticlesBean>() {
            override fun getItemType(data: List<ArticlesBean>, position: Int): Int {
                //根据是否有图片 判断为文章还是项目，好像有点low的感觉。。。我看实体类好像没有相关的字段，就用了这个，也有可能是我没发现
                return if (TextUtils.isEmpty(data[position].envelopePic)) article else project
            }
        })
        // 第二步，绑定 item 类型
        getMultiTypeDelegate()?.let {
            it.addItemType(article, R.layout.item_collect_article)
            it.addItemType(project, R.layout.item_collect_project)
        }
    }

    override fun convert(holder: BaseViewHolder, item: ArticlesBean) {
        when (holder.itemViewType) {
            article -> {
                //文章布局的赋值
                item.run {
                    holder.setText(
                        R.id.item_home_author,
                        if (author!!.isEmpty()) "匿名用户" else author
                    )
                    holder.setText(R.id.item_home_content, title!!.toHtml())
                    holder.setText(R.id.item_home_type2, chapterName!!.toHtml())
                    holder.setText(R.id.item_home_date, niceDate)
                    holder.getView<CollectView>(R.id.item_home_collect).isChecked = true
                    //隐藏所有标签
                    holder.setGone(R.id.item_home_top, true)
                    holder.setGone(R.id.item_home_type1, true)
                    holder.setGone(R.id.item_home_new, true)
                }
                holder.getView<CollectView>(R.id.item_home_collect)
                    .setOnCollectViewClickListener(object : CollectView.OnCollectViewClickListener {
                        override fun onClick(v: CollectView) {
                            collectAction.invoke(item, v, holder.adapterPosition)
                        }
                    })
            }
            project -> {
                //项目布局的赋值
                item.run {
                    holder.setText(
                        R.id.item_project_author,
                        if (author!!.isEmpty()) "匿名用户" else author
                    )
                    holder.setText(R.id.item_project_title, title!!.toHtml())
                    holder.setText(R.id.item_project_content, desc!!.toHtml())
                    holder.setText(R.id.item_project_type, chapterName!!.toHtml())
                    holder.setText(R.id.item_project_date, niceDate)
                    //隐藏所有标签
                    holder.setGone(R.id.item_project_top, true)
                    holder.setGone(R.id.item_project_type1, true)
                    holder.setGone(R.id.item_project_new, true)
                    holder.getView<CollectView>(R.id.item_project_collect).isChecked = true
                    Glide.with(context).load(envelopePic)
                        .transition(DrawableTransitionOptions.withCrossFade(500))
                        .into(holder.getView(R.id.item_project_imageview))
                }
                holder.getView<CollectView>(R.id.item_project_collect)
                    .setOnCollectViewClickListener(object : CollectView.OnCollectViewClickListener {
                        override fun onClick(v: CollectView) {
                            collectAction.invoke(item, v, holder.adapterPosition)
                        }
                    })
            }
        }

    }

    fun setCollectClick(inputCollectAction: (item: ArticlesBean, v: CollectView, position: Int) -> Unit) {
        this.collectAction = inputCollectAction
    }

}