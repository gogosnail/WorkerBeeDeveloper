package me.gogosnail.workerbee.base.utils

import android.content.Context
import android.os.Vibrator
import me.gogosnail.workerbee.BaseApplication

/** Created by max on 2019/12/17.<br/>
 */
object VibrateManager {
    var vibrator: Vibrator ?= null

    val LIGHT_VIBRATE_ARRAY = longArrayOf(0, 20)
    val HEAVY_VIBRATE_PATTERN = longArrayOf(0, 50)

    fun getDefaultVibrator(): Vibrator? {
        if (vibrator == null) {
            vibrator = BaseApplication.INSTANCE.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }
        return vibrator
    }

    fun vibrateLight() {
        try {
            getDefaultVibrator()?.vibrate(LIGHT_VIBRATE_ARRAY, -1)
        } catch (e: Exception) {

        }
    }

    fun vibrateHeavy() {
        try {
            getDefaultVibrator()?.vibrate(HEAVY_VIBRATE_PATTERN, -1)
        } catch (e: Exception) {

        }
    }
}