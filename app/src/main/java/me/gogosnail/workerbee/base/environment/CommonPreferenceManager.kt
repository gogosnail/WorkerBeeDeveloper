package me.gogosnail.workerbee.base.environment

import android.preference.PreferenceManager
import me.gogosnail.workerbee.BaseApplication

/** Created by max on 2019/12/16.<br/>
 */
object CommonPreferenceManager {
    private val sp = PreferenceManager.getDefaultSharedPreferences(BaseApplication.INSTANCE)
    private val editor = sp.edit()

    fun getInt(key: String, defaultValue: Int): Int {
        return sp.getInt(key, defaultValue)
    }

    fun putInt(key: String, value: Int, apply:Boolean = true) {
        editor.putInt(key, value)
        if (apply) apply()
    }

    fun getLong(key: String, defaultValue: Long): Long {
        return sp.getLong(key, defaultValue)
    }

    fun putLong(key: String, value: Long, apply: Boolean = true) {
        editor.putLong(key, value)
        if (apply) apply()
    }

    fun getString(key: String, defaultValue: String): String {
        return sp.getString(key, defaultValue) ?: defaultValue
    }

    fun putString(key: String, value: String, apply: Boolean = true) {
        editor.putString(key, value)
        if (apply) apply()
    }

    fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return sp.getBoolean(key, defaultValue) ?: defaultValue
    }

    fun putBoolean(key: String, value: Boolean, apply: Boolean = true) {
        editor.putBoolean(key, value)
        if (apply) apply()
    }

    fun apply() {
        editor.apply()
    }
}