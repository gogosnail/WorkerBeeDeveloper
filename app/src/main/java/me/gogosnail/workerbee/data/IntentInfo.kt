package me.gogosnail.workerbee.data

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import me.gogosnail.workerbee.BaseApplication

/** Created by max on 2018/12/26.<br/>
 * 跳转信息
 */
class IntentInfo {
    var packageName = BaseApplication.INSTANCE.packageName
    var targetClass: String? = null
    var action: String? = null
    val bundle = Bundle()

    constructor() {
    }

    constructor(action: String) {
        this.action = action
    }

    constructor(packageName: String, targetClass: String) {
        this.packageName = packageName
        this.targetClass = targetClass
    }

    fun buildIntent(): Intent {
        val intent = Intent()
        intent.action = action
        if (targetClass != null) {
            intent.component = ComponentName(packageName, targetClass)
        }
        intent.putExtras(bundle)
        return intent
    }
}