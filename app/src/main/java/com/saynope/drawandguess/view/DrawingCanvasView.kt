package com.saynope.drawandguess.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.saynope.drawandguess.data.CacheDrawPath
import com.saynope.drawandguess.data.DrawPath
import com.saynope.drawandguess.data.DrawPathPoint
import com.saynope.drawandguess.utils.DisplayUtils

class DrawingCanvasView @JvmOverloads constructor(
    context: Context,
    attributes: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attributes, defStyleAttr) {

    private var path = Path()
    private var cachePath = arrayListOf<DrawPathPoint>()
    private val paint by lazy {
        Paint().also {
            it.color = Color.BLACK
            it.style = Paint.Style.STROKE
            it.strokeWidth = 1f
        }
    }

    private val cachePaint by lazy {
        Paint().also {
            it.color = Color.BLACK
            it.style = Paint.Style.STROKE
            it.strokeWidth = 1f
        }
    }

    private val fillPaint by lazy {
        Paint().also {
            it.color = Color.WHITE
            it.style = Paint.Style.STROKE
            it.strokeWidth = 1f
        }
    }

    private val cachePathList = arrayListOf<CacheDrawPath>()

    private var cachePencilColor = Color.BLACK

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        setMeasuredDimension(widthSize, heightSize)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (!isEnabled) {
            return false
        }
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                cachePath.clear()
                path = Path()
                path.moveTo(event.x, event.y)
                cachePath.add(DrawPathPoint(event.x, event.y))
            }

            MotionEvent.ACTION_MOVE -> {
                path.lineTo(
                    event.x,
                    event.y
                )
                invalidate()
                cachePath.add(DrawPathPoint(event.x, event.y))
            }
            MotionEvent.ACTION_UP -> {
                val element = DrawPath(cachePath, paint.strokeWidth, paint.color)
                drawOnPathCallBack?.invoke(element)
                cachePathList.add(CacheDrawPath(path,paint.strokeWidth,paint.color))
            }
        }
        return true
    }



    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawColor(fillPaint.color)
        cachePathList.forEach {
            run {

                cachePaint.strokeWidth = it.width
                cachePaint.color = it.color

                canvas?.drawPath(it.path, cachePaint)
            }
        }
        canvas?.drawPath(path, paint)
    }

    fun setPencilWidth(widthDp: Float) {
        paint.strokeWidth = DisplayUtils.dp2px(context, widthDp).toFloat()
        paint.color = cachePencilColor
    }

    fun setColor(color: Int) {
        paint.color = color
        cachePencilColor = color
    }

    fun fillCanvasWithColor(color: Int) {
        fillPaint.color = color
        cachePathList.clear()
        path.reset()
        invalidate()
    }

    fun startEraser(width: Float) {
        paint.strokeWidth = DisplayUtils.dp2px(context, width).toFloat()
        cachePencilColor = paint.color
        paint.color = fillPaint.color
    }

    fun addOnDrawPathFinishCallback(block: (pathPoints: DrawPath) -> Unit) {
        this.drawOnPathCallBack = block
    }

    fun onReceiveNetDrawPath(it: DrawPath) {
        val path = Path()
        for (i in it.path.indices) {
            if (i == 0) {
                path.moveTo(it.path[0].x, it.path[0].y)
            } else {
                path.lineTo(it.path[i].x, it.path[i].y)
            }
        }
        cachePathList.add(CacheDrawPath(path,it.width,it.color))
        invalidate()
    }

    private var drawOnPathCallBack: ((pathPoints: DrawPath) -> Unit)? = null

}