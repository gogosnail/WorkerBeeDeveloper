package me.gogosnail.workerbee.box.ruler

import android.annotation.SuppressLint

/** Created by max on 2019/12/16.<br/>
 */
object RulerWindowManager {
    @SuppressLint("StaticFieldLeak")
    private var window: RulerFloatingWindow ?= null

    fun isWindowShown(): Boolean {
        return window?.shown == true
    }

    fun showWindow(shown: Boolean) {
        if (shown) {
            if (window == null) {
                window = RulerFloatingWindow()
            }
            window?.show()
        } else {
            window?.dismiss()
            window = null
        }
    }
}