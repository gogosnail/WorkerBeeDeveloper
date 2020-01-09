package me.gogosnail.workerbee.box.foreground_monitor

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
import me.gogosnail.workerbee.base.utils.Logger
import me.gogosnail.workerbee.base.utils.ReflectionUtils
import me.gogosnail.workerbee.base.utils.VibrateManager
import me.gogosnail.workerbee.base.widget.ControlPanelTouchHelper
import me.gogosnail.workerbee.base.widget.FloatingWindow
import me.gogosnail.workerbee.base.widget.OnComputeInternalInsetsListener
import me.gogosnail.workerbee.base.widget.OnMoveListener
import kotlin.math.max
import kotlin.math.min

/** Created by max on 2019/3/24.<br/>
 */
class ForegroundMonitorFloatingWindow : FloatingWindow() {
    var contentView: View? = null
    var topWindowTv: TextView? = null
    var panelLl: LinearLayout? = null
    var controlPanelView: ImageView? = null
    private val touchRegion = Region()
    val highlightOptionColor = BaseApplication.INSTANCE.resources.getColor(R.color.faded_red)
    private val touchHelper = ControlPanelTouchHelper()
    private val computeInternalInsetsListener: OnComputeInternalInsetsListener = OnComputeInternalInsetsListener()
    override fun onCreateView(): View {
        windowLp.width = WindowManager.LayoutParams.MATCH_PARENT
        windowLp.height = WindowManager.LayoutParams.MATCH_PARENT
        contentView = LayoutInflater.from(context).inflate(R.layout.layout_foreground_monitor_view, null)
        topWindowTv = contentView!!.findViewById(R.id.tv_top_window_name)
        panelLl = contentView!!.findViewById(R.id.ll_panel)
        controlPanelView = contentView!!.findViewById(R.id.iv_control)
        windowLp.alpha = 0.7f
        panelLl?.addOnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
            updateTouchRegion()
        }
        panelLl?.setOnClickListener {

        }
        return contentView!!
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
    }

    fun updateTouchRegion() {
        if (panelLl == null || controlPanelView == null) {
            touchRegion.setEmpty()
            computeInternalInsetsListener.touchRegion = touchRegion
            return
        }
        val left = panelLl!!.left + controlPanelView!!.left
        val right = left + controlPanelView!!.right
        val top = panelLl!!.top + controlPanelView!!.top
        val bottom = top + controlPanelView!!.bottom
        touchRegion.set(left, top, right, bottom)
        computeInternalInsetsListener.touchRegion = touchRegion
    }

    fun showTopWindowName(name: String?) {
        show()
        topWindowTv?.text = name
    }

    override fun onShown() {
        rootView?.let {
            computeInternalInsetsListener.setTouchableInsets(InputMethodService.Insets.TOUCHABLE_INSETS_REGION)
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

    /**
     * 获取坐标
     */
    private fun readControlLocation(): Pair<Int, Int> {
        val defaultLocationX = 0.dipInt
        val defaultLocationY = 50.dipInt
        var locationX = 0
        var locationY = 0
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            locationX = CommonPreferenceManager.getInt(CommonSpKey.SP_FWM_CONTROL_LOCATION_PORTRAIT_X, defaultLocationX)
            locationY = CommonPreferenceManager.getInt(CommonSpKey.SP_FWM_CONTROL_LOCATION_PORTRAIT_Y, defaultLocationY)
        } else {
            locationX = CommonPreferenceManager.getInt(CommonSpKey.SP_FWM_CONTROL_LOCATION_LAND_X, defaultLocationX)
            locationY = CommonPreferenceManager.getInt(CommonSpKey.SP_FWM_CONTROL_LOCATION_LAND_Y, defaultLocationY)
        }
        return locationX to locationY
    }

    /**
     * 保存坐标
     */
    private fun saveControlLocation(x: Int, y: Int) {
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            CommonPreferenceManager.putInt(CommonSpKey.SP_FWM_CONTROL_LOCATION_PORTRAIT_X, x, false)
            CommonPreferenceManager.putInt(CommonSpKey.SP_FWM_CONTROL_LOCATION_PORTRAIT_Y, y, false)
        } else {
            CommonPreferenceManager.putInt(CommonSpKey.SP_FWM_CONTROL_LOCATION_LAND_X, x, false)
            CommonPreferenceManager.putInt(CommonSpKey.SP_FWM_CONTROL_LOCATION_LAND_Y, y, false)
        }
        CommonPreferenceManager.apply()
    }

    private fun setControlLocation(left: Int, top: Int) {
        val lp = panelLl?.layoutParams as? FrameLayout.LayoutParams
        lp?.let {
            lp.leftMargin = left
            lp.topMargin = top
            panelLl?.layoutParams = lp
        }
    }

    private val onOnMoveListener: OnMoveListener = object : OnMoveListener {
        private var leftMargin = 0
        private var topMargin = 0
        private var windowWidth = 0
        private var windowHeight = 0
        override fun onMoveStart() {
            val lp = panelLl?.layoutParams as? FrameLayout.LayoutParams
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
            val lp = panelLl?.layoutParams as? FrameLayout.LayoutParams
            val barWidth = panelLl?.width ?: 0
            val barHeight = panelLl?.height ?: 0
            lp?.let {
                val left = min(max(lp.leftMargin + diffX, 0), windowWidth -  barWidth)
                val top = min(max(lp.topMargin + diffY, 0), windowHeight - barHeight)
                lp.leftMargin = left
                lp.topMargin =  top
                panelLl?.layoutParams = lp
            }
        }

        override fun onMoveEnd() {
            val lp = panelLl?.layoutParams as? FrameLayout.LayoutParams
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