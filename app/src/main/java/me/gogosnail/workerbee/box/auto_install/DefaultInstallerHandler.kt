package me.gogosnail.workerbee.box.auto_install

import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo

/** Created by max on 2019/3/26.<br/>
 */
class DefaultInstallerHandler: BaseInstallerHandler() {

    override fun handle(rootNode: AccessibilityNodeInfo?, event: AccessibilityEvent) {
        log("DefaultInstallerHandler", "handle event=$event")
        val packageName = event.packageName
        if (!failedInstall(rootNode) && packageName.contains("packageinstaller")) {
            handleInstaller(rootNode)
        }
    }
}