package me.gogosnail.workerbee.base.widget

import android.content.ComponentCallbacks
import android.content.Context
import android.content.res.Configuration
import android.graphics.PixelFormat
import android.os.Build
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import me.gogosnail.workerbee.BaseApplication
import me.gogosnail.workerbee.base.utils.Logger

/** Created by max on 2019/3/24.<br/>
 */
abstract class FloatingWindow(
        val context: Context = BaseApplication.INSTANCE.applicationContext,
        var locationX: Int = 0,
        var locationY: Int = 0
) : ComponentCallbacks {
    var rootView: View? = null
    var windowLp: WindowManager.LayoutParams =
        WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)
    val windowManager: WindowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    var orientation: Int = Configuration.ORIENTATION_PORTRAIT
    var created = false
    var shown = false

    init {
        val lp = windowLp
        lp.gravity = Gravity.START or Gravity.TOP
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        lp.format = PixelFormat.TRANSPARENT
        lp.packageName = BaseApplication.INSTANCE.packageName
        lp.flags = WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR  or
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
        lp.x = locationX
        lp.y = locationY
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            lp.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            lp.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT
        }
    }

    fun create() {
        created = true
        shown = false
        rootView = onCreateView()
        rootView?.let {
            orientation = context.resources.configuration.orientation
            it.addOnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
                onWindowBoundsChanged()
            }
        }
    }

    abstract fun onCreateView(): View

    override fun onConfigurationChanged(newConfig: Configuration) {
        orientation = newConfig.orientation
    }

    open fun onWindowBoundsChanged() {

    }

    override fun onLowMemory() {
        Logger.e("onLowMemory")
    }

    fun show() {
        if (!created) create()
        if (!shown) {
            try {
                windowManager.addView(rootView, windowLp)
                onShown()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            shown = true
        }
    }

    open fun onShown() {

    }

    fun setLocation(x: Int, y: Int) {
        locationX = x
        locationY = y
        val lp = windowLp
        lp.x = x
        lp.y = y
        if (shown) {
            windowManager.updateViewLayout(rootView, lp)
        }
    }

    fun dismiss() {
        if (shown) {
            try {
                windowManager.removeView(rootView)
                onDimiss()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            shown = false
        }
    }

    open fun onDimiss() {

    }

    fun destory() {
        created = false
    }
}