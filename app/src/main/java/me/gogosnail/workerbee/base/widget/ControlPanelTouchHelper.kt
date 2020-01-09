package me.gogosnail.workerbee.base.widget

import android.annotation.SuppressLint
import android.os.Handler
import android.view.MotionEvent
import android.view.View
import me.gogosnail.workerbee.base.extends.dipInt
import kotlin.math.abs

/** Created by max on 2019/12/16.<br/>
 */
class ControlPanelTouchHelper {

    companion object {
        const val INTERVAL_LONG_PRESS = 500L
        val TOUCH_SLOP = 2.dipInt
    }
    private var bindView:View ?= null
    private var touchX = -1
    private var touchY = -1
    private var isDragged = false
    private val handler = Handler()
    private var onMoveListener: OnMoveListener?= null

    @SuppressLint("ClickableViewAccessibility")
    fun bindControlView(view: View, onMoveListener: OnMoveListener) {
        bindView = view
        bindView?.setOnTouchListener(onTouchListener)
        this.onMoveListener = onMoveListener
    }

    @SuppressLint("ClickableViewAccessibility")
    fun removeControlView() {
        bindView?.setOnTouchListener(null)
        onMoveListener = null
        handler.removeCallbacksAndMessages(null)
    }

    private val onTouchListener: View.OnTouchListener = object : View.OnTouchListener {

        override fun onTouch(v: View?, event: MotionEvent?): Boolean {
            event ?: return false
            val x = event.rawX.toInt()
            val y = event.rawY.toInt()
            when(event.action) {
                MotionEvent.ACTION_DOWN -> {
                    isDragged = false
                    releaseTouch()
                    handler.postDelayed({
                        isDragged = true
                    }, INTERVAL_LONG_PRESS)
                }
                MotionEvent.ACTION_MOVE -> {
                    if (isDragged) {
                        if (touchX == -1 || touchY == -1) {
                            onMoveListener?.onMoveStart()
                            touchX = x
                            touchY = y
                        }
                        val diffX = x - touchX
                        val diffY = y - touchY
                        if (abs(diffX) >= TOUCH_SLOP || abs(diffY) >= TOUCH_SLOP) {
                            onMoveListener?.onMoving(diffX, diffY)
                            touchX = x
                            touchY = y
                        }
                    }
                }
                MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                    if (isDragged) {
                        onMoveListener?.onMoveEnd()
                    }
                    isDragged = false
                    releaseTouch()
                    handler?.removeCallbacksAndMessages(null)
                }
            }
            return true
        }

    }

    private fun releaseTouch() {
        touchX = -1
        touchY = -1
    }
}

interface OnMoveListener {

    fun onMoveStart()

    fun onMoving(diffX: Int, diffY: Int)

    fun onMoveEnd()
}