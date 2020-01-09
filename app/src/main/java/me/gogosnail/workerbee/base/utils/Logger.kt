package me.gogosnail.workerbee.base.utils

import android.util.Log
import me.gogosnail.workerbee.BuildConfig

/** Created by max on 2018/12/26.<br/>
 */
object Logger {
    const val TAG = "WorkerBee"
    private fun allowLog(): Boolean = BuildConfig.DEBUG
    fun d(msg: String) {
        d(TAG, msg)
    }

    fun d(tag: String, msg: String) {
        if (allowLog())
            Log.d(tag, msg)
    }

    fun e(msg: String) {
        Logger.e(msg)
    }

    fun e(tag: String, msg: String) {
        if (allowLog())
            Log.e(tag, msg)
    }

}