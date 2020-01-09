package me.gogosnail.workerbee.box.network

import me.gogosnail.workerbee.BaseApplication
import me.gogosnail.workerbee.R
import me.gogosnail.workerbee.box.IFunctionsModel
import me.gogosnail.workerbee.box.network.browser.BrowserActivity
import me.gogosnail.workerbee.data.FunctionsItem

/** Created by max on 2018/12/28.<br/>
 */
class NetworkFunctionsModel : IFunctionsModel {
    override fun loadFunctions(): List<FunctionsItem> {
        val functions = mutableListOf<FunctionsItem>()
        val resources = BaseApplication.INSTANCE.resources
        functions.add(FunctionsItem.creator("browser",
                resources.getString(R.string.network_function_webview),
                resources.getString(R.string.network_function_webview_desc)) {
            intentInfo.targetClass = BrowserActivity::class.java.name
        })
        return functions
    }
}