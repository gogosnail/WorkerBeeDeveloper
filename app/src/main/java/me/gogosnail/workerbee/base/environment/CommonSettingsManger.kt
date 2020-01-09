package me.gogosnail.workerbee.base.environment

import android.content.Context
import me.gogosnail.workerbee.BaseApplication
import me.gogosnail.workerbee.base.CommonSpKey.Companion.SP_OBSERVER_APP_NAME
import me.gogosnail.workerbee.base.CommonSpKey.Companion.SP_OBSERVER_APP_PACKAGE

/**
 * 公用本地设置
 */
object CommonSettingsManger {
    const val COMMON_SP_NAME = "common_settings_sp"
    val sp = BaseApplication.INSTANCE.getSharedPreferences(COMMON_SP_NAME, Context.MODE_PRIVATE)
    val editor = sp.edit()

    fun getObserverAppName():String? {
        return sp.getString(SP_OBSERVER_APP_NAME, null)
    }

    fun getObserverAppPackage():String? {
        return sp.getString(SP_OBSERVER_APP_PACKAGE, null)
    }

    fun setObserverApp(name:String, packageName:String) {
        editor.putString(SP_OBSERVER_APP_NAME, name)
        editor.putString(SP_OBSERVER_APP_PACKAGE, packageName)
        editor.apply()
    }

    fun apply() {
        editor.apply()
    }

    fun commit() {
        editor.commit()
    }
}