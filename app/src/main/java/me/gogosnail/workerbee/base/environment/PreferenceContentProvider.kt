package me.gogosnail.workerbee.base.environment

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.content.UriMatcher
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import android.util.LruCache
import me.gogosnail.workerbee.base.utils.Logger

/** Created by max on 2019/3/22.<br/>
 * SharedPreference base on ContentProvider.
 */
class PreferenceContentProvider: ContentProvider() {
    companion object {
        const val TAG = "PreferenceContentProvider"
        const val AUTHORITY = "me.gogosnail.workerbee.preference"
        const val URL = "content://$AUTHORITY"
        const val SUCCESS_ACTION = 1
        const val FAILED_ACTION = 0

        const val MATCH_SUCCESS = 1
        private val uriMatcher = UriMatcher(MATCH_SUCCESS)

        const val TYPE_INT = "int"
        const val TYPE_STRING = "string"
        const val TYPE_LONG = "long"
        const val TYPE_BOOLEAN = "boolean"
        const val TYPE_FLOAT = "float"
        const val TYPE_NONE = "none"

        const val PARAM_KEY_TYPE = "key_type"
        const val PARAM_KEY_NAME = "key_name"
        const val PARAM_SP_VALUE = "sp_value"
        const val PARAM_SP_NAME = "sp_name"

        fun isSupportType(type: String?): Boolean {
            return type in arrayListOf(TYPE_INT, TYPE_STRING, TYPE_LONG, TYPE_BOOLEAN, TYPE_FLOAT)
        }

        fun getUri(spName: String, type: String, keyName: String): Uri {
            return Uri.parse("$URL?$PARAM_SP_NAME=$spName&$PARAM_KEY_TYPE=$type&$PARAM_KEY_NAME=$keyName")
        }
    }
    private val spCache = LruCache<String, SharedPreferences?>(5)

    private fun getSharedPreferences(name: String): SharedPreferences? {
        var sp = spCache.get(name)
        if (sp == null) {
            sp = context?.getSharedPreferences(name, MODE_PRIVATE)
            spCache.put(name, sp)
        }
        return sp
    }

    init {
        uriMatcher.addURI(AUTHORITY, null ,MATCH_SUCCESS)
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
//        Logger.d(TAG, "insert uri=$uri values=$values")
        val type = getType(uri)
        if (type != TYPE_NONE) {
            val spName = getSpName(uri) ?: return null
            val keyName = getKeyName(uri) ?: return null
            val value = values?.get(PARAM_SP_VALUE)
            getSharedPreferences(spName)?.let { sp ->
                if (value != null) {
                    setValue(sp, type, keyName, value)
                } else {
                    sp.edit().remove(keyName).apply()
                }
                return uri
            }
        }
        return null
    }

    override fun query(uri: Uri, projection: Array<String>?, selection: String?, selectionArgs: Array<String>?, sortOrder: String?): Cursor? {
//        Logger.d(TAG, "query uri=$uri")
        var cursor: MatrixCursor? = null
        val type = getType(uri)
        if (type != TYPE_NONE) {
            val spName = getSpName(uri) ?: return null
            val keyName = getKeyName(uri) ?: return null
            getSharedPreferences(spName)?.let { sp ->
                getValue(sp, type, keyName) ?.let {
                    cursor = MatrixCursor(arrayOf(PARAM_SP_VALUE))
                    cursor?.addRow(arrayListOf(it))
                }
            }
        }
        return cursor
    }

    override fun onCreate(): Boolean {
        return true
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int {
        return if (insert(uri, values) != null) SUCCESS_ACTION else FAILED_ACTION
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val type = getType(uri)
        if (type != TYPE_NONE) {
            val spName = getSpName(uri) ?: return FAILED_ACTION
            val keyName = getKeyName(uri) ?: return FAILED_ACTION
            getSharedPreferences(spName)?.let { sp ->
                sp.edit().remove(keyName).apply()
                return SUCCESS_ACTION
            }
        }
        return FAILED_ACTION
    }

    /**
     * Get the sp value type.
     */
    override fun getType(uri: Uri): String {
        return if (uriMatcher.match(uri) == MATCH_SUCCESS) {
            val type = uri.getQueryParameter(PARAM_KEY_TYPE)
            if (isSupportType(type)) {
                type
            } else {
                TYPE_NONE
            }
        } else {
            TYPE_NONE
        }
    }

    private fun setValue(sp: SharedPreferences, keyType: String,  key: String, value: Any) {
        val edit = sp.edit()
        when(keyType) {
            TYPE_INT -> edit.putInt(key, value as Int)
            TYPE_LONG -> edit.putLong(key, value as Long)
            TYPE_FLOAT -> edit.putFloat(key, value as Float)
            TYPE_BOOLEAN -> edit.putBoolean(key, value as Boolean)
            TYPE_STRING -> edit.putString(key, value as String)
        }
        edit.apply()
    }

    private fun getValue(sp: SharedPreferences, keyType: String,  key: String):Any? {
        if (!sp.contains(key)) {
            return null
        }
        return when(keyType) {
            TYPE_INT -> sp.getInt(key, 0)
            TYPE_LONG -> sp.getLong(key, 0L)
            TYPE_FLOAT -> sp.getFloat(key, 0f)
            TYPE_BOOLEAN -> sp.getBoolean(key, false)
            TYPE_STRING -> sp.getString(key, null)
            else -> null
        }
    }

    /**
     * Get the sp file name.
     */
    fun getSpName(uri: Uri): String? {
        return uri.getQueryParameter(PARAM_SP_NAME)
    }

    /**
     * Get the key name.
     */
    fun getKeyName(uri: Uri): String? {
        return uri.getQueryParameter(PARAM_KEY_NAME)
    }

    /**
     * Get the key type.
     */
    fun getKeyType(uri: Uri): String? {
        return uri.getQueryParameter(PARAM_KEY_TYPE)
    }
}