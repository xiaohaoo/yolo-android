package com.xiaohaoo.yolo.ui.activity

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.os.SystemClock
import android.util.AttributeSet
import android.util.Size
import android.view.View
import com.xiaohaoo.yolo.util.DetectorUtils
import kotlin.math.max
import kotlin.math.min

class OverlayView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private var boundingBoxList: List<DetectorUtils.Companion.BoundingBox>? = null

    private var scaleFactor = Pair(1.0f, 1.0f)

    private var inputScaleFactor = 1.0f

    private var inputSize = Size(0, 0)

    private var textBackgroundPaint = Paint().apply {
        color = Color.GREEN
        style = Paint.Style.FILL
        textSize = 50f
    }

    private var paint = Paint().apply {
        color = Color.GREEN
        strokeWidth = 5F
        style = Paint.Style.STROKE
    }
    private var textPaint = Paint().apply {
        color = Color.RED
        style = Paint.Style.FILL
        textSize = 40f
    }

    private var currentTime = SystemClock.uptimeMillis()

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        canvas.drawText("延迟: ${SystemClock.uptimeMillis() - currentTime} ms", 40.0f, 120.0f, textPaint)
        currentTime = SystemClock.uptimeMillis()
        val bounds = Rect()
        boundingBoxList?.forEach {
            val left = it.x1 * scaleFactor.first * inputSize.width * inputScaleFactor
            val right = it.x2 * scaleFactor.first * inputSize.width * inputScaleFactor
            val top = it.y1 * scaleFactor.second * inputSize.height * inputScaleFactor
            val bottom = it.y2 * scaleFactor.second * inputSize.height * inputScaleFactor
            canvas.drawRect(left, top, right, bottom, paint)
            val text = LABELS[it.cls]
            textBackgroundPaint.getTextBounds(text, 0, text.length, bounds)
            canvas.drawRect(
                left,
                top,
                left + bounds.width() + BOUNDING_RECT_TEXT_PADDING,
                top + bounds.height() + BOUNDING_RECT_TEXT_PADDING,
                textBackgroundPaint
            )
            canvas.drawText(text, left, top + bounds.height(), textPaint)
        }
    }


    fun event(inputSize: Size, imageSize: Size, boundingBoxList: List<DetectorUtils.Companion.BoundingBox>?) {
        this.inputScaleFactor = 1.0f / min(inputSize.width.toFloat() / imageSize.width.toFloat(), inputSize.height.toFloat() / imageSize.height.toFloat())
        val s = max(width.toFloat() / imageSize.width, height.toFloat() / imageSize.height)
        this.scaleFactor = Pair(s, s)
        this.inputSize = inputSize
        this.boundingBoxList = boundingBoxList
        invalidate()
    }

    companion object {
        private const val BOUNDING_RECT_TEXT_PADDING = 8
        private const val TAG = "MainActivity"
        lateinit var LABELS: List<String>
    }
}
