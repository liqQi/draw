package com.saynope.drawandguess.utils

import android.content.Context
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.view.Gravity
import com.saynope.drawandguess.R

object DrawableFactory {

    fun createPencilDrawable(context: Context, pencilWidth: Float): Drawable {
        val out = GradientDrawable()
        out.shape = GradientDrawable.OVAL
        val dp24 = DisplayUtils.dp2px(context, 24f)
        out.setSize(dp24, dp24)
        out.setStroke(DisplayUtils.dp2px(context, 1f), context.getColor(R.color.colorAccent))


        val inner = GradientDrawable()
        inner.shape = GradientDrawable.OVAL
        inner.setSize(
            DisplayUtils.dp2px(context, pencilWidth),
            DisplayUtils.dp2px(context, pencilWidth)
        )
        inner.setColor(context.getColor(R.color.colorAccent))

        val layerDrawable = LayerDrawable(arrayOf(out, inner))
        layerDrawable.setLayerGravity(0,Gravity.CENTER)
        layerDrawable.setLayerGravity(1,Gravity.CENTER)
        return layerDrawable
    }

}