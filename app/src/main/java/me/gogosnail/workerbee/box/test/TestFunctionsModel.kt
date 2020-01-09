package me.gogosnail.workerbee.box.test

import me.gogosnail.workerbee.BaseApplication
import me.gogosnail.workerbee.R
import me.gogosnail.workerbee.box.IFunctionsModel
import me.gogosnail.workerbee.data.FunctionsItem

/** Created by max on 2018/12/27.<br/>
 */
class TestFunctionsModel : IFunctionsModel {
    override fun loadFunctions(): List<FunctionsItem> {
        val functions = mutableListOf<FunctionsItem>()
        val resources = BaseApplication.INSTANCE.resources
        functions.add(FunctionsItem.creator("test_schemas",
                resources.getString(R.string.test_function_schemas_title),
                resources.getString(R.string.test_function_schemas_desc)) {
            intentInfo.targetClass = TestSchemasOpenActivity::class.java.name
        })
        return functions
    }
}