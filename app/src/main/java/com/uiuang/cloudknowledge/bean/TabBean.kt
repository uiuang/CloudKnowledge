package com.uiuang.cloudknowledge.bean

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TabBean (
     /**
      * children : []
      * courseId : 13
      * id : 294
      * name : 完整项目
      * order : 145000
      * parentChapterId : 293
      * userControlSetTop : false
      * visible : 0
      */
      var defaultUrl: String? = "http://www.wanandroid.com/resources/image/pc/default_project_img.jpg",
     var courseId:Int = 0,
     var id :Int= 0,
     var name: String = "",
     var order:Int = 0,
     var parentChapterId:Int = 0,
     var visible:Int = 0,
     var children: MutableList<ChildrenBean> = arrayListOf(),
     var userControlSetTop:Boolean = false
): Parcelable