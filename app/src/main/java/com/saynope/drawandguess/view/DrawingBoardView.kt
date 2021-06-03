package com.saynope.drawandguess.view

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.saynope.drawandguess.R
import com.saynope.drawandguess.adapter.ColorAdapter
import com.saynope.drawandguess.data.DrawPath
import com.saynope.drawandguess.databinding.ViewDrawingBoardBinding
import com.saynope.drawandguess.utils.DisplayUtils
import com.saynope.drawandguess.utils.DrawableFactory

class DrawingBoardView @JvmOverloads constructor(
    context: Context,
    attributes: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attributes, defStyleAttr) {
    private var bind: ViewDrawingBoardBinding

    init {
        LayoutInflater.from(context).inflate(R.layout.view_drawing_board, this, true)
        bind = ViewDrawingBoardBinding.bind(this)
        bind.pencil.setOnClickListener { _ ->
            showSelectPencilMenu { width -> bind.drawingCanvas.setPencilWidth(width + 1f) }
        }

        bind.colorPicker.setOnClickListener { _ ->
            showSelectColorDialog { color -> bind.drawingCanvas.setColor(color) }
        }

        bind.paintBucket.setOnClickListener { _ ->
            showSelectColorDialog { color -> bind.drawingCanvas.fillCanvasWithColor(color) }
        }

        bind.eraser.setOnClickListener { _ ->
            showSelectPencilMenu { width -> bind.drawingCanvas.startEraser(width + 1f) }
        }
    }

    private fun showSelectColorDialog(block: (color: Int) -> Unit) {
        if(!isEnabled){
            return
        }
        val popWindow = PopupWindow()
        val root = FrameLayout(context)
        val widthPixels = DisplayUtils.getScreenWidth(context)
        val rootParams =
            ViewGroup.LayoutParams(widthPixels, DisplayUtils.dp2px(context, 60f))
        root.layoutParams = rootParams
        val recyclerView = RecyclerView(context)
        val layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        )
        root.addView(recyclerView, layoutParams)
        recyclerView.layoutManager = GridLayoutManager(context, 8)
        val colorAdapter = ColorAdapter()
        recyclerView.adapter = colorAdapter
        colorAdapter.onColorSelected = { color ->
            block.invoke(color)
            popWindow.dismiss()
        }
        popWindow.contentView = root
        popWindow.width = rootParams.width
        popWindow.height = rootParams.height
        popWindow.isTouchable = true
        popWindow.isOutsideTouchable = true
        popWindow.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        popWindow.showAsDropDown(bind.tools, 0, 0)
    }

    private fun showSelectPencilMenu(block: (width: Int) -> Unit) {
        if(!isEnabled){
            return
        }
        val popWindow = PopupWindow()
        val root = LinearLayout(context)
        root.orientation = LinearLayout.HORIZONTAL
        val widthPixels = DisplayUtils.getScreenWidth(context)

        val rootParams =
            ViewGroup.LayoutParams(widthPixels, DisplayUtils.dp2px(context, 30f))
        root.layoutParams = rootParams
        for (index in 0..5) {
            val imageView = ImageView(context)
            val childParams = LinearLayout.LayoutParams(
                DisplayUtils.dp2px(context, 24f),
                DisplayUtils.dp2px(context, 24f)
            )
            childParams.gravity = Gravity.CENTER_VERTICAL
            childParams.width = 0
            childParams.weight = 1f
            imageView.layoutParams = childParams
            imageView.setImageDrawable(DrawableFactory.createPencilDrawable(context, index + 1f))
            imageView.foreground = AppCompatResources.getDrawable(context,R.drawable.draw_util_forground)
            imageView.tag = index
            imageView.setOnClickListener {
                run {
                    val tag = it.tag
                    if (tag is Int) {
                        block.invoke(tag)
                    }
                    popWindow.dismiss()
                }
            }
            root.addView(imageView)
        }

        popWindow.contentView = root
        popWindow.width = rootParams.width
        popWindow.height = rootParams.height
        popWindow.isTouchable = true
        popWindow.isOutsideTouchable = true
        popWindow.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        popWindow.showAsDropDown(bind.tools, 0, 0)
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        bind.drawingCanvas.isEnabled = enabled
    }

    fun addOnDrawPathFinishCallback(block:(pathPoints:DrawPath)->Unit){
        bind.drawingCanvas.addOnDrawPathFinishCallback(block)
    }

    fun onReceiveNetDrawPath(it: DrawPath) {
        bind.drawingCanvas.onReceiveNetDrawPath(it)
    }
}