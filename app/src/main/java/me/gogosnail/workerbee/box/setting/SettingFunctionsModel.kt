package me.gogosnail.workerbee.box.setting

import android.provider.Settings
import me.gogosnail.workerbee.BaseApplication
import me.gogosnail.workerbee.R
import me.gogosnail.workerbee.box.IFunctionsModel
import me.gogosnail.workerbee.data.FunctionsItem
import me.gogosnail.workerbee.box.app.ObserverAppOptionsActivity

/** Created by max on 2018/12/28.<br/>
 */
class SettingFunctionsModel : IFunctionsModel {
    override fun loadFunctions(): List<FunctionsItem> {
        val functions = mutableListOf<FunctionsItem>()
        val resources = BaseApplication.INSTANCE.resources
        functions.add(FunctionsItem.creator("change_observer",
                resources.getString(R.string.graphics_function_setting_observer_title),
                resources.getString(R.string.graphics_function_setting_observer_desc)) {
            intentInfo.targetClass = ObserverAppOptionsActivity::class.java.name
        })
        functions.add(FunctionsItem.creator("date_setting",
                resources.getString(R.string.graphics_function_setting_date_title),
                null) {
            intentInfo.action = Settings.ACTION_DATE_SETTINGS
        })
        functions.add(FunctionsItem.creator("developer_setting",
                resources.getString(R.string.graphics_function_setting_developer_title),
                null) {
            intentInfo.action = Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS
        })
        functions.add(FunctionsItem.creator("developer_setting",
                resources.getString(R.string.graphics_function_setting_wifi_title),
                null) {
            intentInfo.action = Settings.ACTION_WIFI_SETTINGS
        })
        return functions
    }
}