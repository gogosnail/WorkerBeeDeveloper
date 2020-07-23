package me.gogosnail.workerbee.box.adb

import android.view.View
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_adbmessage_clip_desc.*
import me.gogosnail.workerbee.R
import me.gogosnail.workerbee.base.ui.BaseActivity
import me.gogosnail.workerbee.base.utils.ClipboardUtils

/** Created by max on 2020/07/23.<br/>
 */
class AdbClipDescriptionActivity : BaseActivity() {
    override fun layoutId(): Int = R.layout.activity_adbmessage_clip_desc

    override fun initView() {
        super.initView()
        tv_describe_set_clip_cmd.setOnLongClickListener(copyCmdLongClick)
        tv_describe_get_clip_cmd.setOnLongClickListener(copyCmdLongClick)
    }

    private val copyCmdLongClick = object : View.OnLongClickListener {
        override fun onLongClick(v: View?): Boolean {
            val text = (v as? TextView)?.text?.toString()
            if (text.isNullOrEmpty()) {
                Toast.makeText(this@AdbClipDescriptionActivity, "拷贝命令失败", Toast.LENGTH_SHORT).show()
            } else {
                ClipboardUtils.setClip(this@AdbClipDescriptionActivity, text)
            }

            return true
        }

    }
}