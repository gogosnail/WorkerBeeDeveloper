package me.gogosnail.workerbee.box.ruler

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import me.gogosnail.workerbee.R
import me.gogosnail.workerbee.base.CommonSpKey
import me.gogosnail.workerbee.base.environment.CommonPreferenceManager
import me.gogosnail.workerbee.base.extends.dip
import kotlin.math.abs
import kotlin.math.max

/** Created by max on 2019/12/16.<br/>
 */
class RulerCaptureView @JvmOverloads constructor(context: Context,
                                                 attributeSet: AttributeSet? = null,
                                                 defStyleAttr: Int = 0) : View(context, attributeSet, defStyleAttr) {
    private var lineColor: Int = context.resources.getColor(R.color.faded_red)
    private var markColor: Int = context.resources.getColor(R.color.faded_red_two)
    private var markPaint = Paint()
    private var linePaint = Paint()
    private val linePoints = mutableListOf<LinePoint>()
    private val invalidTouchPoint = PointF(-1f, -1f)
    private val touchPoint1 = PointF()
    private val touchPoint2 = PointF()
    private val validTouchDiff = 3.dip
    private var useDip = true//使用dip作为单位
    private val density = resources.displayMetrics.density

    init {
        linePaint.color = lineColor
        linePaint.isAntiAlias = true
        linePaint.strokeWidth = 2.dip
        markPaint.textSize = 16.dip
        markPaint.color = markColor
        markPaint.isAntiAlias = true
        markPaint.strokeWidth = 1f
        releaseTouchPoint(touchPoint1)
        releaseTouchPoint(touchPoint2)
    }

    /**
     * Clear captured lines.
     */
    fun clearCapture() {
        linePoints.clear()
        invalidate()
    }

    fun startCapture() {
        linePoints.clear()
        useDip = CommonPreferenceManager.getBoolean(CommonSpKey.SP_RULER_USE_DIP_UNIT, true)
    }

    override fun onDraw(canvas: Canvas?) {
        canvas ?: return
        linePoints.forEach { line ->
            drawLine(canvas, line.first.x, line.first.y, line.second.x, line.second.y)
        }
        if (touchPoint1 != invalidTouchPoint && touchPoint2 != invalidTouchPoint) {
            drawLine(canvas, touchPoint1.x, touchPoint1.y, touchPoint2.x, touchPoint2.y)
        }
    }

    private fun drawLine(canvas: Canvas?,
                         startX: Float,
                         startY: Float,
                         endX: Float,
                         endY: Float) {
        canvas?.drawLine(startX, startY, endX, endY, linePaint)
        val di = if (useDip) density else 1f
        val with = (abs(endX - startX) / di).toInt()
        val height = (abs(endY - startY) / di).toInt()
        val unit = if (useDip) "dp" else "px"
        val desc = "[${abs(with)}$unit , ${abs(height)}$unit]"
        val textWith = markPaint.measureText(desc)
        val centerX = (startX + endX) / 2
        val centerY = (startY + endY) / 2
        val textX = max(0f, centerX - textWith / 2)
        val textY = centerY - (linePaint.fontMetrics.bottom - linePaint.fontMetrics.top) / 2f
        canvas?.drawText(desc, textX, textY, markPaint)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event ?: return false
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                setTouchPointWithDiffCheck(touchPoint1, event.x, event.y)
                releaseTouchPoint(touchPoint2)
                invalidate()
            }
            MotionEvent.ACTION_MOVE -> {
                setTouchPointWithDiffCheck(touchPoint2, event.x, event.y)
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
                if (touchPoint1 != invalidTouchPoint && touchPoint2 != invalidTouchPoint) {
                    val startP = PointF()
                    startP.set(touchPoint1)
                    val endP = PointF()
                    endP.set(touchPoint2)
                    linePoints.add(LinePoint(startP, endP))
                }
                releaseTouchPoint(touchPoint1)
                releaseTouchPoint(touchPoint2)
                invalidate()
            }
            MotionEvent.ACTION_CANCEL -> {
                releaseTouchPoint(touchPoint1)
                releaseTouchPoint(touchPoint2)
                invalidate()
            }
        }
        return true
    }

    private fun setTouchPointWithDiffCheck(pointF: PointF, touchX: Float, touchY: Float) {
        val diffX = Math.abs(pointF.x - touchX)
        val diffY = Math.abs(pointF.y - touchY)
        if (diffX >= validTouchDiff || diffY >= validTouchDiff) {
            pointF.set(touchX, touchY)
        }
    }

    private fun releaseTouchPoint(pointF: PointF) {
        pointF.set(invalidTouchPoint)
    }
}

/**
 * Ends points
 */
typealias LinePoint = Pair<PointF, PointF>