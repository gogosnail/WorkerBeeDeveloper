package me.gogosnail.workerbee.box.auto_install

import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo

/** Created by max on 2019/3/29.<br/>
 */
class MiuiInstallerHandler : BaseInstallerHandler() {
    override fun handle(rootNode: AccessibilityNodeInfo?, event: AccessibilityEvent) {
        log("handle event=$event")
        val packageName = event.packageName?:return
        when {
            packageName.contains("packageinstaller") || packageName.contains("com.miui.securitycenter") -> {
                if (!failedInstall(rootNode)) {
                    handleInstaller(rootNode)
                }
            }
        }
    }
}