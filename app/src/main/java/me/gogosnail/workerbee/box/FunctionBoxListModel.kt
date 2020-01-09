package me.gogosnail.workerbee.box

import me.gogosnail.workerbee.BaseApplication
import me.gogosnail.workerbee.base.FUNCTION_TAG
import me.gogosnail.workerbee.box.app.AppListScanActivity
import me.gogosnail.workerbee.box.auto_install.AutoInstallToolActivity
import me.gogosnail.workerbee.box.foreground_monitor.ForegroundMonitorActivity
import me.gogosnail.workerbee.box.network.browser.BrowserActivity
import me.gogosnail.workerbee.box.ruler.RulerSettingActivity
import me.gogosnail.workerbee.data.*

/** Created by max on 2018/12/26.<br/>
 * 生成FunctionBox列表
 */
class FunctionBoxListModel {

    /**
     * 添加一级功能菜单
     */
    fun createList(): MutableList<FunctionBoxBean> {
        val list = mutableListOf<FunctionBoxBean>()
        list.addItem(FUNCTION_BOX_AUTO_INSTALL)
        list.addItem(FUNCTION_BOX_FOREGROUND_WINDOW)
        list.addItem(FUNCTION_BOX_RULER)
        list.addItem(FUNCTION_BOX_SETTING)
        list.addItem(FUNCTION_BOX_NETWORK)
        list.addItem(FUNCTION_BOX_APP)
        list.addItem(FUNCTION_BOX_TEST)
        return list
    }

    /**
     * 一级功能菜单的路由
     */
    private fun createIntentInfo(tag: String):IntentInfo {
        val intent = IntentInfo()
        when(tag) {
            FUNCTION_BOX_APP -> {
                intent.targetClass = AppListScanActivity::class.java.name
                intent.bundle.putString(FUNCTION_TAG, tag)
            }
            FUNCTION_BOX_RULER -> {
                intent.targetClass = RulerSettingActivity::class.java.name
            }
            FUNCTION_BOX_AUTO_INSTALL -> {
                intent.targetClass = AutoInstallToolActivity::class.java.name
            }
            FUNCTION_BOX_NETWORK-> {
                intent.targetClass = BrowserActivity::class.java.name
            }
            FUNCTION_BOX_FOREGROUND_WINDOW -> {
                intent.targetClass = ForegroundMonitorActivity::class.java.name
            }
            else -> {
                intent.targetClass = FunctionListActivity::class.java.name
                intent.bundle.putString(FUNCTION_TAG, tag)
            }
        }
        return intent
    }

    private fun MutableList<FunctionBoxBean>.addItem(tag: String) {
        val resources = BaseApplication.INSTANCE.applicationContext.resources
        val iconId = resources.getIdentifier("ic_function_box_$tag", "drawable", BaseApplication.INSTANCE.packageName)
        val nameId = resources.getIdentifier("function_box_$tag", "string", BaseApplication.INSTANCE.packageName)
        add(FunctionBoxBean(tag, resources.getString(nameId), resources.getDrawable(iconId), createIntentInfo(tag)))
    }
}