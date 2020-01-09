package me.gogosnail.workerbee.base.ui

import android.app.Activity
import android.app.Dialog
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.support.v4.app.FragmentActivity

/** Created by max on 2018/12/27.<br/>
 */
open class BaseDialog(private val activity: Activity) : Dialog(activity) , LifecycleObserver {

    init {
        if (activity is FragmentActivity) {
            activity.lifecycle.addObserver(this)
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy(){
        if (isShowing) {
            dismiss()
        }
        if (activity is FragmentActivity) {
            activity.lifecycle.addObserver(this)
        }
    }
}