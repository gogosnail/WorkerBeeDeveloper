package me.gogosnail.workerbee.box.ruler

import android.content.res.Configuration
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.Region
import android.inputmethodservice.InputMethodService
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import me.gogosnail.workerbee.BaseApplication
import me.gogosnail.workerbee.R
import me.gogosnail.workerbee.base.CommonSpKey
import me.gogosnail.workerbee.base.environment.CommonPreferenceManager
import me.gogosnail.workerbee.base.extends.dipInt
import me.gogosnail.workerbee.base.utils.ReflectionUtils
import me.gogosnail.workerbee.base.utils.VibrateManager
import me.gogosnail.workerbee.base.widget.ControlPanelTouchHelper
import me.gogosnail.workerbee.base.widget.FloatingWindow
import me.gogosnail.workerbee.base.widget.OnComputeInternalInsetsListener
import me.gogosnail.workerbee.base.widget.OnMoveListener
import kotlin.math.max
import kotlin.math.min

/** Created by max on 2019/12/16.<br/>
 * 尺子的操作界面
 */
class RulerFloatingWindow : FloatingWindow() {
    companion object {
        const val OPTION_MODE_RELEASE_RULER = 0//释放捕捉
        const val OPTION_MODE_CAPTURE_RULER = 1//捕捉模式
    }

    var contentView: View? = null
    var rulerActionBar: LinearLayout? = null
    var controlPanelView: ImageView ?= null
    var rulerTv: TextView? = null
    var eraserTv: TextView? = null
    var captureView: RulerCaptureView? = null
    val highlightOptionColor = BaseApplication.INSTANCE.resources.getColor(R.color.faded_red)
    val normalOptionColor = BaseApplication.INSTANCE.resources.getColor(R.color.white)
    private var rulerMode = OPTION_MODE_RELEASE_RULER//操作模式
    private val touchRegion = Region()
    private val touchHelper = ControlPanelTouchHelper()
    private val computeInternalInsetsListener: OnComputeInternalInsetsListener = OnComputeInternalInsetsListener()
    override fun onCreateView(): View {
        windowLp.width = WindowManager.LayoutParams.MATCH_PARENT
        windowLp.height = WindowManager.LayoutParams.MATCH_PARENT
        contentView = LayoutInflater.from(context).inflate(R.layout.layout_ruler_capture_window, null)
        rulerActionBar = contentView?.findViewById(R.id.ll_ruler_action)
        controlPanelView = contentView?.findViewById(R.id.iv_control)
        rulerTv = contentView?.findViewById(R.id.tv_ruler)
        eraserTv = contentView?.findViewById(R.id.tv_eraser)
        captureView = contentView?.findViewById(R.id.capture_view)
        windowLp.alpha = 0.7f
        updateRulerMode(OPTION_MODE_RELEASE_RULER)
        initActions()
        rulerActionBar?.addOnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
            touchRegion.set(v?.left ?: 0, v?.top ?: 0, v?.right
                    ?: 0, v?.bottom ?: 0)
            updateTouchRegion()
        }
        rulerActionBar?.setOnClickListener {

        }
        return contentView!!
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
    }

    private fun initActions() {
        rulerTv?.setOnClickListener {
            updateRulerMode(if (OPTION_MODE_CAPTURE_RULER == rulerMode) OPTION_MODE_RELEASE_RULER else OPTION_MODE_CAPTURE_RULER)
        }
        eraserTv?.setOnClickListener {
            captureView?.clearCapture()
        }
    }

    override fun onShown() {
        rootView?.let {
            val listener = computeInternalInsetsListener.bind()
            if (listener != null) {
                ReflectionUtils.addOnComputeInternalInsetsListener(it.viewTreeObserver, listener)
            }
        }
        controlPanelView?.let { touchHelper.bindControlView(it, onOnMoveListener) }
        val pair = readControlLocation()
        setControlLocation(pair.first, pair.second)
    }

    override fun onDimiss() {
        rootView?.let {
            ReflectionUtils.removeOnComputeInternalInsetsListener(it.viewTreeObserver)
        }
        touchHelper.removeControlView()
    }

    fun updateRulerMode(mode: Int) {
        val drawable = context.resources.getDrawable(R.drawable.ic_ruler_white)
        when (mode) {
            OPTION_MODE_CAPTURE_RULER -> {
                drawable?.colorFilter = PorterDuffColorFilter(highlightOptionColor, PorterDuff.Mode.SRC_IN)
                rulerTv?.setCompoundDrawablesRelativeWithIntrinsicBounds(null, drawable, null, null)
                captureView?.startCapture()
                rulerTv?.setTextColor(highlightOptionColor)
            }
            OPTION_MODE_RELEASE_RULER -> {
                drawable?.clearColorFilter()
                rulerTv?.setTextColor(normalOptionColor)
            }
        }
        rulerMode = mode
        updateTouchRegion()
    }

    /**
     * Only update when current rulerMode is specifiedMode.
     */
    fun updateTouchRegion(specifiedMode: Int ?= null) {
        if (specifiedMode != null && specifiedMode != rulerMode) return
        if (rulerMode == OPTION_MODE_CAPTURE_RULER) {
            computeInternalInsetsListener.setTouchableInsets(InputMethodService.Insets.TOUCHABLE_INSETS_FRAME)
            computeInternalInsetsListener.touchRegion = null
        } else {
            computeInternalInsetsListener.setTouchableInsets(InputMethodService.Insets.TOUCHABLE_INSETS_REGION)
            computeInternalInsetsListener.touchRegion = touchRegion
        }
    }

    /**
     * 获取坐标
     */
    private fun readControlLocation(): Pair<Int, Int> {
        val defaultLocationX = 50.dipInt
        val defaultLocationY = 0.dipInt
        var locationX = 0
        var locationY = 0
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            locationX = CommonPreferenceManager.getInt(CommonSpKey.SP_RULER_CONTROL_LOCATION_PORTRAIT_X, defaultLocationX)
            locationY = CommonPreferenceManager.getInt(CommonSpKey.SP_RULER_CONTROL_LOCATION_PORTRAIT_Y, defaultLocationY)
        } else {
            locationX = CommonPreferenceManager.getInt(CommonSpKey.SP_RULER_CONTROL_LOCATION_LAND_X, defaultLocationX)
            locationY = CommonPreferenceManager.getInt(CommonSpKey.SP_RULER_CONTROL_LOCATION_LAND_Y, defaultLocationY)
        }
        return locationX to locationY
    }

    /**
     * 保存坐标
     */
    private fun saveControlLocation(x: Int, y: Int) {
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            CommonPreferenceManager.putInt(CommonSpKey.SP_RULER_CONTROL_LOCATION_PORTRAIT_X, x, false)
            CommonPreferenceManager.putInt(CommonSpKey.SP_RULER_CONTROL_LOCATION_PORTRAIT_Y, y, false)
        } else {
            CommonPreferenceManager.putInt(CommonSpKey.SP_RULER_CONTROL_LOCATION_LAND_X, x, false)
            CommonPreferenceManager.putInt(CommonSpKey.SP_RULER_CONTROL_LOCATION_LAND_Y, y, false)
        }
        CommonPreferenceManager.apply()
    }

    private fun setControlLocation(left: Int, top: Int) {
        val lp = rulerActionBar?.layoutParams as? FrameLayout.LayoutParams
        lp?.let {
            lp.leftMargin = left
            lp.topMargin = top
            rulerActionBar?.layoutParams = lp
        }
    }

    private val onOnMoveListener: OnMoveListener = object : OnMoveListener {
        private var leftMargin = 0
        private var topMargin = 0
        private var windowWidth = 0
        private var windowHeight = 0
        override fun onMoveStart() {
            val lp = rulerActionBar?.layoutParams as? FrameLayout.LayoutParams
            leftMargin = lp?.leftMargin ?: 0
            topMargin = lp?.topMargin ?: 0
            controlPanelView?.drawable?.let {
                it.colorFilter = PorterDuffColorFilter(highlightOptionColor, PorterDuff.Mode.SRC_IN)
            }
            VibrateManager.vibrateLight()
            val metrics = context.resources.displayMetrics
            windowWidth = metrics.widthPixels
            windowHeight = metrics.heightPixels
        }

        override fun onMoving(diffX: Int, diffY: Int) {
            val lp = rulerActionBar?.layoutParams as? FrameLayout.LayoutParams
            val barWidth = rulerActionBar?.width ?: 0
            val barHeight = rulerActionBar?.height ?: 0
            lp?.let {
                val left = min(max(lp.leftMargin + diffX, 0), windowWidth -  barWidth)
                val top = min(max(lp.topMargin + diffY, 0), windowHeight - barHeight)
                lp.leftMargin = left
                lp.topMargin =  top
                rulerActionBar?.layoutParams = lp
            }
        }

        override fun onMoveEnd() {
            val lp = rulerActionBar?.layoutParams as? FrameLayout.LayoutParams
            val left = lp?.leftMargin ?: 0
            val top = lp?.topMargin ?: 0
            if (left != leftMargin || top != topMargin) {
                saveControlLocation(left, top)
            }
            controlPanelView?.drawable?.let {
                it.clearColorFilter()
            }
        }
    }
}