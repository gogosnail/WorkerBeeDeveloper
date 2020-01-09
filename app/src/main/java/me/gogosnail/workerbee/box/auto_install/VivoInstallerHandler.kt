package me.gogosnail.workerbee.box.auto_install

import android.os.Bundle
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import me.gogosnail.workerbee.BaseApplication
import me.gogosnail.workerbee.base.CrossSpKey.Companion.SP_SYSTEM_ACCOUNT_PASSWORD
import me.gogosnail.workerbee.base.environment.CrossPreferenceManager
import me.gogosnail.workerbee.box.utils.AccessibilityUtils

/** Created by max on 2019/3/26.<br/>
 */
class VivoInstallerHandler : BaseInstallerHandler() {

    private fun handleVivoAccount(rootNode: AccessibilityNodeInfo?) {
        val rootNode = rootNode ?: return
        val inputNode = AccessibilityUtils.findFirstChildByClassName("android.widget.EditText", rootNode)
        log("handleVivoAccount inputNode=$inputNode")
        inputNode?.let {
            val arguments = Bundle()
            val text = CrossPreferenceManager.getDefaultSharedPreference(BaseApplication.INSTANCE).getString(SP_SYSTEM_ACCOUNT_PASSWORD, AutoInstallToolActivity.DEFAULT_ACCOUNT_PASSWORD)
            if (!text.isNullOrEmpty()) {
                arguments.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, text)
                it.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments)
                AccessibilityUtils.clickNodesByText(rootNode, "确定")
            }
            it.recycle()
        }
    }

    override fun handle(rootNode: AccessibilityNodeInfo?, event: AccessibilityEvent) {
        val packageName = event.packageName?:return
        when {
            packageName.contains("com.vivo.secime.service") -> {
                handleVivoAccount(rootNode)
            }
            packageName.contains("packageinstaller") || packageName.contains("com.bbk.account")-> {
                handleVivoAccount(rootNode)
                if (!failedInstall(rootNode)) {
                    handleInstaller(rootNode)
                }
            }
        }
    }
}