package me.gogosnail.workerbee.box.foreground_monitor

import android.accessibilityservice.AccessibilityService
import android.content.ComponentName
import android.view.accessibility.AccessibilityEvent
import me.gogosnail.workerbee.base.CrossSpKey.Companion.SP_FOREGROUND_MONITOR_ENABLE
import me.gogosnail.workerbee.base.environment.CrossPreferenceManager


/** Created by max on 2019/3/22.<br/>
 */
class ForegroundMonitorAccessibilityService : AccessibilityService() {
    companion object {
        const val TAG = "ForegroundMonitorAccessi"
    }

    private var window: ForegroundMonitorFloatingWindow? = null

    override fun onInterrupt() {
    }

    override fun onCreate() {
        super.onCreate()
        window = ForegroundMonitorFloatingWindow()
        window?.create()
    }

    override fun onDestroy() {
        super.onDestroy()
        window?.dismiss()
        window = null
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        event ?: return
        if (event.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            val enable = CrossPreferenceManager.getDefaultSharedPreference(this).getBoolean(SP_FOREGROUND_MONITOR_ENABLE, true)
            if (!enable) {
                window?.dismiss()
                return
            }
            try {
                val componentName = ComponentName(
                        event.packageName.toString(),
                        event.className.toString())
                val activityInfo = packageManager.getActivityInfo(componentName, 0)
                if (activityInfo != null) window?.showTopWindowName(componentName.flattenToShortString())
            } catch (e: Exception) {

            }
        }
    }
}