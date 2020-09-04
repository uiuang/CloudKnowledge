package com.uiuang.cloudknowledge.bean

data class ChildrenBean (
    var courseId: Int = 0,
    val id: Int = 0,
    val name: String? = null,
    val order: Int = 0,
    val parentChapterId: Int = 0,
    val userControlSetTop: Boolean = false,
    val visible: Int = 0,
    val children: List<*>? = null
){
    /**
     * children : []
     * courseId : 13
     * id : 60
     * name : Android Studio相关
     * order : 1000
     * parentChapterId : 150
     * userControlSetTop : false
     * visible : 1
     */
}