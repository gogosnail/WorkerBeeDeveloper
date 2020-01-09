package me.gogosnail.workerbee.base.environment

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context

/** Created by max on 2019/3/22.<br/>
 * Cross process shared preference.
 */
open class CrossSharedPreference(val context: Context, val spName: String) {

    private fun getContentResolver(): ContentResolver {
        return context.contentResolver
    }

    fun getInt(name: String, defaultValue: Int): Int {
        val uri = PreferenceContentProvider.getUri(spName, PreferenceContentProvider.TYPE_INT, name)
        val cursor = getContentResolver().query(uri, null, null, null, null)
        cursor?.moveToFirst()
        val value =  cursor?.getInt(0) ?: defaultValue
        cursor?.close()
        return value
    }

    fun putInt(name: String, value: Int) {
        val uri = PreferenceContentProvider.getUri(spName, PreferenceContentProvider.TYPE_INT, name)
        val values = ContentValues()
        values.put(PreferenceContentProvider.PARAM_SP_VALUE, value)
        getContentResolver().insert(uri, values)
    }

    fun getLong(name: String, defaultValue: Long): Long {
        val uri = PreferenceContentProvider.getUri(spName, PreferenceContentProvider.TYPE_LONG, name)
        val cursor = getContentResolver().query(uri, null, null, null, null)
        cursor?.moveToFirst()
        val value =  cursor?.getLong(0) ?: defaultValue
        cursor?.close()
        return value
    }

    fun putLong(name: String, value: Long) {
        val uri = PreferenceContentProvider.getUri(spName, PreferenceContentProvider.TYPE_LONG, name)
        val values = ContentValues()
        values.put(PreferenceContentProvider.PARAM_SP_VALUE, value)
        getContentResolver().insert(uri, values)
    }

    fun getFloat(name: String, defaultValue: Float): Float {
        val uri = PreferenceContentProvider.getUri(spName, PreferenceContentProvider.TYPE_FLOAT, name)
        val cursor = getContentResolver().query(uri, null, null, null, null)
        cursor?.moveToFirst()
        val value =  cursor?.getFloat(0) ?: defaultValue
        cursor?.close()
        return value
    }

    fun putFloat(name: String, value: Float) {
        val uri = PreferenceContentProvider.getUri(spName, PreferenceContentProvider.TYPE_FLOAT, name)
        val values = ContentValues()
        values.put(PreferenceContentProvider.PARAM_SP_VALUE, value)
        getContentResolver().insert(uri, values)
    }

    fun getString(name: String, defaultValue: String): String {
        val uri = PreferenceContentProvider.getUri(spName, PreferenceContentProvider.TYPE_STRING, name)
        val cursor = getContentResolver().query(uri, null, null, null, null)
        cursor?.moveToFirst()
        val value =  cursor?.getString(0) ?: defaultValue
        cursor?.close()
        return value
    }

    fun putString(name: String, value: String) {
        val uri = PreferenceContentProvider.getUri(spName, PreferenceContentProvider.TYPE_STRING, name)
        val values = ContentValues()
        values.put(PreferenceContentProvider.PARAM_SP_VALUE, value)
        getContentResolver().insert(uri, values)
    }

    fun getBoolean(name: String, defaultValue: Boolean): Boolean {
        val uri = PreferenceContentProvider.getUri(spName, PreferenceContentProvider.TYPE_BOOLEAN, name)
        val cursor = getContentResolver().query(uri, null, null, null, null)
        cursor?.moveToFirst()
        val raw = cursor?.getString(0)
        val value = raw?.toBoolean() ?: defaultValue
        cursor?.close()
        return value
    }

    fun putBoolean(name: String, value: Boolean) {
        val uri = PreferenceContentProvider.getUri(spName, PreferenceContentProvider.TYPE_BOOLEAN, name)
        val values = ContentValues()
        values.put(PreferenceContentProvider.PARAM_SP_VALUE, value)
        getContentResolver().insert(uri, values)
    }
}