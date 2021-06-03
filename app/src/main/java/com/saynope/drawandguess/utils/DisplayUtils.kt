package com.saynope.drawandguess.utils

import android.content.Context

object DisplayUtils{
    fun getScreenWidth(context:Context): Int {
        val displayMetrics = context.resources.displayMetrics
        return displayMetrics.widthPixels
    }

    fun dp2px(context: Context,dp:Float):Int{
        val displayMetrics = context.resources.displayMetrics
        return (dp*displayMetrics.density).toInt()
    }

    fun px2dp(context: Context,px: Int):Float{
        val displayMetrics = context.resources.displayMetrics
        return px.toFloat()/displayMetrics.density
    }
}