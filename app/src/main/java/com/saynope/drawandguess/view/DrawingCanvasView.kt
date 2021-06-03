package com.saynope.drawandguess.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.saynope.drawandguess.data.DrawPath
import com.saynope.drawandguess.utils.DisplayUtils

class DrawingCanvasView @JvmOverloads constructor(
    context: Context,
    attributes: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attributes, defStyleAttr) {

    private var path = Path()
    private var flagControlInit = false
    private var cacheControlPoint = Point(0, 0)
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

    private val pathList = arrayListOf<DrawPath>()

    private var cachePencilColor = Color.BLACK

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        setMeasuredDimension(widthSize, heightSize)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if(!isEnabled){
            return false
        }
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                path = Path()
                path.moveTo(event.x, event.y)
            }

            MotionEvent.ACTION_MOVE -> {
                cacheControlPoint = Point(event.x.toInt(), event.y.toInt())
                if (!flagControlInit) {
                    flagControlInit = !flagControlInit
                } else {
                    path.quadTo(
                        cacheControlPoint.x.toFloat(),
                        cacheControlPoint.y.toFloat(),
                        event.x,
                        event.y
                    )
                    invalidate()
                }
            }

            MotionEvent.ACTION_UP -> {
                pathList.add(DrawPath(path, paint.strokeWidth, paint.color))
            }
        }
        return true
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawColor(fillPaint.color)
        pathList.forEach {
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
        pathList.clear()
        path.reset()
        invalidate()
    }

    fun startEraser(width: Float) {
        paint.strokeWidth = DisplayUtils.dp2px(context, width).toFloat()
        cachePencilColor = paint.color
        paint.color = fillPaint.color
    }
}