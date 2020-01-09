package me.gogosnail.workerbee.box.app

import me.gogosnail.workerbee.BaseApplication
import me.gogosnail.workerbee.R
import me.gogosnail.workerbee.box.IFunctionsModel
import me.gogosnail.workerbee.data.FunctionsItem

/** Created by max on 2018/12/27.<br/>
 */
class AppFunctionsModel : IFunctionsModel {
    override fun loadFunctions(): List<FunctionsItem> {
        val functions = mutableListOf<FunctionsItem>()
        val resources = BaseApplication.INSTANCE.resources
        functions.add(FunctionsItem.creator("app_list",
                resources.getString(R.string.app_function_scan_title),
                resources.getString(R.string.app_function_scan_desc)) {
            intentInfo.targetClass = AppListScanActivity::class.java.name
        })
        return functions
    }
}