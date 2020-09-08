package com.uiuang.mvvm.ext

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken



fun <T> Gson.typedToJson(src:T):String = toJson(src)

inline fun <reified T:Any> Gson.fromJson(json:String):T = fromJson(json,object : TypeToken<T>(){}.type)
