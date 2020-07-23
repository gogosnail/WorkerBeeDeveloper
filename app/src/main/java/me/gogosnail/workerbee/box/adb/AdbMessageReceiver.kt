package me.gogosnail.workerbee.box.adb

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import me.gogosnail.workerbee.R
import me.gogosnail.workerbee.base.utils.ClipboardUtils
import me.gogosnail.workerbee.base.utils.Logger

/** Created by max on 2020/07/23.<br/>
 * 通过Adb收发消息
 */
class AdbMessageReceiver: BroadcastReceiver() {

    companion object {
        private const val TAG = "AdbMessageReceiver"
        private const val SET_CLIP = "workerbee.setclip"
        private const val GET_CLIP = "workerbee.getclip"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        context?:return
        val action = intent?.action
        Logger.d(TAG, "onReceive action:$action")
        when(action) {
            SET_CLIP -> {
                val clipTxt = intent.extras?.getString("s")
                Logger.d(TAG, "onReceive action:$action clipTxt:$clipTxt")
                if (clipTxt.isNullOrEmpty()) {
                    resultCode = Activity.RESULT_CANCELED
                    resultData = "没有收到文本，使用以下命令:\nadb shell am broadcast -a workerbee.setclip -f 16777216 -es '要复制的文本'\n"
                } else {
                    ClipboardUtils.setClip(context, clipTxt)
                    resultCode = Activity.RESULT_OK
                    resultData = context.getString(R.string.tips_clipboard_copy, clipTxt)
                }
            }
            GET_CLIP -> {
                val clipTxt = ClipboardUtils.getClip(context)
                Logger.d(TAG, "onReceive action:$action clipTxt:$clipTxt")
                if (clipTxt.isNullOrEmpty()) {
                    resultCode = Activity.RESULT_CANCELED
                    resultData = "error:剪切板无内容"
                } else {
                    resultCode = Activity.RESULT_OK
                    resultData = "剪切板:$clipTxt"
                }
            }
        }
    }

}