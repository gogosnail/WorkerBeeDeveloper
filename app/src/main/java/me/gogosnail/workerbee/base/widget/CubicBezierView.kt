package me.gogosnail.workerbee.base.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import me.gogosnail.workerbee.R
import me.gogosnail.workerbee.base.extends.dip
import me.gogosnail.workerbee.base.extends.dipInt

/** Created by max on 2018/12/27.<br/>
 */
class CubicBezierView : View {
    private val paint = Paint()
    val POINT_SIZE = 16.dip
    val LINE_WIDTH = 1.dip
    val BEZIER_LINE_WIDTH = 300.dipInt
    var paintColor = Color.RED
    val start = PointF()
    val control1 = PointF()
    val control2 = PointF()
    val end = PointF()
    var dataChangeListener: OnDataChangeListener? = null
    //    var touchDownPoint: PointF? = null
    var touchSlop = 3.dipInt
    var touchDownPoints = hashMapOf<Int, PointF>()

    constructor(context: Context?) : super(context) {
        initView()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView()
    }

    private fun initView() {
        touchSlop = ViewConfiguration.get(context).scaledTouchSlop
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event ?: return false
        val index = event.actionIndex
        val action = event.action.and(MotionEvent.ACTION_MASK)
        when (action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_POINTER_DOWN -> {
                val x = event.getX(index)
                val y = event.getY(index)
                val point = findTouchPoint(x, y)
                if (point != null) {
                    touchDownPoints[index] = point
                }
                if (touchDownPoints.isNotEmpty()) return true
            }
            MotionEvent.ACTION_MOVE -> {
                val count = event.pointerCount
                touchDownPoints.keys.forEach { index ->
                    if (index < count) {
                        val x = event.getX(index)
                        val y = event.getY(index)
                        val point = touchDownPoints[index]
                        point?.x = x
                        point?.y = y
//                    }
                        onDataChange()
                    }
                }
                return true
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_POINTER_UP -> {
                if (touchDownPoints.containsKey(index)) {
                    val point = touchDownPoints[index]
                    val x = event.getX(index)
                    val y = event.getY(index)
                    point?.x = x
                    point?.y = y
                    touchDownPoints.remove(index)
                    onDataChange()
                    return true
                }
            }
            MotionEvent.ACTION_CANCEL -> {
                touchDownPoints.clear()
            }

        }
        return false
    }

    private fun findTouchPoint(x: Float, y: Float): PointF? {
        if (inTouchNearPoint(x, y, control1)) {
            return control1
        } else if (inTouchNearPoint(x, y, control2)) {
            return control2
        }
        return null
    }

    private fun inTouchNearPoint(x: Float, y: Float, point: PointF): Boolean {
        if (Math.abs(x - point.x) <= POINT_SIZE && Math.abs(y - point.y) <= POINT_SIZE) {
            return true
        }
        return false
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        initPoints()
    }

    private fun initPoints() {
        val centerX = width / 2f
        val centerY = height / 2f
        start.x = centerX - BEZIER_LINE_WIDTH/2
        start.y = centerY
        end.x = centerX + BEZIER_LINE_WIDTH/2
        end.y = centerY
        control1.x = start.x
        control1.y = centerY + 200
        control2.x = end.x
        control2.y = centerY + 200
        onDataChange()
        touchDownPoints.clear()
    }

    fun onDataChange() {
        invalidate()
        dataChangeListener?.onChange()
    }

    override fun onDraw(canvas: Canvas?) {
        canvas ?: return
        super.onDraw(canvas)
        drawLine(canvas, start, control1)
        drawLine(canvas, control1, control2)
        drawLine(canvas, control2, end)
        drawBezier(canvas)
        drawPoint(canvas, start)
        drawPoint(canvas, control1)
        drawPoint(canvas, control2)
        drawPoint(canvas, end)

        val path = Path()

        val control1 = PointF((- 0.1 * width).toFloat(), (50).dip)
        val control2 = PointF((width + 0.1 * width).toFloat(), (50).dip)
        val end = PointF(width.toFloat(), 0f)
        path.cubicTo(control1.x, control1.y, control2.x, control2.y, end.x, end.y)
        canvas.drawPath(path, paint)
    }

    private fun drawPoint(canvas: Canvas, point: PointF) {
        paint.strokeWidth = POINT_SIZE
        paint.style = Paint.Style.STROKE
        paint.color = context.resources.getColor(R.color.accent)
        canvas.drawPoint(point.x, point.y, paint)
        paint.alpha = 150
        paint.textSize = 12.dip
        paint.strokeWidth = 0f
        paint.style = Paint.Style.FILL
        canvas.drawText("(${point.x.toInt()},${point.y.toInt()})", point.x, point.y - 20.dipInt, paint)
        paint.alpha = 255
    }

    private fun drawLine(canvas: Canvas, point1: PointF, point2: PointF) {
        paint.strokeWidth = LINE_WIDTH
        paint.style = Paint.Style.STROKE
        paint.color = context.resources.getColor(R.color.accent)
        canvas.drawLine(point1.x, point1.y, point2.x, point2.y, paint)
    }

    private fun drawBezier(canvas: Canvas) {
        paint.color = paintColor
        paint.style = Paint.Style.FILL
        paint.strokeWidth = 3.dip
        val path = Path()
        path.moveTo(start.x, start.y)
        path.cubicTo(control1.x, control1.y, control2.x, control2.y, end.x, end.y)
        canvas.drawPath(path, paint)
    }

    interface OnDataChangeListener {
        fun onChange()
    }

}