package com.uiuang.cloudknowledge.ui.adapter.film

import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.uiuang.cloudknowledge.R
import com.uiuang.cloudknowledge.bean.FilmDetailBean
import com.uiuang.cloudknowledge.ext.imageUrl
import com.uiuang.mvvm.util.dp2px


/**
 * @Title:
 * @Description:
 * @author zsc
 * @date 2020/9/20 14:54
 */
class FilmDetailImageAdapter() : BaseQuickAdapter<FilmDetailBean.Basic.ImageList, BaseViewHolder>(
    R.layout.item_film_detail_image
) {
    override fun convert(holder: BaseViewHolder, item: FilmDetailBean.Basic.ImageList) {
        var ivImage: ImageView = holder.getView(R.id.iv_image)
        ivImage.imageUrl(item.imgUrl, context.dp2px(140), context.dp2px(100))

    }

}