package me.gogosnail.workerbee.base.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import me.gogosnail.workerbee.R
import me.gogosnail.workerbee.base.ui.BaseToast

/** Created by max on 2018/12/28.<br/>
 */
object ClipboardUtils {
    fun setClip(context: Context, text: CharSequence) {
        (context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).primaryClip = ClipData.newPlainText("", text)
        BaseToast.showToast(R.string.tips_clipboard_copy)
    }

    fun getClip(context: Context): CharSequence? {
        return (context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).primaryClip.getItemAt(0).text
    }
}