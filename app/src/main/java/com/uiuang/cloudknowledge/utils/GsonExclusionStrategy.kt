package com.uiuang.cloudknowledge.utils

import com.google.gson.ExclusionStrategy
import com.google.gson.FieldAttributes

class GsonExclusionStrategy : ExclusionStrategy {
    override fun shouldSkipClass(clazz: Class<*>?): Boolean = false


    override fun shouldSkipField(f: FieldAttributes?): Boolean {
        if (f?.name == null) {
            return true
        }
        val name = f.name
        return !(name == "url" || name == "desc")
    }

}