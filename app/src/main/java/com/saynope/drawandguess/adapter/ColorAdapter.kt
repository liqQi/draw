package com.saynope.drawandguess.adapter

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.saynope.drawandguess.utils.DisplayUtils

class ColorAdapter : RecyclerView.Adapter<ColorHolder>() {

    private val color = arrayOf(
        "#000000",
        "#ffffff",
        "#d50000",
        "#aa00ff",
        "#304ffe",
        "#0091ea",
        "#00bfa5",
        "#64dd17",
        "#ffd600",
        "#ff6d00",
        "#dd2c00",
        "#4a148c",
        "#004d40",
        "#827717",
        "#ffcc80",
        "#607d8b"
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorHolder {
        val view = View(parent.context)
        view.layoutParams = RecyclerView.LayoutParams(
            RecyclerView.LayoutParams.MATCH_PARENT,
            DisplayUtils.dp2px(parent.context, 30f)
        )
        return ColorHolder(view)
    }

    override fun getItemCount(): Int {
        return color.size
    }

    override fun onBindViewHolder(holder: ColorHolder, position: Int) {
        holder.itemView.background = ColorDrawable(Color.parseColor(color[position]))
        holder.itemView.tag = Color.parseColor(color[position])
        holder.itemView.setOnClickListener{v->
            run {
                val tag = v.tag
                if (tag is Int) {
                    onColorSelected?.invoke(tag)
                }
            }
        }
    }

    var onColorSelected: ((color:Int) ->Unit)? = null

}