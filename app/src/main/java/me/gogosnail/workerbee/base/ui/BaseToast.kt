package me.gogosnail.workerbee.base.ui

import android.widget.Toast
import me.gogosnail.workerbee.BaseApplication

/** Created by max on 2018/12/28.<br/>
 */
object BaseToast {
    private var toast: Toast? = null

    fun showToast(msg: String) {
        showCompleteToast(msg, Toast.LENGTH_SHORT, false)
    }

    fun showToastLong(msg: String) {
        showCompleteToast(msg, Toast.LENGTH_SHORT, false)
    }

    fun showToast(msg: Int) {
        showCompleteToast(BaseApplication.INSTANCE.resources.getString(msg), Toast.LENGTH_SHORT, false)
    }

    fun showToastLong(msg: Int) {
        showCompleteToast(BaseApplication.INSTANCE.resources.getString(msg), Toast.LENGTH_SHORT, false)
    }

    fun showCompleteToast(msg: String?, duration: Int, broke: Boolean) {
        msg?:return
        if (broke && toast != null) {
            toast?.cancel()
        }
        toast = Toast.makeText(BaseApplication.INSTANCE.applicationContext, msg, duration)
        toast?.show()
    }
}