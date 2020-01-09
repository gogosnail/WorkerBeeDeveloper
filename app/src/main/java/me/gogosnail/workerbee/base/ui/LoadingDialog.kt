package me.gogosnail.workerbee.base.ui

import android.app.Activity
import android.os.Bundle
import android.view.WindowManager
import me.gogosnail.workerbee.R

/** Created by max on 2018/12/27.<br/>
 * loading 弹窗
 */
class LoadingDialog(activity: Activity): BaseDialog(activity) {
    var isLoading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        window.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_loading)
    }

    override fun show() {
        isLoading = true
        super.show()
    }

    override fun dismiss() {
        isLoading = false
        super.dismiss()
    }
}