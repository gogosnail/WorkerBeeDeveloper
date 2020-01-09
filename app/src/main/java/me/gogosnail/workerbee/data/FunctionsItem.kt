package me.gogosnail.workerbee.data

/** Created by max on 2018/12/26.<br/>
 * 工具列表项
 */
class FunctionsItem(val tag: String, val title: String, var desc: String?, val intentInfo: IntentInfo) {
    /**
     * 返回[true] 表示已处理click，不在触发默认click
     */
    var onClick:(() ->Boolean)? = null
    companion object {
        fun creator(tag: String, title: String, desc:String?, builder: FunctionsItem.() -> Unit): FunctionsItem {
            val functionsItem = FunctionsItem(tag, title, desc, IntentInfo())
            builder.invoke(functionsItem)
            return functionsItem
        }
    }
}