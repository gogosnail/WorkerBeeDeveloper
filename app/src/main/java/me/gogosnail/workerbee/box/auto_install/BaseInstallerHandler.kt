package me.gogosnail.workerbee.box.auto_install

import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import me.gogosnail.workerbee.base.utils.Logger
import me.gogosnail.workerbee.box.utils.AccessibilityUtils

/** Created by max on 2019/3/26.<br/>
 */
abstract class BaseInstallerHandler {

    protected fun handleInstaller(rootNode:AccessibilityNodeInfo?) {
        val rootNode = rootNode?:return
        AccessibilityUtils.clickNodesByText(rootNode, "继续安装")
        checkClickOneNodeByText(rootNode, "安装")
        AccessibilityUtils.clickNodesByText(rootNode, "打开")
    }

    fun checkClickOneNodeByText(rootNode:AccessibilityNodeInfo?, text: String) {
        if (AccessibilityUtils.hasTextOnlyOneNode(rootNode, text)) {
            AccessibilityUtils.clickNodesByText(rootNode, text)
        }
    }

    abstract fun handle(rootNode: AccessibilityNodeInfo?, event: AccessibilityEvent)


    fun failedInstall(rootNode: AccessibilityNodeInfo?): Boolean {
        return AccessibilityUtils.hasNodes(rootNode, "安装失败")
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