package com.saynope.drawandguess.utils

import com.google.gson.Gson
import java.lang.reflect.Type

object GsonUtils {

    private val gson = Gson()

    fun <T>fromJson(json:String,clazz:Class<T>):T{
        return gson.fromJson(json,clazz)
    }

    fun <T>fromJson(json:String,typeOfT: Type):T{
        return gson.fromJson(json,typeOfT)
    }

    fun <T>toJson(t:T):String{
        return gson.toJson(t)
    }
}