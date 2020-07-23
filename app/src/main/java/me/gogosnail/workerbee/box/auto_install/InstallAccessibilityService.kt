package me.gogosnail.workerbee.box.auto_install

import android.accessibilityservice.AccessibilityService
import android.os.Build
import android.support.annotation.RequiresApi
import android.view.accessibility.AccessibilityEvent
import me.gogosnail.workerbee.base.CrossSpKey.Companion.SP_AUTO_INSTALL_ENABLE
import me.gogosnail.workerbee.base.environment.CrossPreferenceManager
import me.gogosnail.workerbee.base.utils.Logger

/** Created by max on 2019/3/22.<br/>
 */

const val PRINT_LOG = true
const val TAG = "InstallAccessibilityService"

class InstallAccessibilityService : AccessibilityService() {
    companion object {
        const val PATTERN_INSTALLER = "packageinstaller"
    }

    private var installerHandler: BaseInstallerHandler? = null

    override fun onCreate() {
        super.onCreate()
        val mode = Build.MODEL.toLowerCase()
        log(" onCreate mode=$mode")
        installerHandler = when {
            mode.contains("vivo") -> {
                VivoInstallerHandler()
            }
            mode.contains("mi") -> {
                MiuiInstallerHandler()
            }
            mode.contains("oppo") -> {
                OppoInstallerHandler()
            }
            else -> {
                DefaultInstallerHandler()
            }
        }
    }

    override fun onInterrupt() {
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        event ?: return
        val packageName = event.packageName?.toString() ?: return
        val type = event.eventType ?: return
        val enable = CrossPreferenceManager.getDefaultSharedPreference(this).getBoolean(SP_AUTO_INSTALL_ENABLE, true)
        if (!enable) return
        log("onAccessibilityEvent1 packageName=$packageName type=$type event=$event enable=$enable")
        if (type != AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED
                && type != AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED
                && type != AccessibilityEvent.TYPE_WINDOWS_CHANGED) return
//        log("onAccessibilityEvent2 packageName=$packageName type=$type")
        installerHandler?.handle(rootInActiveWindow, event)
    }

    fun log(msg: String) {
        log(TAG, msg)
    }

    fun log(tag: String, msg: String) {
        if (PRINT_LOG) {
            Logger.d(tag, msg)
        }
    }
}