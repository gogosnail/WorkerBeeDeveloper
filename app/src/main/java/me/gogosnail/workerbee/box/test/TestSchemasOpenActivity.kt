package me.gogosnail.workerbee.box.test

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_test_schemas_open.*
import me.gogosnail.workerbee.R
import me.gogosnail.workerbee.base.ui.BaseActivity

/** Created by max on 2018/12/27.<br/>
 * 应用列表扫描
 */
class TestSchemasOpenActivity : BaseActivity() {
    override fun layoutId() = R.layout.activity_test_schemas_open
    val schemas = arrayOf("")

    override fun initView() {
        btn_jump.setOnClickListener {
            val schemas = et_schemas.text?.toString()
            if (!schemas.isNullOrEmpty()) {
                try {
                    val intent = Intent()
                    intent.action = "android.intent.action.VIEW"
                    intent.data = Uri.parse(schemas)
                    startActivity(intent)
                } catch (e:Exception) {
                    e.printStackTrace()
                }
            } else {
                et_schemas.setBackgroundResource(R.drawable.shape_line_red_radius_8)
                et_schemas.postDelayed({
                    et_schemas.setBackgroundResource(R.drawable.shape_line_gray_radius_8)
                }, 1000L)
            }
        }
    }

}